package com.example.pizzeriaapp.modelo

// Un producto de la carta con su receta (cantidad de cada ingrediente por unidad) y su categoría
// Es "open" para poder crear productos específicos (por ejemplo los promocionables de Catalogo.kt)
open class Producto(val nombre: String, val precio: Double, val ingredientes: Map<String, Int>, val categoria: Categoria) {

    // No se permite crear un producto con precio 0 o negativo
    init {
        require(precio > 0) { "El precio debe ser mayor a 0" }
    }

    // Descripción con nombre, precio e ingredientes
    fun detallarProducto(): String {
        val textoIngredientes = ingredientes.map { "${it.key} x${it.value}" }.joinToString(", ")
        return "$nombre - $$precio | Ingredientes: $textoIngredientes"
    }

    // Dos productos son iguales si tienen el mismo nombre y la misma categoría
    // (el precio, los ingredientes y la clase específica no cuentan)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true          // es el mismo objeto
        if (other !is Producto) return false     // no es un Producto
        return nombre == other.nombre && categoria == other.categoria
    }

    // Se calcula con los mismos datos que equals, para que sea coherente
    override fun hashCode(): Int {
        return 31 * nombre.hashCode() + categoria.hashCode()
    }
}
