package com.example.pizzeriaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    // Se pide el resumen una vez al abrir la pantalla
    LaunchedEffect(Unit) {
        cargarResumen()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Resumen de la pizzería", style = MaterialTheme.typography.headlineSmall)

        val resumen = estado.resumen
        if (resumen == null) {
            Text(text = "Cargando...", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(text = "Total de clientes: ${resumen.totalClientes}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Total de pedidos: ${resumen.totalPedidos}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Último cliente: ${resumen.ultimoCliente}", style = MaterialTheme.typography.bodyLarge)
        }

        Button(onClick = verPedidos, modifier = Modifier.fillMaxWidth()) {
            Text("Ver pedidos")
        }
        Button(onClick = verClientes, modifier = Modifier.fillMaxWidth()) {
            Text("Ver clientes")
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
