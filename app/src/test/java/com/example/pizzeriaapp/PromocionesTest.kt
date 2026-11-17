package com.example.pizzeriaapp

import com.example.pizzeriaapp.logica.descuentoPromocion
import com.example.pizzeriaapp.logica.esPromocionable
import com.example.pizzeriaapp.logica.productosPromocionables
import com.example.pizzeriaapp.modelo.Bebidas
import com.example.pizzeriaapp.modelo.Carta
import com.example.pizzeriaapp.modelo.Categoria
import com.example.pizzeriaapp.modelo.Producto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PromocionesTest {

    private val enPromocion = LocalDate.of(2026, 11, 15)
    private val fueraDePromocion = LocalDate.of(2026, 10, 15)
    private val delta = 0.001

    @Test
    fun soloLosProductosAnotadosSonPromocionables() {
        assertTrue(esPromocionable(Carta.pepperoni))
        assertTrue(esPromocionable(Carta.vegetariana))
        assertTrue(esPromocionable(Bebidas.jugoNatural))
        assertFalse(esPromocionable(Carta.napolitana))
        assertFalse(esPromocionable(Bebidas.aguaMineral))
    }

    @Test
    fun productoComunConMismoNombreNoEsPromocionable() {
        // La anotación está en la clase, no en el nombre
        val pepperoniComun = Producto("Pepperoni", 8990.0, emptyMap(), Categoria.PIZZA)
        assertFalse(esPromocionable(pepperoniComun))
    }

    @Test
    fun descuentoSoloParaProductosAnotados() {
        // Pepperoni (promocionable) + Napolitana (no): 10% solo de 8990
        val productos = listOf(Carta.pepperoni, Carta.napolitana)
        assertEquals(899.0, descuentoPromocion(productos, enPromocion), delta)
    }

    @Test
    fun sinProductosAnotadosNoHayDescuento() {
        val productos = listOf(Carta.napolitana, Carta.hawaiana, Bebidas.aguaMineral)
        assertEquals(0.0, descuentoPromocion(productos, enPromocion), delta)
    }

    @Test
    fun fueraDelRangoDeFechasNoHayDescuento() {
        val productos = listOf(Carta.pepperoni, Carta.margarita)
        assertEquals(0.0, descuentoPromocion(productos, fueraDePromocion), delta)
    }

    @Test
    fun bordesDelRangoDePromocion() {
        val productos = listOf(Carta.pepperoni)
        assertEquals(899.0, descuentoPromocion(productos, LocalDate.of(2026, 11, 1)), delta)
        assertEquals(899.0, descuentoPromocion(productos, LocalDate.of(2026, 11, 30)), delta)
        assertEquals(0.0, descuentoPromocion(productos, LocalDate.of(2026, 10, 31)), delta)
        assertEquals(0.0, descuentoPromocion(productos, LocalDate.of(2026, 12, 1)), delta)
    }

    @Test
    fun cuentaCadaUnidadPromocionable() {
        // 2 Jugos naturales: 2 x 249
        val productos = listOf(Bebidas.jugoNatural, Bebidas.jugoNatural)
        assertEquals(498.0, descuentoPromocion(productos, enPromocion), delta)
    }

    @Test
    fun listaDePromocionablesPorFuente() {
        assertEquals(
            setOf("Pepperoni", "Vegetariana", "Margarita"),
            productosPromocionables(Carta).map { it.nombre }.toSet()
        )
        assertEquals(
            setOf("Bebida 500 ml", "Jugo natural"),
            productosPromocionables(Bebidas).map { it.nombre }.toSet()
        )
    }
}
