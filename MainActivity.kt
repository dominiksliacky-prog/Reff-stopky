package sk.futbalovestopky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RefereeStopwatch() }
    }
}

@Composable
fun RefereeStopwatch() {
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var running by remember { mutableStateOf(false) }
    var startedAt by remember { mutableLongStateOf(0L) }

    LaunchedEffect(running, startedAt) {
        while (running) {
            elapsedMs = System.currentTimeMillis() - startedAt
            delay(100L)
        }
    }

    val totalSeconds = (elapsedMs / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    val minuteText = minutes.toString()
    val secondText = seconds.toString().padStart(2, '0')

    MaterialTheme(
        colors = androidx.wear.compose.material.Colors(
            primary = Color(0xFF71F59A),
            primaryVariant = Color(0xFF42C875),
            secondary = Color.White,
            secondaryVariant = Color.LightGray,
            background = Color.Black,
            surface = Color(0xFF151515),
            error = Color.Red,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White,
            onError = Color.White
        )
    ) {
        Scaffold {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "FUTBAL",
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = minuteText,
                        fontSize = if (minutes >= 100) 44.sp else 54.sp,
                        lineHeight = 56.sp,
                        color = Color(0xFF71F59A),
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = ":$secondText",
                        fontSize = 23.sp,
                        lineHeight = 31.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }

                Text(
                    text = if (running) "BEŽÍ" else if (elapsedMs > 0L) "POZASTAVENÉ" else "PRIPRAVENÉ",
                    fontSize = 10.sp,
                    color = if (running) Color(0xFF71F59A) else Color.LightGray,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = {
                        if (running) {
                            elapsedMs = System.currentTimeMillis() - startedAt
                            running = false
                        } else {
                            startedAt = System.currentTimeMillis() - elapsedMs
                            running = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.82f).height(42.dp),
                    shape = CircleShape
                ) {
                    Text(
                        text = if (running) "PAUZA" else if (elapsedMs > 0L) "POKRAČOVAŤ" else "ŠTART",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        running = false
                        elapsedMs = 0L
                        startedAt = 0L
                    },
                    modifier = Modifier.fillMaxWidth(0.62f).height(34.dp),
                    shape = CircleShape,
                    background = androidx.wear.compose.material.ButtonDefaults.secondaryButtonColors()
                ) {
                    Text("RESET", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
