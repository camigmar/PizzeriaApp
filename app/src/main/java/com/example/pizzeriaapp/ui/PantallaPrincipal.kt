package com.example.pizzeriaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
                .padding(Espacios.pantalla),
            verticalArrangement = Arrangement.spacedBy(Espacios.entreBloques)
        ) {
            TituloPantalla("Resumen de la pizzería")

            // Contenedor del resumen: "Cargando..." y los datos ocupan el mismo lugar,
            // así uno se desvanece mientras el otro aparece sin que la pantalla salte
            Box(modifier = Modifier.fillMaxWidth()) {
                // Dentro de un Box se usa la versión general de AnimatedVisibility
                // (la de ColumnScope no se puede llamar aquí)
                androidx.compose.animation.AnimatedVisibility(
                    visible = estado.resumen == null,
                    enter = Animaciones.entrada(),
                    exit = Animaciones.salida()
                ) {
                    TarjetaPizzeria(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Cargando...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(Espacios.tarjeta)
                        )
                    }
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
                    BloqueResumen(resumen)
                }
            }

            // Empuja los botones hacia abajo de la pantalla
            Spacer(modifier = Modifier.weight(1f))

            BotonPrincipal(texto = "Ver pedidos", alPresionar = verPedidos)
            BotonPrincipal(texto = "Ver clientes", alPresionar = verClientes)
        }
    }
}

// Los dos totales lado a lado con números grandes y, debajo, el último cliente
@Composable
private fun BloqueResumen(resumen: ResumenGeneral) {
    Column(verticalArrangement = Arrangement.spacedBy(Espacios.entreTarjetas)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacios.entreTarjetas)) {
            TarjetaTotal(etiqueta = "CLIENTES", valor = resumen.totalClientes, modifier = Modifier.weight(1f))
            TarjetaTotal(etiqueta = "PEDIDOS", valor = resumen.totalPedidos, modifier = Modifier.weight(1f))
        }

        // Zona de énfasis en café oscuro, con la misma forma que las tarjetas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.medium)
                .padding(Espacios.tarjeta + 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "ÚLTIMO CLIENTE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.75f)
            )
            Text(
                text = resumen.ultimoCliente,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

// Tarjeta con una etiqueta pequeña y un número grande en rojo tomate
@Composable
private fun TarjetaTotal(etiqueta: String, valor: Int, modifier: Modifier = Modifier) {
    TarjetaPizzeria(modifier = modifier) {
        Column(modifier = Modifier.padding(Espacios.tarjeta + 4.dp)) {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFF8F0)
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
