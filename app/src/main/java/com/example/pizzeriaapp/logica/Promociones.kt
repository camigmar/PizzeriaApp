package com.example.pizzeriaapp.logica

import com.example.pizzeriaapp.modelo.Producto
import com.example.pizzeriaapp.modelo.Promocionable
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

// Anotación @Promocionable de la clase del producto, o null si no la tiene (reflection)
fun promocionDe(producto: Producto): Promocionable? {
    return producto::class.findAnnotation<Promocionable>()
}

// Indica si la clase del producto está marcada con @Promocionable
fun esPromocionable(producto: Producto): Boolean {
    return promocionDe(producto) != null
}

// Descuento de promoción de una lista de productos (una entrada por unidad)
// Solo cuenta si la fecha está en el período de promoción, y solo para los productos @Promocionable,
// cada uno con el porcentaje indicado en su anotación
fun descuentoPromocion(productos: List<Producto>, fecha: LocalDate): Double {
    if (!estaEnPromocion(fecha)) return 0.0
    return productos.sumOf { producto ->
        val promocion = promocionDe(producto)
        if (promocion != null) producto.precio * promocion.porcentaje else 0.0
    }
}

// Productos promocionables dentro de la fuente indicada (por ejemplo Carta o Bebidas)
// Se buscan con reflection: se lee cada propiedad con su getter y se revisa la anotación de su clase
fun productosPromocionables(fuente: Any): List<Producto> {
    return fuente::class.memberProperties
        .mapNotNull { it.getter.call(fuente) as? Producto }
        .filter { esPromocionable(it) }
}

// Describe una clase con reflection: nombre, propiedades y funciones declaradas en ella
fun describirClase(clase: KClass<*>): String {
    val descripcion = StringBuilder()
    descripcion.appendLine("Clase: ${clase.simpleName}")

    // Propiedades como "nombre: Tipo", ordenadas por nombre
    // Se quita el paquete del tipo (ej: kotlin.String -> String) para que sea más fácil de leer
    descripcion.appendLine("  Propiedades:")
    for (propiedad in clase.declaredMemberProperties.sortedBy { it.name }) {
        val tipo = propiedad.returnType.toString().replace(Regex("\\b[a-z]\\w*\\."), "")
        descripcion.appendLine("    - ${propiedad.name}: $tipo")
    }

    // Funciones solo con su nombre, ordenadas por nombre
    descripcion.appendLine("  Funciones:")
    val funciones = clase.declaredFunctions.sortedBy { it.name }
    if (funciones.isEmpty()) {
        descripcion.appendLine("    (ninguna)")
    } else {
        for (funcion in funciones) {
            descripcion.appendLine("    - ${funcion.name}()")
        }
    }

    return descripcion.toString().trimEnd()
}
