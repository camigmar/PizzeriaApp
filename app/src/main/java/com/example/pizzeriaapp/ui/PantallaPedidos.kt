package com.example.pizzeriaapp.ui

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pizzeriaapp.util.formatearMonto

// Lista de los pedidos del día con su número y total
@Composable
fun PantallaPedidos(
    estado: EstadoPizzeria,
    cargarPedidos: () -> Unit,
    volver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Parte oculta y al abrir la pantalla pasa a visible: la lista aparece con Fade In
    val aparicion = remember { MutableTransitionState(false) }

    // Al abrir la pantalla: se muestra la lista y se piden los pedidos
    LaunchedEffect(Unit) {
        aparicion.targetState = true
        cargarPedidos()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Espacios.pantalla),
        verticalArrangement = Arrangement.spacedBy(Espacios.entreBloques)
    ) {
        TituloPantalla("Pedidos del día")

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
                    // Los pedidos no tienen número propio: se usa su posición en la lista
                    itemsIndexed(estado.pedidos) { indice, pedido ->
                        TarjetaPizzeria(modifier = Modifier.fillMaxWidth()) {
                            // Número y cliente a la izquierda, total destacado a la derecha
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Espacios.tarjeta),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(text = "Pedido N° ${indice + 1}", style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        text = "Cliente: ${pedido.cliente.nombre}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "Total: ${formatearMonto(pedido.calcularTotal())}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // Se desvanece cuando llegan los pedidos
            // Dentro de un Box se usa la versión general de AnimatedVisibility
            // (la de ColumnScope no se puede llamar aquí)
            androidx.compose.animation.AnimatedVisibility(
                visible = estado.pedidos.isEmpty(),
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
