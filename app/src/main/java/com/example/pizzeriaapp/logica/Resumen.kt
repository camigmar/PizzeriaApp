package com.example.pizzeriaapp.logica

import com.example.pizzeriaapp.modelo.Bebidas
import com.example.pizzeriaapp.modelo.Carta
import com.example.pizzeriaapp.modelo.Categoria
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.Pedido
import com.example.pizzeriaapp.modelo.Producto
import com.example.pizzeriaapp.util.formatearMonto
import java.time.LocalDate

// Texto del estado de un pedido rechazado por falta de ingredientes
const val ESTADO_RECHAZADO = "Rechazado por falta de ingredientes"

// Pedidos completados: solo los entregados ("Listo" y "En camino" no cuentan)
fun pedidosCompletados(pedidos: List<Pedido>): List<Pedido> {
    return pedidos.filter { it.estado.esCompletado }
}

// Pedidos pendientes: todos los que aún no se entregan
fun pedidosPendientes(pedidos: List<Pedido>): List<Pedido> {
    return pedidos.filterNot { it.estado.esCompletado }
}

// Resumen final de la jornada con datos reales: pedidos combinados, desestructuración,
// productos promocionables e igualdad de objetos
// Si el cliente tiene menos de 2 pedidos confirmados, se usa como ejemplo la suma de los dos pedidos de ejemplo
fun generarResumenFinal(
    cliente: Cliente,
    pedidosReales: List<Pedido>,
    pedidoEjemplo1: Pedido,
    pedidoEjemplo2: Pedido,
    registroClientes: Set<Cliente>,
    registroProductos: Set<Producto>
): String {
    val lineaDoble = "=".repeat(60)
    val lineaSimple = "-".repeat(60)
    val resumen = StringBuilder()

    // Encabezado con la fecha de hoy
    resumen.appendLine(lineaDoble)
    resumen.appendLine("          RESUMEN FINAL DE LA JORNADA (${LocalDate.now()})")
    resumen.appendLine(lineaDoble)

    // 1) Pedidos combinados con el operador +
    resumen.appendLine("1) PEDIDOS COMBINADOS (operador +)")
    // Con 2 o más pedidos reales se combinan todos; si no, se usa el ejemplo
    val pedidosACombinar = if (pedidosReales.size >= 2) pedidosReales else listOf(pedidoEjemplo1, pedidoEjemplo2)
    if (pedidosReales.size < 2) {
        resumen.appendLine("  (Ejemplo: el cliente tiene menos de 2 pedidos confirmados)")
    }
    for ((indice, pedido) in pedidosACombinar.withIndex()) {
        resumen.appendLine("  Pedido ${indice + 1} (${pedido.cliente.nombre}): ${pedido.resumirProductos()} | Total: ${formatearMonto(pedido.calcularTotal())}")
    }
    // + ahora valida fecha, estado y tope de unidades: si no se pueden combinar se muestra el motivo
    try {
        val combinado = pedidosACombinar.reduce { a, b -> a + b }
        resumen.appendLine("  Combinado: ${combinado.resumirProductos()}")
        resumen.appendLine("  Descuento: ${formatearMonto(combinado.calcularDescuento())} | Total: ${formatearMonto(combinado.calcularTotal())}")
    } catch (e: IllegalArgumentException) {
        resumen.appendLine("  No se pudieron combinar: ${e.message}")
    }
    resumen.appendLine(lineaSimple)

    // 2) Cliente separado en sus partes
    resumen.appendLine("2) CLIENTE DESESTRUCTURADO")
    val (nombre, correo, telefono) = cliente
    resumen.appendLine("  Nombre:   $nombre")
    resumen.appendLine("  Correo:   $correo")
    resumen.appendLine("  Teléfono: $telefono")
    resumen.appendLine(lineaSimple)

    // 3) Productos marcados con @Promocionable (motivo leído con reflection en la clase del producto)
    resumen.appendLine("3) PRODUCTOS PROMOCIONABLES (anotación @Promocionable)")
    for (fuente in listOf(Carta, Bebidas)) {
        resumen.appendLine("  ${fuente::class.simpleName}:")
        for (producto in productosPromocionables(fuente)) {
            val promocion = promocionDe(producto) ?: continue
            resumen.appendLine("    - ${producto.nombre.padEnd(14)} ${formatearMonto(producto.precio).padStart(7)} | ${promocion.motivo}")
        }
    }
    resumen.appendLine(lineaSimple)

    // 4) Igualdad de objetos: todos los resultados se calculan aquí
    resumen.appendLine("4) IGUALDAD DE OBJETOS")
    val copiaCliente = Cliente(cliente.nombre, cliente.contacto, cliente.correo, "Otra dirección 123")
    resumen.appendLine("  Cliente == copia con otra dirección:      ${cliente == copiaCliente}")

    // Si María ya está en el registro, un duplicado suyo sería rechazado
    val mariaDuplicada = Cliente("María Soto", "912345678", "maria.soto@correo.cl", "Otra Calle 999")
    val textoMaria = if (mariaDuplicada in registroClientes) "rechazada" else "aceptada"
    resumen.appendLine("  María duplicada en el registro:           $textoMaria")

    val pepperoniOtroPrecio = Producto("Pepperoni", 9990.0, mapOf("masa" to 1), Categoria.PIZZA)
    val pepperoniBebida = Producto("Pepperoni", 8990.0, emptyMap(), Categoria.BEBIDA)
    resumen.appendLine("  Pepperoni == Pepperoni con otro precio:   ${Carta.pepperoni == pepperoniOtroPrecio}")
    resumen.appendLine("  Pepperoni (PIZZA) == Pepperoni (BEBIDA):  ${Carta.pepperoni == pepperoniBebida}")
    resumen.appendLine("  Clientes distintos registrados:           ${registroClientes.size}")
    resumen.appendLine("  Productos distintos registrados:          ${registroProductos.size}")
    resumen.append(lineaDoble)

    return resumen.toString()
}

// Resumen completo del pedido listo para mostrar
fun generarResumen(idPedido: Int, pedido: Pedido, estado: String, repartidor: String): String {
    val lineaDoble = "=".repeat(50)
    val lineaSimple = "-".repeat(50)
    val resumen = StringBuilder()

    // Encabezado
    resumen.appendLine(lineaDoble)
    resumen.appendLine("            RESUMEN DEL PEDIDO N° $idPedido")
    resumen.appendLine(lineaDoble)

    // Cliente
    resumen.appendLine("CLIENTE")
    resumen.appendLine("  Nombre:    ${pedido.cliente.nombre}")
    resumen.appendLine("  Teléfono:  ${pedido.cliente.contacto}")
    resumen.appendLine("  Correo:    ${pedido.cliente.correo}")
    resumen.appendLine("  Dirección: ${pedido.cliente.direccion}")
    resumen.appendLine(lineaSimple)

    // Productos: una línea por cada producto distinto
    resumen.appendLine("PRODUCTOS")
    for ((nombreProducto, productosIguales) in pedido.productos.groupBy { it.nombre }) {
        val precioUnitario = productosIguales.first().precio
        val cantidad = productosIguales.size
        val subtotalLinea = precioUnitario * cantidad
        resumen.appendLine("  ${nombreProducto.padEnd(14)} ${formatearMonto(precioUnitario).padStart(9)} x $cantidad = ${formatearMonto(subtotalLinea).padStart(10)}")
    }
    resumen.appendLine(lineaSimple)

    // Totales
    resumen.appendLine("TOTALES")
    resumen.appendLine("  ${"Subtotal:".padEnd(18)}${formatearMonto(pedido.calcularSubtotal()).padStart(12)}")
    resumen.appendLine("  ${"Descuento:".padEnd(18)}${formatearMonto(pedido.calcularDescuento()).padStart(12)}")
    resumen.appendLine("  ${"Impuesto (19%):".padEnd(18)}${formatearMonto(pedido.calcularImpuesto()).padStart(12)}")
    resumen.appendLine("  ${"TOTAL:".padEnd(18)}${formatearMonto(pedido.calcularTotal()).padStart(12)}")
    // Si fue rechazado los montos son solo referenciales
    if (estado.startsWith("Rechazado")) {
        resumen.appendLine("  (Pedido rechazado: este monto NO se cobrará)")
    }
    resumen.appendLine(lineaSimple)

    // Fecha y promoción del pedido: solo se aplica a los productos @Promocionable
    val promocionablesDelPedido = pedido.productos.filter { esPromocionable(it) }.map { it.nombre }.distinct()
    val textoPromocion = when {
        !estaEnPromocion(pedido.fecha) -> "No aplica"
        promocionablesDelPedido.isEmpty() -> "Vigente, pero el pedido no tiene productos promocionables"
        else -> "Aplicada a ${promocionablesDelPedido.joinToString(", ")} (${formatearMonto(descuentoPromocion(pedido.productos, pedido.fecha))})"
    }
    resumen.appendLine("  Fecha:      ${pedido.fecha}")
    resumen.appendLine("  Promoción:  $textoPromocion")

    // Estado, repartidor y notas
    resumen.appendLine("  Estado:     $estado")
    resumen.appendLine("  Repartidor: $repartidor")
    resumen.appendLine("  Notas:      ${pedido.notas ?: "Sin notas"}")
    resumen.appendLine(lineaSimple)

    // Pizzas y bebidas que pueden entrar en promociones, ordenadas por nombre
    val promocionables = productosPromocionables(Carta) + productosPromocionables(Bebidas)
    resumen.appendLine("  Promocionables: ${promocionables.map { it.nombre }.sorted().joinToString(", ")}")
    resumen.append(lineaDoble)

    return resumen.toString()
}
