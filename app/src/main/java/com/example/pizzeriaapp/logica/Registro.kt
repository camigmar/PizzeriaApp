package com.example.pizzeriaapp.logica

import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.Producto

// Agrega un cliente al registro; el Set no acepta repetidos (usa equals y hashCode)
// Devuelve true si se registró o false si ya existía
fun registrarCliente(registro: MutableSet<Cliente>, cliente: Cliente): Boolean {
    return registro.add(cliente)
}

// Agrega un producto al registro; el Set no acepta repetidos (usa equals y hashCode)
// Devuelve true si se registró o false si ya existía
fun registrarProducto(registro: MutableSet<Producto>, producto: Producto): Boolean {
    return registro.add(producto)
}
