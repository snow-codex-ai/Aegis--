package com.aegis.mobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aegis.mobile.voice.AegisVoiceService

data class Chat(val id: Int, val title: String, val messages: MutableList<Pair<Boolean,String>>)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AegisApp() }
    }

    @Composable
    private fun AegisApp() {
        var tab by remember { mutableStateOf("Home") }
        var assistantName by remember { mutableStateOf("AEGIS") }
        var prompt by remember { mutableStateOf("") }
        var chats by remember { mutableStateOf(listOf(
            Chat(1, "Main", mutableListOf(false to "Systems online. Tell me what you want to accomplish."))
        )) }
        var activeChat by remember { mutableStateOf(1) }

        MaterialTheme(colorScheme = darkColorScheme()) {
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color(0xFF040611), Color(0xFF10162D), Color(0xFF050711)))
                )
            ) {
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(assistantName, style = MaterialTheme.typography.headlineMedium)
                            Text("PERSONAL AI COMMAND CENTER", color = Color(0xFF9FA8C7))
                        }
                        Text("● ONLINE", color = Color(0xFF72F5B6))
                    }
                    Spacer(Modifier.height(14.dp))

                    when(tab) {
                        "Home" -> HomeScreen(
                            prompt, { prompt = it },
                            onRun = {
                                if (prompt.isNotBlank()) {
                                    chats = chats.map {
                                        if (it.id == activeChat) {
                                            it.copy(messages = (it.messages + (true to prompt) +
                                                (false to localReply(prompt))).toMutableList())
                                        } else it
                                    }
                                    prompt = ""
                                }
                            },
                            onVoice = {
                                startForegroundService(Intent(this@MainActivity, AegisVoiceService::class.java))
                            }
                        )
                        "Chats" -> ChatsScreen(chats, activeChat, { activeChat = it })
                        "Markets" -> MarketsScreen()
                        "Tools" -> ToolsScreen()
                        "Settings" -> SettingsScreen(assistantName) { assistantName = it }
                    }

                    Spacer(Modifier.weight(1f))
                    NavigationBar(containerColor = Color.White.copy(alpha=.06f)) {
                        listOf("Home","Chats","Markets","Tools","Settings").forEach {
                            NavigationBarItem(
                                selected = tab == it, onClick = { tab = it },
                                icon = { Text(when(it) {
                                    "Home" -> "⌂"; "Chats" -> "◫"; "Markets" -> "⌁"
                                    "Tools" -> "◇"; else -> "⚙"
                                })},
                                label = { Text(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun localReply(input: String): String {
        val q = input.lowercase()
        return when {
            "hello" in q || "hi" in q -> "Hello. I am ready."
            "math" in q || "physics" in q -> "STEM mode selected. In the full local engine, this routes the problem to the symbolic/numerical solver."
            "code" in q || "python" in q -> "Coding mode selected. I can route this to the local coding engine."
            "trade" in q || "market" in q -> "Market analysis mode selected. Use verified, timestamped market data and treat signals as research rather than guaranteed predictions."
            "pdf" in q || "file" in q -> "File workspace selected. Android's file picker can provide access to files you explicitly choose."
            else -> "Command received. The V1 interface is ready; connect a local or hosted model to turn this into full natural-language tool execution."
        }
    }

    @Composable private fun HomeScreen(prompt:String,onPrompt:(String)->Unit,onRun:()->Unit,onVoice:()->Unit) {
        Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
            GlassCard {
                Text("AI CORE", style=MaterialTheme.typography.titleLarge)
                Text("Plan • Execute • Verify • Remember", color=Color.Gray)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(prompt,onPrompt,Modifier.fillMaxWidth(),
                    placeholder={Text("Ask AEGIS anything…")})
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    Button(onClick=onRun){Text("RUN")}
                    OutlinedButton(onClick=onVoice){Text("🎙 VOICE")}
                }
            }
            Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                Stat("MEMORY","LOCAL")
                Stat("TOOLS","12+")
            }
            Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                Stat("MODE","FREE")
                Stat("TRADING","RESEARCH")
            }
        }
    }

    @Composable private fun ChatsScreen(chats:List<Chat>,active:Int,onSelect:(Int)->Unit) {
        LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) {
            item { Text("MULTI-CHAT INBOX", style=MaterialTheme.typography.headlineSmall) }
            items(chats) { chat ->
                GlassCard {
                    Text(chat.title, style=MaterialTheme.typography.titleMedium)
                    Text("${chat.messages.size} messages", color=Color.Gray)
                    Button(onClick={onSelect(chat.id)}) { Text(if(chat.id==active)"ACTIVE" else "OPEN") }
                }
            }
            item {
                GlassCard {
                    Text("ACTIVE CONVERSATION")
                    val chat=chats.firstOrNull{it.id==active}
                    chat?.messages?.forEach { (user,msg) ->
                        Text(if(user)"YOU: $msg" else "AEGIS: $msg", modifier=Modifier.padding(vertical=5.dp))
                    }
                }
            }
        }
    }

    @Composable private fun MarketsScreen() {
        Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text("MARKET INTELLIGENCE", style=MaterialTheme.typography.headlineSmall)
            GlassCard {
                Text("Portfolio intelligence")
                Text("Connect a free/authorized market-data source to populate live values.")
                Text("Signals should show evidence, timestamp, uncertainty, risk and scenarios.")
            }
            GlassCard {
                Text("ANALYSIS PIPELINE")
                Text("Technical • Fundamental • News • Sentiment • Risk • Backtest")
            }
        }
    }

    @Composable private fun ToolsScreen() {
        Column(verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Text("TOOL MATRIX", style=MaterialTheme.typography.headlineSmall)
            listOf(
                "🧮 Math & Physics","💻 Coding","🌐 Web Research",
                "📁 Local Files","📄 PDF / Documents","🖼 Image Tools",
                "🎵 Audio","🎬 Video","📱 Android Actions","📈 Trading Research"
            ).forEach { GlassCard { Text(it, style=MaterialTheme.typography.titleMedium) } }
        }
    }

    @Composable private fun SettingsScreen(name:String,onName:(String)->Unit) {
        var value by remember(name){ mutableStateOf(name) }
        Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text("AEGIS SETTINGS", style=MaterialTheme.typography.headlineSmall)
            GlassCard {
                Text("Assistant identity")
                OutlinedTextField(value,{value=it},Modifier.fillMaxWidth(),label={Text("Assistant name")})
                Button(onClick={onName(value)}){Text("SAVE")}
            }
            GlassCard {
                Text("Voice & privacy")
                Text("Voice mode runs through an Android foreground service and requires permission.")
                Text("Memory should remain user-controlled and local by default.")
            }
        }
    }

    @Composable private fun GlassCard(content:@Composable ColumnScope.()->Unit) {
        Card(
            shape=RoundedCornerShape(24.dp),
            colors=CardDefaults.cardColors(containerColor=Color.White.copy(alpha=.075f)),
            modifier=Modifier.fillMaxWidth()
        ){ Column(Modifier.padding(16.dp),content=content) }
    }

    @Composable private fun RowScope.Stat(title:String,value:String) {
        Card(Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=Color.White.copy(alpha=.06f)),
            shape=RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(14.dp)){Text(title,color=Color.Gray);Text(value,style=MaterialTheme.typography.titleLarge)}
        }
    }
}
