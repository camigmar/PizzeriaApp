package com.example.pizzeriaapp

import com.example.pizzeriaapp.logica.pedidosCompletados
import com.example.pizzeriaapp.logica.pedidosPendientes
import com.example.pizzeriaapp.modelo.Bebidas
import com.example.pizzeriaapp.modelo.Carta
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.EstadoPedido
import com.example.pizzeriaapp.modelo.Pedido
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PedidoTest {

    private val enPromocion = LocalDate.of(2026, 11, 15)
    private val fueraDePromocion = LocalDate.of(2026, 10, 15)
    private val delta = 0.001

    private val maria = Cliente("María Soto", "987654321", "maria.soto@correo.cl", "Los Aromos 123")
    private val juan = Cliente("Juan Pérez", "976543210", "juan.perez@correo.cl", "Av. Matta 456")

    // ---------- Montos ----------

    @Test
    fun subtotalSumaCadaUnidad() {
        val pedido = Pedido(maria, listOf(Carta.napolitana, Carta.napolitana, Bebidas.aguaMineral), fecha = fueraDePromocion)
        assertEquals(20170.0, pedido.calcularSubtotal(), delta)
    }

    @Test
    fun descuentoPorMasDeTresPizzas() {
        val pedido = Pedido(maria, List(4) { Carta.napolitana }, fecha = fueraDePromocion)
        assertEquals(3796.0, pedido.calcularDescuento(), delta)
    }

    @Test
    fun tresPizzasNoTienenDescuentoPorCantidad() {
        val pedido = Pedido(maria, List(3) { Carta.napolitana }, fecha = fueraDePromocion)
        assertEquals(0.0, pedido.calcularDescuento(), delta)
    }

    @Test
    fun lasBebidasNoCuentanParaElDescuentoPorCantidad() {
        val pedido = Pedido(maria, List(3) { Carta.napolitana } + Bebidas.aguaMineral, fecha = fueraDePromocion)
        assertEquals(0.0, pedido.calcularDescuento(), delta)
    }

    @Test
    fun descuentoPorCantidadMasPromocion() {
        // 4 Pepperoni en promoción: 10% por cantidad + 10% por promoción
        val pedido = Pedido(maria, List(4) { Carta.pepperoni }, fecha = enPromocion)
        assertEquals(7192.0, pedido.calcularDescuento(), delta)
    }

    @Test
    fun ivaDe19PorCientoYTotal() {
        val pedido = Pedido(maria, listOf(Carta.napolitana, Carta.napolitana), fecha = fueraDePromocion)
        assertEquals(3606.2, pedido.calcularImpuesto(), delta)
        assertEquals(22586.2, pedido.calcularTotal(), delta)
    }

    @Test
    fun ivaSeCalculaSobreElMontoConDescuento() {
        val pedido = Pedido(maria, List(4) { Carta.napolitana }, fecha = fueraDePromocion)
        assertEquals((37960.0 - 3796.0) * 0.19, pedido.calcularImpuesto(), delta)
    }

    // ---------- Operador + ----------

    @Test
    fun plusJuntaProductosYNotas() {
        val pedido1 = Pedido(maria, listOf(Carta.pepperoni), "Sin cebolla", enPromocion)
        val pedido2 = Pedido(maria, listOf(Carta.hawaiana, Bebidas.jugoNatural), "Tocar el timbre", enPromocion)

        val combinado = pedido1 + pedido2

        assertEquals(3, combinado.productos.size)
        assertEquals(enPromocion, combinado.fecha)
        assertEquals("Sin cebolla | Tocar el timbre", combinado.notas)
        assertEquals(EstadoPedido.EN_PREPARACION, combinado.estado)
    }

    @Test
    fun plusSinNotasQuedaEnNull() {
        val combinado = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion) +
            Pedido(maria, listOf(Carta.hawaiana), fecha = enPromocion)
        assertNull(combinado.notas)
    }

    @Test
    fun plusConFechaDistintaFalla() {
        val pedido1 = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        val pedido2 = Pedido(maria, listOf(Carta.hawaiana), fecha = fueraDePromocion)
        assertThrows(IllegalArgumentException::class.java) { pedido1 + pedido2 }
    }

    @Test
    fun plusConEstadoIncompatibleFalla() {
        val pedido1 = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        val pedido2 = Pedido(maria, listOf(Carta.hawaiana), fecha = enPromocion)
        pedido2.cambiarEstado(EstadoPedido.LISTO)

        assertThrows(IllegalArgumentException::class.java) { pedido1 + pedido2 }
        assertThrows(IllegalArgumentException::class.java) { pedido2 + pedido1 }
    }

    @Test
    fun plusConClienteDistintoFalla() {
        val pedido1 = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        val pedido2 = Pedido(juan, listOf(Carta.hawaiana), fecha = enPromocion)
        assertThrows(IllegalArgumentException::class.java) { pedido1 + pedido2 }
    }

    // ---------- Tope de 100 unidades ----------

    @Test
    fun pedidoDe100UnidadesEsValido() {
        val pedido = Pedido(maria, List(60) { Carta.pepperoni } + List(40) { Bebidas.aguaMineral }, fecha = enPromocion)
        assertEquals(100, pedido.productos.size)
    }

    @Test
    fun pedidoDe101UnidadesFalla() {
        assertThrows(IllegalArgumentException::class.java) {
            Pedido(maria, List(60) { Carta.pepperoni } + List(41) { Bebidas.aguaMineral }, fecha = enPromocion)
        }
    }

    @Test
    fun plusQueSuperaElTopeFalla() {
        // Cada uno es válido por separado, pero juntos pasan de 100
        val pedido1 = Pedido(maria, List(60) { Carta.pepperoni }, fecha = enPromocion)
        val pedido2 = Pedido(maria, List(50) { Carta.hawaiana }, fecha = enPromocion)
        assertThrows(IllegalArgumentException::class.java) { pedido1 + pedido2 }
    }

    // ---------- Estados ----------

    @Test
    fun transicionesHastaEntregado() {
        val pedido = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.estado)

        pedido.cambiarEstado(EstadoPedido.LISTO)
        assertEquals(EstadoPedido.LISTO, pedido.estado)

        pedido.cambiarEstado(EstadoPedido.EN_CAMINO)
        assertEquals(EstadoPedido.EN_CAMINO, pedido.estado)

        pedido.cambiarEstado(EstadoPedido.ENTREGADO)
        assertEquals(EstadoPedido.ENTREGADO, pedido.estado)
    }

    @Test
    fun noSePuedeSaltarUnEstado() {
        val pedido = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        assertThrows(IllegalStateException::class.java) { pedido.cambiarEstado(EstadoPedido.EN_CAMINO) }
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.estado)
    }

    @Test
    fun entregadoEsElEstadoFinal() {
        val pedido = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        pedido.cambiarEstado(EstadoPedido.LISTO)
        pedido.cambiarEstado(EstadoPedido.EN_CAMINO)
        pedido.cambiarEstado(EstadoPedido.ENTREGADO)
        assertThrows(IllegalStateException::class.java) { pedido.cambiarEstado(EstadoPedido.LISTO) }
    }

    @Test
    fun soloEntregadoCuentaComoCompletado() {
        assertFalse(EstadoPedido.EN_PREPARACION.esCompletado)
        assertFalse(EstadoPedido.LISTO.esCompletado)
        assertFalse(EstadoPedido.EN_CAMINO.esCompletado)
        assertTrue(EstadoPedido.ENTREGADO.esCompletado)
    }

    @Test
    fun listoYEnCaminoQuedanPendientes() {
        val listo = Pedido(maria, listOf(Carta.pepperoni), fecha = enPromocion)
        listo.cambiarEstado(EstadoPedido.LISTO)

        val enCamino = Pedido(juan, listOf(Carta.hawaiana), fecha = enPromocion)
        enCamino.cambiarEstado(EstadoPedido.LISTO)
        enCamino.cambiarEstado(EstadoPedido.EN_CAMINO)

        val entregado = Pedido(juan, listOf(Carta.margarita), fecha = enPromocion)
        entregado.cambiarEstado(EstadoPedido.LISTO)
        entregado.cambiarEstado(EstadoPedido.EN_CAMINO)
        entregado.cambiarEstado(EstadoPedido.ENTREGADO)

        val pedidos = listOf(listo, enCamino, entregado)
        assertEquals(listOf(entregado), pedidosCompletados(pedidos))
        assertEquals(listOf(listo, enCamino), pedidosPendientes(pedidos))
    }

    // ---------- Desestructuración ----------

    @Test
    fun desestructuracionDelPedido() {
        val pedido = Pedido(maria, listOf(Carta.napolitana, Carta.napolitana), fecha = fueraDePromocion)
        val (cliente, productos, total) = pedido
        assertEquals(maria, cliente)
        assertEquals(2, productos.size)
        assertEquals(22586.2, total, delta)
    }
}
