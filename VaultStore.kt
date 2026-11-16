package com.apivault.data

import android.content.Context
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class VaultItem(val id: String, val label: String, val provider: String, val maskedSuffix: String)

class VaultStore(context: Context) {
    private val prefs = context.getSharedPreferences("api_vault_secure_index", Context.MODE_PRIVATE)
    private val alias = "api_vault_master_aes"
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    private fun key(): SecretKey {
        (keyStore.getKey(alias, null) as? SecretKey)?.let { return it }
        val generator = KeyGenerator.getInstance("AES", "AndroidKeyStore")
        generator.init(android.security.keystore.KeyGenParameterSpec.Builder(
            alias,
            android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build())
        return generator.generateKey()
    }

    suspend fun add(label: String, provider: String, secret: String) = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID().toString()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key())
        val encrypted = cipher.doFinal(secret.toByteArray(Charsets.UTF_8))
        val record = JSONObject().put("label", label).put("provider", provider)
            .put("iv", Base64.encodeToString(cipher.iv, Base64.NO_WRAP))
            .put("data", Base64.encodeToString(encrypted, Base64.NO_WRAP))
            .put("suffix", secret.takeLast(4))
        val all = JSONArray(prefs.getString("records", "[]"))
        all.put(JSONObject().put("id", id).put("record", record))
        prefs.edit().putString("records", all.toString()).apply()
    }

    fun readAll(): List<VaultItem> {
        val all = JSONArray(prefs.getString("records", "[]"))
        return (0 until all.length()).mapNotNull { i ->
            runCatching {
                val row = all.getJSONObject(i)
                val r = row.getJSONObject("record")
                VaultItem(row.getString("id"), r.getString("label"), r.getString("provider"), r.getString("suffix"))
            }.getOrNull()
        }
    }

    suspend fun readSecret(id: String): String? = withContext(Dispatchers.IO) {
        val all = JSONArray(prefs.getString("records", "[]"))
        for (i in 0 until all.length()) {
            val row = all.getJSONObject(i)
            if (row.getString("id") == id) {
                val r = row.getJSONObject("record")
                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, Base64.decode(r.getString("iv"), Base64.NO_WRAP)))
                return@withContext cipher.doFinal(Base64.decode(r.getString("data"), Base64.NO_WRAP)).toString(Charsets.UTF_8)
            }
        }
        null
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        val all = JSONArray(prefs.getString("records", "[]"))
        val kept = JSONArray()
        for (i in 0 until all.length()) {
            val row = all.getJSONObject(i)
            if (row.getString("id") != id) kept.put(row)
        }
        prefs.edit().putString("records", kept.toString()).apply()
    }
}
