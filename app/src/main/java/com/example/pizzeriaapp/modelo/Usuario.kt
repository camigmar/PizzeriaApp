package com.example.pizzeriaapp.modelo

// Clase base para cualquier persona del sistema
open class Usuario(val nombre: String, val contacto: String) {

    open fun presentarse(): String {
        return "Soy $nombre, mi contacto es $contacto"
    }
}
