package com.example.pizzeriaapp.logica

import com.example.pizzeriaapp.modelo.Bebidas
import com.example.pizzeriaapp.modelo.Carta
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.EstadoPedido
import com.example.pizzeriaapp.modelo.Pedido
import com.example.pizzeriaapp.modelo.Repartidor
import java.time.LocalDate

// Datos fijos que antes se creaban en main() (ahora no hay consola para pedirlos)
object DatosEjemplo {

    // Inventario de ingredientes (en unidades); cada llamada entrega uno nuevo
    fun inventarioInicial(): MutableMap<String, Int> = mutableMapOf(
        "masa" to 20,
        "salsa de tomate" to 20,
        "queso" to 20,
        "pepperoni" to 10,
        "tomate" to 20,
        "piña" to 10,
        "jamón" to 10,
        "verduras" to 15,
        "albahaca" to 10
    )

    // Carta que se muestra al cliente para elegir (pizzas del object Carta)
    val carta = listOf(Carta.pepperoni, Carta.napolitana, Carta.hawaiana, Carta.vegetariana, Carta.margarita)

    // Bebidas que se pueden agregar al pedido (del object Bebidas)
    val menuBebidas = listOf(Bebidas.bebida500, Bebidas.jugoNatural, Bebidas.aguaMineral)

    // Repartidores con su distancia al local
    val repartidores = listOf(
        Repartidor("Ana", "911111111", 3.5, true),
        Repartidor("Pedro", "922222222", 1.2, false),
        Repartidor("Luis", "933333333", 2.0, true),
        Repartidor("Sofía", "944444444", 4.8, true)
    )

    // Cliente de ejemplo
    val clienteMaria = Cliente("María Soto", "+56 (9) 8765-4321", "maria.soto@correo.cl", "Los Aromos 123")

    // Pedido de ejemplo: 2 Pepperoni, 1 Napolitana y 2 Jugos naturales (una entrada por unidad)
    fun crearPedidoEjemplo(fecha: LocalDate = LocalDate.now()): Pedido {
        val productos = List(2) { Carta.pepperoni } + Carta.napolitana + List(2) { Bebidas.jugoNatural }
        return Pedido(clienteMaria, productos, "Sin cebolla, por favor", fecha)
    }

    // Dos pedidos de María para combinar con +: deben tener la misma fecha y seguir en preparación
    fun crearPedidosParaCombinar(fecha: LocalDate = LocalDate.now()): Pair<Pedido, Pedido> {
        val pedido1 = Pedido(clienteMaria, listOf(Carta.pepperoni, Carta.pepperoni), fecha = fecha)
        val pedido2 = Pedido(clienteMaria, listOf(Carta.hawaiana, Carta.hawaiana), fecha = fecha)
        return pedido1 to pedido2
    }

    // Pedidos de ejemplo del día (los mismos de main); el de María tiene fecha fuera de la promoción
    fun crearPedidosDelDia(): List<Pedido> {
        val pedidos = listOf(
            Pedido(Cliente("María Soto", "987654321", "maria.soto@correo.cl", "Los Aromos 123"), listOf(Carta.pepperoni, Carta.pepperoni), fecha = LocalDate.of(2026, 10, 15)),
            Pedido(Cliente("Juan Pérez", "976543210", "juan.perez@correo.cl", "Av. Matta 456"), listOf(Carta.napolitana)),
            Pedido(Cliente("Carla Rojas", "965432109", "carla.rojas@correo.cl", "Pasaje Sur 789"), listOf(Carta.hawaiana, Carta.hawaiana, Carta.hawaiana)),
            Pedido(Cliente("Diego Muñoz", "954321098", "diego.munoz@correo.cl", "Calle Norte 321"), listOf(Carta.vegetariana, Carta.pepperoni)),
            Pedido(Cliente("Valentina Díaz", "943210987", "valentina.diaz@correo.cl", "Av. Grecia 654"), listOf(Carta.margarita, Carta.margarita, Carta.margarita, Carta.margarita))
        )

        // Todos parten "En preparación"; se actualizan los que ya avanzaron
        // El de Carla pasa primero por "Listo" y luego a "En camino"
        pedidos[1].cambiarEstado(EstadoPedido.LISTO)
        pedidos[2].cambiarEstado(EstadoPedido.LISTO)
        pedidos[2].cambiarEstado(EstadoPedido.EN_CAMINO)
        pedidos[4].cambiarEstado(EstadoPedido.LISTO)
        return pedidos
    }
}
