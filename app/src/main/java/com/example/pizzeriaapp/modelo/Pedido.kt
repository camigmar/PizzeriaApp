package com.example.pizzeriaapp.modelo

import com.example.pizzeriaapp.logica.MAX_UNIDADES_PEDIDO
import com.example.pizzeriaapp.logica.calcularDescuento
import com.example.pizzeriaapp.logica.calcularImpuesto
import com.example.pizzeriaapp.logica.calcularTotal
import com.example.pizzeriaapp.logica.descuentoPromocion
import java.time.LocalDate

// Un pedido de un cliente con los productos que pidió
// La lista productos tiene UNA entrada por unidad (2 Pepperoni = 2 elementos en la lista)
// Las notas son opcionales: si no se indican quedan en null
// La fecha, si no se indica, es la de hoy
class Pedido(val cliente: Cliente, val productos: List<Producto>, val notas: String? = null, val fecha: LocalDate = LocalDate.now()) {

    // Tope para el pedido completo (sumando pizzas y bebidas), no solo por producto
    // Como también se revisa al crear el pedido que devuelve +, aplica a los pedidos combinados
    init {
        require(productos.size <= MAX_UNIDADES_PEDIDO) {
            "El pedido no puede tener más de $MAX_UNIDADES_PEDIDO unidades en total (tiene ${productos.size})"
        }
    }

    // Solo se cambia con cambiarEstado() para que siempre sea un estado válido
    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION
        private set

    // Suma de los precios de todos los productos
    fun calcularSubtotal(): Double {
        return productos.sumOf { it.precio }
    }

    // Descuento del pedido: el de cantidad (función existente) más el de promoción si corresponde
    // El de cantidad cuenta solo las pizzas y se calcula sobre su subtotal
    // El de promoción se aplica solo a los productos marcados con @Promocionable (reflection)
    fun calcularDescuento(): Double {
        val cantidadPizzas = productos.count { it.categoria == Categoria.PIZZA }
        val subtotalPizzas = productos.filter { it.categoria == Categoria.PIZZA }.sumOf { it.precio }
        val descuentoCantidad = calcularDescuento(subtotalPizzas, cantidadPizzas)
        return descuentoCantidad + descuentoPromocion(productos, fecha)
    }

    // IVA sobre el monto ya descontado, usando la función existente
    fun calcularImpuesto(): Double {
        return calcularImpuesto(calcularSubtotal() - calcularDescuento())
    }

    // Total con descuento e impuesto, usando las funciones existentes
    fun calcularTotal(): Double {
        return calcularTotal(calcularSubtotal(), calcularDescuento(), calcularImpuesto())
    }

    // Cantidad por producto, ejemplo: "2 Pepperoni, 1 Vegetariana"
    fun resumirProductos(): String {
        return productos.groupBy { it.nombre }.map { "${it.value.size} ${it.key}" }.joinToString(", ")
    }

    // Operador +: junta dos pedidos del mismo cliente en uno nuevo (los originales no cambian)
    // Exige la misma fecha y que ambos sigan en preparación
    // Los montos se recalculan solos porque dependen de la lista de productos y la fecha
    operator fun plus(otro: Pedido): Pedido {
        require(cliente.correo == otro.cliente.correo) {
            "Solo se pueden combinar pedidos del mismo cliente (${cliente.nombre} y ${otro.cliente.nombre} son distintos)"
        }
        require(fecha == otro.fecha) {
            "Solo se pueden combinar pedidos de la misma fecha ($fecha y ${otro.fecha} son distintas)"
        }
        require(estado.admiteCombinar && otro.estado.admiteCombinar) {
            "Solo se pueden combinar pedidos \"${EstadoPedido.EN_PREPARACION}\" (los estados son \"$estado\" y \"${otro.estado}\")"
        }

        // Notas: ambas unidas con " | ", o la que exista, o null si no hay ninguna
        val notasCombinadas = when {
            notas != null && otro.notas != null -> "$notas | ${otro.notas}"
            notas != null -> notas
            else -> otro.notas
        }

        // El init del nuevo pedido revisa que no pase el tope de unidades
        return Pedido(cliente, productos + otro.productos, notasCombinadas, fecha)
    }

    // Cambia el estado solo si el cambio sigue el orden correcto
    // Si no se puede, lanza IllegalStateException con el aviso (antes se imprimía por consola)
    fun cambiarEstado(nuevoEstado: EstadoPedido) {
        val siguiente = estado.siguiente

        // El pedido ya llegó al estado final
        checkNotNull(siguiente) {
            "El pedido ya está \"$estado\" (estado final), no tiene más cambios."
        }

        // El estado existe, pero no se puede saltar a él desde el actual
        check(nuevoEstado == siguiente) {
            "No se puede pasar de \"$estado\" a \"$nuevoEstado\". Siguiente estado permitido: \"$siguiente\"."
        }

        estado = nuevoEstado
    }

    // Desestructuración: val (cliente, productos, total) = pedido

    // Primer valor: el cliente del pedido
    operator fun component1(): Cliente = cliente

    // Segundo valor: la lista de productos
    operator fun component2(): List<Producto> = productos

    // Tercer valor: el total a pagar (se calcula al momento)
    operator fun component3(): Double = calcularTotal()
}
