package com.example.pizzeriaapp.ui

import androidx.compose.foundation.layout.Arrangement
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
    // Se piden los clientes una vez al abrir la pantalla
    LaunchedEffect(Unit) {
        cargarClientes()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Clientes", style = MaterialTheme.typography.headlineSmall)

        if (estado.clientes.isEmpty()) {
            Text(text = "Cargando...", style = MaterialTheme.typography.bodyLarge)
        }

        // weight(1f) deja la lista entre el título y el botón
        LazyColumn(
            modifier = Modifier.weight(1f),
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

        Button(onClick = volver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
