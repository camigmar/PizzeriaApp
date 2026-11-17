package com.example.pizzeriaapp

import com.example.pizzeriaapp.util.esCorreoValido
import com.example.pizzeriaapp.util.formatearMonto
import com.example.pizzeriaapp.util.formatearTelefono
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UtilTest {

    // ---------- Teléfono ----------

    @Test
    fun telefonosValidosQuedanConElMismoFormato() {
        assertEquals("+56 (9) 6580-1007", formatearTelefono("+56 9 6580 1007"))
        assertEquals("+56 (9) 6580-1007", formatearTelefono("965801007"))
        assertEquals("+56 (9) 6580-1007", formatearTelefono("56965801007"))
        assertEquals("+56 (9) 6580-1007", formatearTelefono("+56 (9) 6580-1007"))
    }

    @Test
    fun telefonosInvalidosDevuelvenNull() {
        assertNull(formatearTelefono("12345"))
        assertNull(formatearTelefono("+56 2 2345 6789"))  // fijo, no celular
        assertNull(formatearTelefono("9658010071"))       // un dígito de más
        assertNull(formatearTelefono(""))
    }

    // ---------- Correo ----------

    @Test
    fun correosValidos() {
        assertTrue(esCorreoValido("maria.soto@correo.cl"))
        assertTrue(esCorreoValido("juan+pizza@mi-dominio.com"))
        assertTrue(esCorreoValido("  carla@correo.cl  "))
    }

    @Test
    fun correosInvalidos() {
        assertFalse(esCorreoValido("maria.soto"))
        assertFalse(esCorreoValido("maria@"))
        assertFalse(esCorreoValido("maria@correo"))
        assertFalse(esCorreoValido("@correo.cl"))
        assertFalse(esCorreoValido("maria soto@correo.cl"))
    }

    // ---------- Monto ----------

    @Test
    fun montoConPuntoDeMilesYSinDecimales() {
        assertEquals("$26.970", formatearMonto(26970.0))
        assertEquals("$8.990", formatearMonto(8990.4))
        assertEquals("$0", formatearMonto(0.0))
    }
}
