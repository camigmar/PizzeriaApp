package com.example.pizzeriaapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Piezas visuales comunes para que las tres pantallas se vean iguales

// Márgenes y espacios de todas las pantallas
object Espacios {
    val pantalla = 20.dp      // margen exterior de cada pantalla
    val entreBloques = 16.dp  // entre título, contenido y botones
    val entreTarjetas = 12.dp // entre tarjetas de una lista
    val tarjeta = 16.dp       // relleno dentro de una tarjeta
}

// Tarjeta plana: sin sombra, con borde fino y esquinas redondeadas (forma "medium" del tema)
@Composable
fun TarjetaPizzeria(modifier: Modifier = Modifier, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        content = contenido
    )
}

// Título grande de cada pantalla
@Composable
fun TituloPantalla(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

// Botón píldora con el color primario
@Composable
fun BotonPrincipal(texto: String, alPresionar: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = alPresionar,
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(texto)
    }
}

// Botón píldora con borde, para acciones secundarias como "Volver"
@Composable
fun BotonContorno(texto: String, alPresionar: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = alPresionar,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(texto)
    }
}
