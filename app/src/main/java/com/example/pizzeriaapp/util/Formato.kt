package com.example.pizzeriaapp.util

import java.util.Locale

// Monto en pesos chilenos, con punto de miles y sin decimales. Ejemplo: "$26.970"
fun formatearMonto(monto: Double): String {
    return String.format(Locale.forLanguageTag("es-CL"), "$%,.0f", monto)
}

// Formatea celulares chilenos como "+56 (9) XXXX-XXXX"; devuelve null si no es válido
fun formatearTelefono(telefono: String): String? {
    // Se quita todo lo que no sea dígito (espacios, +, paréntesis, guiones)
    val soloDigitos = telefono.replace(Regex("\\D"), "")

    // 56 opcional, luego el 9 y dos grupos de 4 dígitos
    val regexTelefono = Regex("^(?:56)?(9)(\\d{4})(\\d{4})$")
    val resultado = regexTelefono.matchEntire(soloDigitos) ?: return null

    // Se arma el formato con los grupos capturados (siempre con +56)
    val (nueve, parte1, parte2) = resultado.destructured
    return "+56 ($nueve) $parte1-$parte2"
}
