package com.example.pizzeriaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pizzeriaapp.data.ResumenGeneral
import com.example.pizzeriaapp.ui.theme.PizzeriaAppTheme

// Resumen general de la pizzería con accesos a pedidos y clientes
@Composable
fun PantallaPrincipal(
    estado: EstadoPizzeria,
    cargarResumen: () -> Unit,
    verPedidos: () -> Unit,
    verClientes: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Parte oculto (false) y al abrir la pantalla pasa a visible (true): eso dispara el Fade In
    val aparicion = remember { MutableTransitionState(false) }

    // Al abrir la pantalla: se muestra el contenido y se pide el resumen
    LaunchedEffect(Unit) {
        aparicion.targetState = true
        cargarResumen()
    }

    AnimatedVisibility(
        visibleState = aparicion,
        enter = Animaciones.entrada(),
        exit = Animaciones.salida(),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Resumen de la pizzería", style = MaterialTheme.typography.headlineSmall)

            // Contenedor del resumen: "Cargando..." y los datos ocupan el mismo lugar,
            // así uno se desvanece mientras el otro aparece sin que la pantalla salte
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(16.dp)) {
                    // Dentro de un Box se usa la versión general de AnimatedVisibility
                    // (la de ColumnScope no se puede llamar aquí)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = estado.resumen == null,
                        enter = Animaciones.entrada(),
                        exit = Animaciones.salida()
                    ) {
                        Text(text = "Cargando...", style = MaterialTheme.typography.bodyLarge)
                    }

                    // Dentro de un Box se usa la versión general de AnimatedVisibility
                    // (la de ColumnScope no se puede llamar aquí)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = estado.resumen != null,
                        enter = Animaciones.entrada(),
                        exit = Animaciones.salida()
                    ) {
                        // Se revisa de nuevo por si el resumen desaparece durante el Fade Out
                        val resumen = estado.resumen ?: return@AnimatedVisibility
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "Total de clientes: ${resumen.totalClientes}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Total de pedidos: ${resumen.totalPedidos}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Último cliente: ${resumen.ultimoCliente}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Button(onClick = verPedidos, modifier = Modifier.fillMaxWidth()) {
                Text("Ver pedidos")
            }
            Button(onClick = verClientes, modifier = Modifier.fillMaxWidth()) {
                Text("Ver clientes")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaPrincipalPreview() {
    PizzeriaAppTheme {
        PantallaPrincipal(
            estado = EstadoPizzeria(resumen = ResumenGeneral(5, 5, "Valentina Díaz")),
            cargarResumen = {},
            verPedidos = {},
            verClientes = {}
        )
    }
}
