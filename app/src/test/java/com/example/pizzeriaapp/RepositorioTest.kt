package com.example.pizzeriaapp

import com.example.pizzeriaapp.data.PizzeriaRepositorio
import com.example.pizzeriaapp.logica.DatosEjemplo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

// runTest salta el delay(2000), así la prueba no espera de verdad
class RepositorioTest {

    private val repositorio = PizzeriaRepositorio()

    @Test
    fun resumenCoincideConDatosEjemplo() = runTest {
        val pedidos = DatosEjemplo.crearPedidosDelDia()
        val clientes = pedidos.map { it.cliente }.distinct()

        val resumen = repositorio.cargarResumen()

        assertEquals(clientes.size, resumen.totalClientes)
        assertEquals(pedidos.size, resumen.totalPedidos)
        assertEquals(clientes.last().nombre, resumen.ultimoCliente)
        assertEquals("Valentina Díaz", resumen.ultimoCliente)
    }

    @Test
    fun cargaPedidosYClientesDelDia() = runTest {
        assertEquals(5, repositorio.cargarPedidos().size)
        assertEquals(5, repositorio.cargarClientes().size)
    }
}
