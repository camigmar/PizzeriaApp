package com.example.pizzeriaapp.ui

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
            .padding(Espacios.pantalla),
        verticalArrangement = Arrangement.spacedBy(Espacios.entreBloques)
    ) {
        TituloPantalla("Clientes")

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
                    verticalArrangement = Arrangement.spacedBy(Espacios.entreTarjetas)
                ) {
                    items(estado.clientes) { cliente ->
                        TarjetaPizzeria(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Espacios.tarjeta),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Espacios.tarjeta)
                            ) {
                                // Círculo con la inicial del cliente
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cliente.nombre.take(1).uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(text = cliente.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        text = cliente.correo,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = cliente.direccion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
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
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        BotonContorno(texto = "Volver", alPresionar = volver)
    }
}
