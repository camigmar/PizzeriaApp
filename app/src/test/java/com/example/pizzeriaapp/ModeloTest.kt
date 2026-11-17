package com.example.pizzeriaapp

import com.example.pizzeriaapp.logica.asignarRepartidor
import com.example.pizzeriaapp.logica.registrarCliente
import com.example.pizzeriaapp.logica.registrarProducto
import com.example.pizzeriaapp.modelo.Carta
import com.example.pizzeriaapp.modelo.Categoria
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.Producto
import com.example.pizzeriaapp.modelo.Repartidor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ModeloTest {

    private val maria = Cliente("María Soto", "987654321", "maria.soto@correo.cl", "Los Aromos 123")

    // ---------- Cliente ----------

    @Test
    fun clientesIgualesConCorreoEnMayusculasYOtraDireccion() {
        val copia = Cliente("María Soto", "912345678", "MARIA.SOTO@correo.cl", "Otra Calle 999")
        assertEquals(maria, copia)
        assertEquals(maria.hashCode(), copia.hashCode())
    }

    @Test
    fun clientesConDistintoNombreNoSonIguales() {
        val otra = Cliente("María Soto Pérez", "987654321", "maria.soto@correo.cl", "Los Aromos 123")
        assertNotEquals(maria, otra)
    }

    @Test
    fun registroDeClientesNoAceptaDuplicados() {
        val registro = mutableSetOf<Cliente>()
        assertTrue(registrarCliente(registro, maria))
        assertFalse(registrarCliente(registro, Cliente("María Soto", "9", "maria.soto@correo.cl", "X")))
        assertEquals(1, registro.size)
    }

    @Test
    fun desestructuracionDelCliente() {
        val (nombre, correo, telefono) = maria
        assertEquals("María Soto", nombre)
        assertEquals("maria.soto@correo.cl", correo)
        assertEquals("987654321", telefono)
    }

    // ---------- Producto ----------

    @Test
    fun productosConMismoNombreYCategoriaSonIguales() {
        // El precio y la clase no cuentan
        val otroPrecio = Producto("Pepperoni", 9990.0, mapOf("masa" to 1), Categoria.PIZZA)
        assertEquals(Carta.pepperoni, otroPrecio)
        assertEquals(otroPrecio, Carta.pepperoni)
        assertEquals(Carta.pepperoni.hashCode(), otroPrecio.hashCode())
    }

    @Test
    fun productosConDistintaCategoriaNoSonIguales() {
        val pepperoniBebida = Producto("Pepperoni", 8990.0, emptyMap(), Categoria.BEBIDA)
        assertNotEquals(Carta.pepperoni, pepperoniBebida)
    }

    @Test
    fun registroDeProductosNoAceptaDuplicados() {
        val registro = mutableSetOf<Producto>()
        assertTrue(registrarProducto(registro, Carta.pepperoni))
        assertFalse(registrarProducto(registro, Producto("Pepperoni", 1.0, emptyMap(), Categoria.PIZZA)))
        assertEquals(1, registro.size)
    }

    @Test
    fun precioCeroONegativoFalla() {
        assertThrows(IllegalArgumentException::class.java) { Producto("Prueba", 0.0, emptyMap(), Categoria.PIZZA) }
        assertThrows(IllegalArgumentException::class.java) { Producto("Prueba", -100.0, emptyMap(), Categoria.PIZZA) }
    }

    // ---------- Repartidor ----------

    @Test
    fun seAsignaElRepartidorDisponibleMasCercano() {
        val repartidores = listOf(
            Repartidor("Ana", "911111111", 3.5, true),
            Repartidor("Pedro", "922222222", 1.2, false),
            Repartidor("Luis", "933333333", 2.0, true)
        )
        assertEquals("Luis", asignarRepartidor(repartidores)?.nombre)
    }

    @Test
    fun sinRepartidoresDisponiblesDevuelveNull() {
        val repartidores = listOf(Repartidor("Pedro", "922222222", 1.2, false))
        assertNull(asignarRepartidor(repartidores))
    }
}
