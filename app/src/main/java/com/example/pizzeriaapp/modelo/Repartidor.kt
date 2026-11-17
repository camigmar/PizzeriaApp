package com.example.pizzeriaapp.modelo

// Repartidor: tiene distancia al local y si está disponible
class Repartidor(nombre: String, telefono: String, val distanciaKm: Double, val disponible: Boolean) : Usuario(nombre, telefono) {

    override fun presentarse(): String {
        return "Soy el repartidor $nombre | Teléfono: $contacto | Estoy a $distanciaKm km"
    }

    fun entregarPedido(pedido: Pedido): String {
        return "$nombre entrega el pedido de ${pedido.cliente.nombre} en ${pedido.cliente.direccion}"
    }
}
