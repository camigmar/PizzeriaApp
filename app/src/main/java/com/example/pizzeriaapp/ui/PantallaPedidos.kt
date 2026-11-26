package com.example.pizzeriaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    // Se piden los pedidos una vez al abrir la pantalla
    LaunchedEffect(Unit) {
        cargarPedidos()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Pedidos del día", style = MaterialTheme.typography.headlineSmall)

        if (estado.pedidos.isEmpty()) {
            Text(text = "Cargando...", style = MaterialTheme.typography.bodyLarge)
        }

        // weight(1f) deja la lista entre el título y el botón
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Los pedidos no tienen número propio: se usa su posición en la lista
            itemsIndexed(estado.pedidos) { indice, pedido ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Pedido N° ${indice + 1}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Cliente: ${pedido.cliente.nombre}")
                        Text(text = "Total: ${formatearMonto(pedido.calcularTotal())}")
                    }
                }
            }
        }

        Button(onClick = volver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
