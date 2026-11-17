package com.example.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzeriaapp.logica.DatosEjemplo
import com.example.pizzeriaapp.logica.asignarRepartidor
import com.example.pizzeriaapp.logica.generarResumen
import com.example.pizzeriaapp.ui.theme.PizzeriaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzeriaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaPrincipal(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Saludo y, debajo, el resumen del pedido de ejemplo
@Composable
fun PantallaPrincipal(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Greeting()
        Spacer(modifier = Modifier.height(16.dp))
        ResumenPedido(textoResumenEjemplo())
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Bienvenidos a la mejor pizzería de la ciudad",
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
}

// Monoespaciada para que las columnas del resumen queden alineadas
@Composable
fun ResumenPedido(resumen: String, modifier: Modifier = Modifier) {
    Text(
        text = resumen,
        fontFamily = FontFamily.Monospace,
        fontSize = 11.sp,
        modifier = modifier
    )
}

// Arma el resumen con el pedido y los repartidores de ejemplo
fun textoResumenEjemplo(): String {
    val pedido = DatosEjemplo.crearPedidoEjemplo()
    val repartidor = asignarRepartidor(DatosEjemplo.repartidores)?.nombre ?: "Sin repartidores disponibles"
    return generarResumen(1001, pedido, pedido.estado.texto, repartidor)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PizzeriaAppTheme {
        PantallaPrincipal()
    }
}
