package br.dev.patrocinio.controle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.dev.patrocinio.controle.ui.theme.ControleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import android.view.MotionEvent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf


@OptIn(ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {

    private val client = OkHttpClient()
    private val robotIp = "http://192.168.4.1"  // Defina o IP do ESP8266 (alterar se necessário)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF006400))
                            .padding(innerPadding)
                    ) {
                        // Exibir o IP do robô no canto superior esquerdo sem o "http://"
                        RobotIpDisplay(
                            ip = robotIp.removePrefix("http://"),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        )

                        // Layout existente
                        CenteredImage(modifier = Modifier.align(Alignment.Center))
                        ControlButtonsLeft(
                            buttonSize = 120.dp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        ControlButtonsRight(
                            buttonSize = 120.dp,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        }
    }

    // Função para exibir o IP do robô
    @Composable
    fun RobotIpDisplay(ip: String, modifier: Modifier = Modifier) {
        Text(
            text = ip,
            modifier = modifier,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    // Função para os botões de controle à esquerda
    @Composable
    fun ControlButtonsLeft(buttonSize: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = modifier.padding(start = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botão "Pra Cima"
            ArrowButton(
                icon = Icons.Filled.ArrowUpward,
                contentDescription = "Up",
                size = buttonSize,
                onPress = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("F")  // Enviar comando 'F' para mover o robô para frente
                    }
                },
                onRelease = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("S")  // Enviar comando 'S' para parar o robô quando soltar
                    }
                }
            )
            // Botão "Pra Baixo"
            ArrowButton(
                icon = Icons.Filled.ArrowDownward,
                contentDescription = "Down",
                size = buttonSize,
                onPress = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("B")  // Enviar comando 'B' para mover o robô para trás
                    }
                },
                onRelease = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("S")  // Enviar comando 'S' para parar o robô quando soltar
                    }
                }
            )
        }
    }

    // Função para os botões de controle à direita
    @Composable
    fun ControlButtonsRight(buttonSize: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = modifier.padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botão "Esquerda"
            ArrowButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Left",
                size = buttonSize,
                onPress = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("L")  // Enviar comando 'L' para mover o robô para a esquerda
                    }
                },
                onRelease = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("S")  // Enviar comando 'S' para parar o robô quando soltar
                    }
                }
            )
            // Botão "Direita"
            ArrowButton(
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Right",
                size = buttonSize,
                onPress = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("R")  // Enviar comando 'R' para mover o robô para a direita
                    }
                },
                onRelease = {
                    coroutineScope.launch(Dispatchers.IO) {
                        sendCommandToRobot("S")  // Enviar comando 'S' para parar o robô quando soltar
                    }
                }
            )
        }
    }

    @Composable
    fun ArrowButton(
        icon: ImageVector,
        contentDescription: String,
        size: androidx.compose.ui.unit.Dp,
        onPress: () -> Unit,
        onRelease: () -> Unit
    ) {
        var isPressed by remember { mutableStateOf(false) }
        val buttonColor by animateColorAsState(
            targetValue = if(isPressed) Color.LightGray else Color.Gray
        )

        Button(
            onClick = { /* Vazio porque o onClick é tratado pelos eventos de toque abaixo */ },
            modifier = Modifier
                .size(width = 200.dp, height = size)
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            onPress()  // Movimento contínuo enquanto pressionado
                            true
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            onRelease()  // Para o movimento quando soltar
                            true
                        }
                        else -> false
                    }
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    // Função para a imagem centralizada
    @Composable
    fun CenteredImage(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.pngwing),  // Verifique se a imagem está correta no diretório res/drawable
            contentDescription = "Example Image",
            modifier = modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
    }

    // Função para enviar comandos HTTP ao robô
    private fun sendCommandToRobot(command: String) {
        val url = "$robotIp/?State=$command"
        val request = Request.Builder().url(url).build()

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Erro inesperado: $response")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ControlButtonsPreview() {
        ControleTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF006400))
            ) {
                // Exibir o IP do robô no preview sem o "http://"
                RobotIpDisplay(
                    ip = robotIp.removePrefix("http://"),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )

                CenteredImage(modifier = Modifier.align(Alignment.Center))
                ControlButtonsLeft(
                    buttonSize = 120.dp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                ControlButtonsRight(
                    buttonSize = 120.dp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}
