package com.apivault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.apivault.data.VaultStore
import com.apivault.data.VaultItem
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = VaultStore(this)
        setContent { ApiVaultApp(store) }
    }
}

private val destinations = listOf(
    "الرئيسية", "المفاتيح", "المزودون", "النماذج", "المحادثات",
    "مختبر API", "المقارنة", "الاستخدام والتكلفة", "التوجيه الذكي",
    "التشخيص", "قوالب الأوامر", "النسخ الاحتياطي", "الأمان", "الإعدادات"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiVaultApp(store: VaultStore) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route ?: "الرئيسية"
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF62D6B5),
            secondary = androidx.compose.ui.graphics.Color(0xFF8BA9FF),
            background = androidx.compose.ui.graphics.Color(0xFF101827),
            surface = androidx.compose.ui.graphics.Color(0xFF182235)
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Column {
                        Text("API VAULT", fontWeight = FontWeight.Bold)
                        Text(current, style = MaterialTheme.typography.labelSmall)
                    } },
                    navigationIcon = {
                        IconButton(onClick = { if (!nav.popBackStack()) nav.navigate("الرئيسية") }) {
                            Icon(Icons.Default.Menu, contentDescription = "القائمة")
                        }
                    },
                    actions = { IconButton(onClick = { nav.navigate("الأمان") }) {
                        Icon(Icons.Default.Shield, contentDescription = "الأمان")
                    } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf("الرئيسية", "المفاتيح", "المحادثات", "الإعدادات").forEach { item ->
                        NavigationBarItem(
                            selected = current == item,
                            onClick = { nav.navigate(item) { launchSingleTop = true } },
                            icon = { Icon(
                                when(item) {
                                    "الرئيسية" -> Icons.Default.Dashboard
                                    "المفاتيح" -> Icons.Default.Key
                                    "المحادثات" -> Icons.Default.Chat
                                    else -> Icons.Default.Settings
                                }, contentDescription = item
                            ) },
                            label = { Text(item) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(navController = nav, startDestination = "الرئيسية", modifier = Modifier.padding(padding)) {
                destinations.forEach { route ->
                    composable(route) {
                        when(route) {
                            "الرئيسية" -> DashboardScreen(onOpen = { nav.navigate(it) })
                            "المفاتيح" -> KeysScreen(store)
                            "المزودون" -> InfoScreen(route, "إدارة مزودي الخدمة وإعدادات الاتصال. أضف المزود المخصص من إعدادات التكامل.")
                            "النماذج" -> InfoScreen(route, "كتالوج النماذج: أضف Model ID يدويًا وحدد القدرات والأسعار ومصدرها.")
                            "المحادثات" -> InfoScreen(route, "واجهة المحادثات الموحدة قيد استكمال موصلات المزودين.")
                            "مختبر API" -> InfoScreen(route, "أداة اختبار الطلبات ستستخدم إعدادات المزود والمفتاح والنموذج.")
                            "المقارنة" -> InfoScreen(route, "مقارنة متوازية للنماذج مع تقدير التكلفة قبل الإرسال.")
                            "الاستخدام والتكلفة" -> InfoScreen(route, "تمييز الاستخدام الفعلي المعاد من المزود عن التقدير المحلي.")
                            "التوجيه الذكي" -> InfoScreen(route, "قواعد أولوية وFallback وحدود ميزانية قابلة للضبط.")
                            "التشخيص" -> InfoScreen(route, "حالة الاتصال والمصادقة ومعدلات الفشل وزمن الاستجابة.")
                            "قوالب الأوامر" -> InfoScreen(route, "إدارة قوالب System Prompt والمفضلة والمجموعات.")
                            "النسخ الاحتياطي" -> InfoScreen(route, "نسخ مشفر واستعادة؛ لا تضع المفاتيح في ملفات تصدير عادية.")
                            "الأمان" -> InfoScreen(route, "الأسرار تحفظ مشفرة محليًا. استخدم قفل الجهاز ولا تشارك مفاتيح API.")
                            else -> InfoScreen(route, "إعدادات اللغة والخصوصية والميزانيات وسلوك التطبيق.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardScreen(onOpen: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card {
                Column(Modifier.padding(18.dp)) {
                    Text("مركز إدارة واجهات الذكاء الاصطناعي", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("إدارة محلية للمفاتيح والمزودين والنماذج والاستخدام.")
                }
            }
        }
        items(destinations.drop(1)) { item ->
            Card(onClick = { onOpen(item) }) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(item, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronLeft, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun KeysScreen(store: VaultStore) {
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf(store.readAll()) }
    var label by remember { mutableStateOf("") }
    var provider by remember { mutableStateOf("OpenAI-compatible") }
    var secret by remember { mutableStateOf("") }
    var showSecret by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("إضافة مفتاح API", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        OutlinedTextField(label, { label = it }, label = { Text("اسم المفتاح") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(provider, { provider = it }, label = { Text("اسم المزود") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            secret, { secret = it }, label = { Text("قيمة المفتاح") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showSecret) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
            trailingIcon = { TextButton(onClick = { showSecret = !showSecret }) { Text(if(showSecret) "إخفاء" else "إظهار") } }
        )
        Button(
            enabled = label.isNotBlank() && secret.isNotBlank(),
            onClick = {
                scope.launch {
                    store.add(label.trim(), provider.trim(), secret)
                    secret = ""; label = ""
                    items = store.readAll()
                }
            }, modifier = Modifier.fillMaxWidth()
        ) { Text("حفظ المفتاح مشفرًا") }
        Text("المفاتيح المحفوظة (${items.size})", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                Card {
                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.label, fontWeight = FontWeight.Bold)
                            Text(item.provider, style = MaterialTheme.typography.bodySmall)
                            Text("••••••••  ${item.maskedSuffix}", style = MaterialTheme.typography.labelSmall)
                        }
                        TextButton(onClick = {
                            scope.launch { store.delete(item.id); items = store.readAll() }
                        }) { Text("حذف") }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoScreen(title: String, description: String) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(description)
        Card {
            Text(
                "تنبيه: ظهور هذا القسم لا يعني أن التكامل الخارجي مكتمل. راجع IMPLEMENTATION_STATUS.md لمعرفة حالة التنفيذ بدقة.",
                Modifier.padding(16.dp), color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
