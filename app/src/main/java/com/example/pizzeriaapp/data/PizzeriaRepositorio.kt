package com.example.pizzeriaapp.data

import com.example.pizzeriaapp.logica.DatosEjemplo
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.Pedido
import kotlinx.coroutines.delay

// Datos generales para el resumen de la pantalla principal
data class ResumenGeneral(
    val totalClientes: Int,
    val totalPedidos: Int,
    val ultimoCliente: String
)

// Capa de datos: entrega la información de DatosEjemplo simulando una carga lenta
// (como si viniera de internet o de una base de datos)
class PizzeriaRepositorio(private val demoraMs: Long = 2000) {

    // Pedidos del día
    suspend fun cargarPedidos(): List<Pedido> {
        delay(demoraMs)
        return DatosEjemplo.crearPedidosDelDia()
    }

    // Clientes de los pedidos del día, sin repetidos (usa equals de Cliente)
    suspend fun cargarClientes(): List<Cliente> {
        delay(demoraMs)
        return clientesDe(DatosEjemplo.crearPedidosDelDia())
    }

    // Totales y nombre del último cliente, o "Sin clientes" si no hay
    suspend fun cargarResumen(): ResumenGeneral {
        delay(demoraMs)
        val pedidos = DatosEjemplo.crearPedidosDelDia()
        val clientes = clientesDe(pedidos)
        return ResumenGeneral(
            totalClientes = clientes.size,
            totalPedidos = pedidos.size,
            ultimoCliente = clientes.lastOrNull()?.nombre ?: "Sin clientes"
        )
    }

    // Clientes en el orden de sus pedidos, sin repetidos
    private fun clientesDe(pedidos: List<Pedido>): List<Cliente> {
        return pedidos.map { it.cliente }.distinct()
    }
}
