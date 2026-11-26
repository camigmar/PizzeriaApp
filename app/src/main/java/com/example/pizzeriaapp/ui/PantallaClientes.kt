package com.example.pizzeriaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Lista de los clientes del día
@Composable
fun PantallaClientes(
    estado: EstadoPizzeria,
    cargarClientes: () -> Unit,
    volver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Parte oculta y al abrir la pantalla pasa a visible: la lista aparece con Fade In
    val aparicion = remember { MutableTransitionState(false) }

    // Al abrir la pantalla: se muestra la lista y se piden los clientes
    LaunchedEffect(Unit) {
        aparicion.targetState = true
        cargarClientes()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Clientes", style = MaterialTheme.typography.headlineSmall)

        // weight(1f) deja este espacio entre el título y el botón;
        // "Cargando..." y la lista comparten el lugar para que no salte al cambiar
        Box(modifier = Modifier.weight(1f)) {
            // Dentro de un Box se usa la versión general de AnimatedVisibility
            // (la de ColumnScope no se puede llamar aquí)
            androidx.compose.animation.AnimatedVisibility(
                visibleState = aparicion,
                enter = Animaciones.entrada(),
                exit = Animaciones.salida()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estado.clientes) { cliente ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = cliente.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(text = cliente.correo)
                                Text(text = cliente.direccion)
                            }
                        }
                    }
                }
            }

            // Se desvanece cuando llegan los clientes
            // Dentro de un Box se usa la versión general de AnimatedVisibility
            // (la de ColumnScope no se puede llamar aquí)
            androidx.compose.animation.AnimatedVisibility(
                visible = estado.clientes.isEmpty(),
                enter = Animaciones.entrada(),
                exit = Animaciones.salida()
            ) {
                Text(text = "Cargando...", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Button(onClick = volver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
