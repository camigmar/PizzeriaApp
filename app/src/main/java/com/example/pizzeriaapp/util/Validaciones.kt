package com.example.pizzeriaapp.util

// El correo es válido si tiene la forma usuario@dominio.ext (antes era regexCorreo dentro de main)
val REGEX_CORREO = Regex("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")

// Indica si el correo (sin espacios al inicio ni al final) tiene un formato válido
fun esCorreoValido(correo: String): Boolean {
    return REGEX_CORREO.matches(correo.trim())
}
