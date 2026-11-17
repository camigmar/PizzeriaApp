package com.example.pizzeriaapp.modelo

// ---------- Productos promocionables específicos ----------
// Cada uno es su propia clase marcada con @Promocionable, así el descuento
// de promoción se aplica solo a ellos (se detecta con reflection en Promociones.kt)

@Promocionable(motivo = "Más vendida")
class PizzaPepperoni : Producto("Pepperoni", 8990.0, mapOf("masa" to 1, "salsa de tomate" to 1, "queso" to 2, "pepperoni" to 3), Categoria.PIZZA)

@Promocionable(motivo = "Opción saludable")
class PizzaVegetariana : Producto("Vegetariana", 9490.0, mapOf("masa" to 1, "salsa de tomate" to 1, "queso" to 2, "verduras" to 3), Categoria.PIZZA)

@Promocionable(motivo = "La más económica")
class PizzaMargarita : Producto("Margarita", 8490.0, mapOf("masa" to 1, "salsa de tomate" to 1, "queso" to 2, "albahaca" to 1), Categoria.PIZZA)

@Promocionable(motivo = "Ideal para compartir")
class Bebida500 : Producto("Bebida 500 ml", 1490.0, emptyMap(), Categoria.BEBIDA)

@Promocionable(motivo = "Fruta de temporada")
class JugoNatural : Producto("Jugo natural", 2490.0, emptyMap(), Categoria.BEBIDA)

// ---------- Carta ----------

// Pizzas de la carta, cada una con su receta (unidades por UNA pizza)
// Las que no son promocionables son un Producto común
object Carta {
    val pepperoni: Producto = PizzaPepperoni()

    val napolitana = Producto("Napolitana", 9490.0, mapOf("masa" to 1, "salsa de tomate" to 1, "queso" to 2, "tomate" to 2), Categoria.PIZZA)

    val hawaiana = Producto("Hawaiana", 9490.0, mapOf("masa" to 1, "salsa de tomate" to 1, "queso" to 2, "piña" to 2, "jamón" to 2), Categoria.PIZZA)

    val vegetariana: Producto = PizzaVegetariana()

    val margarita: Producto = PizzaMargarita()
}

// Bebidas de la carta: no tienen receta, por eso el mapa de ingredientes va vacío
object Bebidas {
    val bebida500: Producto = Bebida500()

    val jugoNatural: Producto = JugoNatural()

    val aguaMineral = Producto("Agua mineral", 1190.0, emptyMap(), Categoria.BEBIDA)
}
