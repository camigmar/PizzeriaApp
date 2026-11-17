package com.example.pizzeriaapp.logica

import java.time.LocalDate

// Cantidad permitida de cada producto por pedido (mínimo y máximo)
val RANGO_CANTIDAD = 1..100

// Máximo de unidades para el pedido completo (todas las pizzas y bebidas juntas)
const val MAX_UNIDADES_PEDIDO = 100

// Período de promoción (desde y hasta, ambos incluidos)
val RANGO_PROMOCION = LocalDate.of(2026, 11, 1)..LocalDate.of(2026, 11, 30)

// Descuento por defecto de un producto @Promocionable durante la promoción: 10%
const val DESCUENTO_PROMOCION = 0.10

// Indica si una fecha cae dentro del período de promoción
fun estaEnPromocion(fecha: LocalDate): Boolean {
    return fecha in RANGO_PROMOCION
}

// 10% de descuento si son más de 3 pizzas
fun calcularDescuento(subtotal: Double, cantidadPizzas: Int): Double {
    return if (cantidadPizzas > 3) subtotal * 0.10 else 0.0
}

// IVA de 19% sobre la base
fun calcularImpuesto(base: Double): Double {
    return base * 0.19
}

// Total a pagar: subtotal menos descuento más impuesto
fun calcularTotal(subtotal: Double, descuento: Double, impuesto: Double): Double {
    return subtotal - descuento + impuesto
}
