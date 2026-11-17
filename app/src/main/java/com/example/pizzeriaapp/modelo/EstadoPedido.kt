package com.example.pizzeriaapp.modelo

// Estados por los que pasa un pedido, en orden:
// En preparación -> Listo -> En camino -> Entregado
enum class EstadoPedido(val texto: String) {
    EN_PREPARACION("En preparación"),
    LISTO("Listo"),
    EN_CAMINO("En camino"),
    ENTREGADO("Entregado");

    // Único estado al que se puede pasar desde este (null si es el estado final)
    val siguiente: EstadoPedido?
        get() = when (this) {
            EN_PREPARACION -> LISTO
            LISTO -> EN_CAMINO
            EN_CAMINO -> ENTREGADO
            ENTREGADO -> null
        }

    // Solo un pedido entregado se considera completado ("Listo" y "En camino" siguen pendientes)
    val esCompletado: Boolean
        get() = this == ENTREGADO

    // Solo los pedidos que aún se están preparando se pueden combinar con +
    val admiteCombinar: Boolean
        get() = this == EN_PREPARACION

    // Así "$estado" muestra el mismo texto que antes ("En preparación", "Listo", ...)
    override fun toString(): String = texto
}
