package br.dev.patrocinio.controle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.tooling.preview.Preview
import br.dev.patrocinio.controle.ui.theme.ControleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF006400))
                            .padding(innerPadding)
                    ) {
                        // Adiciona a imagem centralizada
                        CenteredImage(modifier = Modifier.align(Alignment.Center))
                        // Adiciona os botões na lateral esquerda
                        ControlButtonsLeft(
                            buttonSize = 120.dp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        // Adiciona os botões de direção na lateral direita
                        ControlButtonsRight(
                            buttonSize = 120.dp,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        }
    }
}

// Função para os botões de controle à esquerda
@Composable
fun ControlButtonsLeft(buttonSize: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(start = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os botões
    ) {
        ArrowButton(
            icon = Icons.Filled.ArrowUpward,
            contentDescription = "Up",
            size = buttonSize,
            onClick = { /* Ação para cima */ }
        )
        ArrowButton(
            icon = Icons.Filled.ArrowDownward,
            contentDescription = "Down",
            size = buttonSize,
            onClick = { /* Ação para baixo */ }
        )
    }
}

// Função para os botões de controle à direita
@Composable
fun ControlButtonsRight(buttonSize: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(end = 16.dp), // Espaçamento do lado direito da tela
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os botões
    ) {
        ArrowButton(
            icon = Icons.Filled.ArrowBack,
            contentDescription = "Left",
            size = buttonSize,
            onClick = { /* Ação para esquerda */ }
        )
        ArrowButton(
            icon = Icons.Filled.ArrowForward,
            contentDescription = "Right",
            size = buttonSize,
            onClick = { /* Ação para direita */ }
        )
    }
}

// Função para o botão com seta
@Composable
fun ArrowButton(
    icon: ImageVector,
    contentDescription: String,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(width = 200.dp, height = size) // Tamanho retangular
            .background(Color.Gray), // Cor do botão
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray, // Cor do botão
            contentColor = Color.White
        )
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp) // Tamanho da seta
        )
    }
}

// Função para a imagem centralizada
@Composable
fun CenteredImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.pngwing), // Substitua pelo nome correto da imagem
        contentDescription = "Example Image",
        modifier = modifier.size(200.dp), // Define o tamanho da imagem
        contentScale = ContentScale.Crop // Ajusta a imagem ao contêiner
    )
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
