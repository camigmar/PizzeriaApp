package com.example.pizzeriaapp

import com.example.pizzeriaapp.logica.DatosEjemplo
import com.example.pizzeriaapp.logica.Faltante
import com.example.pizzeriaapp.logica.calcularIngredientesNecesarios
import com.example.pizzeriaapp.logica.reabastecerInventario
import com.example.pizzeriaapp.logica.verificarStock
import com.example.pizzeriaapp.modelo.Bebidas
import com.example.pizzeriaapp.modelo.Carta
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.Pedido
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class InventarioTest {

    private val fecha = LocalDate.of(2026, 11, 15)
    private val maria = Cliente("María Soto", "987654321", "maria.soto@correo.cl", "Los Aromos 123")

    @Test
    fun ingredientesParaUnLote() {
        val receta = mapOf("masa" to 1, "queso" to 2)
        assertEquals(mapOf("masa" to 3, "queso" to 6), calcularIngredientesNecesarios(receta, 3))
    }

    @Test
    fun stockSuficienteConfirmaYDescuenta() {
        val inventario = DatosEjemplo.inventarioInicial()
        val pedido = Pedido(maria, listOf(Carta.pepperoni, Carta.pepperoni), fecha = fecha)

        val resultado = verificarStock(pedido, inventario)

        assertTrue(resultado.confirmado)
        assertTrue(resultado.faltantes.isEmpty())
        assertEquals(4, inventario["pepperoni"])   // 10 - 2 x 3
        assertEquals(16, inventario["queso"])      // 20 - 2 x 2
        assertEquals(18, inventario["masa"])
    }

    @Test
    fun stockInsuficienteRechazaSinDescontar() {
        val inventario = DatosEjemplo.inventarioInicial()
        val pedido = Pedido(maria, List(4) { Carta.pepperoni }, fecha = fecha)

        val resultado = verificarStock(pedido, inventario)

        assertFalse(resultado.confirmado)
        assertEquals(listOf(Faltante("pepperoni", 12, 10)), resultado.faltantes)
        assertEquals(2, resultado.faltantes.first().faltan)
        assertEquals(DatosEjemplo.inventarioInicial(), inventario)
    }

    @Test
    fun sumaIngredientesDeDistintasPizzas() {
        // Masa: 1 por pizza, sin importar cuál
        val inventario = DatosEjemplo.inventarioInicial()
        val pedido = Pedido(maria, List(10) { Carta.margarita } + List(11) { Carta.napolitana }, fecha = fecha)

        val resultado = verificarStock(pedido, inventario)

        assertFalse(resultado.confirmado)
        assertTrue(resultado.faltantes.any { it.ingrediente == "masa" && it.necesario == 21 })
    }

    @Test
    fun lasBebidasNoUsanIngredientes() {
        val inventario = DatosEjemplo.inventarioInicial()
        val pedido = Pedido(maria, List(50) { Bebidas.aguaMineral }, fecha = fecha)

        val resultado = verificarStock(pedido, inventario)

        assertTrue(resultado.confirmado)
        assertEquals(DatosEjemplo.inventarioInicial(), inventario)
    }

    @Test
    fun reabastecerSumaUnidades() {
        val inventario = DatosEjemplo.inventarioInicial()
        reabastecerInventario(inventario, "pepperoni", 15)
        assertEquals(25, inventario["pepperoni"])
    }

    @Test
    fun reabastecerConCantidadCeroONegativaFalla() {
        val inventario = DatosEjemplo.inventarioInicial()
        assertThrows(IllegalArgumentException::class.java) { reabastecerInventario(inventario, "pepperoni", -5) }
        assertThrows(IllegalArgumentException::class.java) { reabastecerInventario(inventario, "pepperoni", 0) }
        assertEquals(10, inventario["pepperoni"])
    }
}
