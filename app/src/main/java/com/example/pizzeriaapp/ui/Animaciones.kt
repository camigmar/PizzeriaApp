package com.example.pizzeriaapp.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

// Animaciones comunes de la app: todas las pantallas usan estas para que se vean iguales
object Animaciones {

    // Duración de cada animación en milisegundos
    const val DURACION_MS = 600

    // Fade In: el elemento aparece de a poco
    fun entrada(): EnterTransition = fadeIn(animationSpec = tween(DURACION_MS))

    // Fade Out: el elemento se desvanece de a poco
    fun salida(): ExitTransition = fadeOut(animationSpec = tween(DURACION_MS))
}
