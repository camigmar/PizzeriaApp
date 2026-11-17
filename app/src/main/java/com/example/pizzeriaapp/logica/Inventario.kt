package com.example.pizzeriaapp.logica

import com.example.pizzeriaapp.modelo.Pedido

// Un ingrediente que no alcanza para el pedido
data class Faltante(val ingrediente: String, val necesario: Int, val disponible: Int) {
    val faltan: Int
        get() = necesario - disponible
}

// Resultado de revisar el stock: si se confirmó, lo que se necesitaba y lo que faltó
data class ResultadoStock(
    val confirmado: Boolean,
    val necesarios: Map<String, Int>,
    val faltantes: List<Faltante>
)

// Cantidad total de cada ingrediente para un lote de pizzas
fun calcularIngredientesNecesarios(receta: Map<String, Int>, cantidadPizzas: Int): Map<String, Int> {
    val necesarios = mutableMapOf<String, Int>()
    for ((ingrediente, cantidadPorPizza) in receta) {
        necesarios[ingrediente] = cantidadPorPizza * cantidadPizzas
    }
    return necesarios
}

// Resta del inventario lo que usa el pedido
fun descontarInventario(inventario: MutableMap<String, Int>, necesarios: Map<String, Int>) {
    for ((ingrediente, cantidad) in necesarios) {
        inventario[ingrediente] = (inventario[ingrediente] ?: 0) - cantidad
    }
}

// Suma unidades de un ingrediente al inventario; la cantidad debe ser positiva
fun reabastecerInventario(inventario: MutableMap<String, Int>, ingrediente: String, cantidad: Int) {
    require(cantidad > 0) { "La cantidad a reabastecer debe ser mayor a 0 (se recibió $cantidad)" }
    inventario[ingrediente] = (inventario[ingrediente] ?: 0) + cantidad
}

// Revisa si hay ingredientes para todo el pedido
// Si alcanzan, los descuenta del inventario; si no, devuelve la lista de lo que falta
fun verificarStock(pedido: Pedido, inventario: MutableMap<String, Int>): ResultadoStock {
    // Ingredientes que necesita el pedido completo: se suman los de cada producto distinto
    val necesarios = mutableMapOf<String, Int>()
    for ((producto, productosIguales) in pedido.productos.groupBy { it }) {
        val necesariosProducto = calcularIngredientesNecesarios(producto.ingredientes, productosIguales.size)
        for ((ingrediente, cantidad) in necesariosProducto) {
            necesarios[ingrediente] = (necesarios[ingrediente] ?: 0) + cantidad
        }
    }

    // Revisar cada ingrediente necesario y guardar los que no alcanzan
    val faltantes = mutableListOf<Faltante>()
    for ((ingrediente, necesario) in necesarios) {
        val disponible = inventario[ingrediente] ?: 0
        if (necesario > disponible) {
            faltantes.add(Faltante(ingrediente, necesario, disponible))
        }
    }

    val confirmado = faltantes.isEmpty()
    if (confirmado) {
        // Descontar del inventario lo que usa el pedido
        descontarInventario(inventario, necesarios)
    }
    return ResultadoStock(confirmado, necesarios, faltantes)
}

// Texto con los ingredientes que faltan y los pasos para reabastecer (lo que antes se imprimía)
fun describirFaltantes(resultado: ResultadoStock): String {
    val texto = StringBuilder()
    texto.appendLine("El pedido no puede completarse. Faltan los siguientes ingredientes:")
    for (faltante in resultado.faltantes) {
        texto.appendLine("- ${faltante.ingrediente}: necesario ${faltante.necesario}, disponible ${faltante.disponible}")
        texto.appendLine("  Pasos para reabastecer:")
        texto.appendLine("  1. Contactar al proveedor.")
        texto.appendLine("  2. Solicitar ${faltante.faltan} unidades de ${faltante.ingrediente}.")
        texto.appendLine("  3. Actualizar el inventario.")
    }
    return texto.toString().trimEnd()
}
