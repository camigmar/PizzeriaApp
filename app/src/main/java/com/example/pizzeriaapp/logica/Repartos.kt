package com.example.pizzeriaapp.logica

import com.example.pizzeriaapp.modelo.Repartidor

// Devuelve el repartidor disponible más cercano (o null si no hay ninguno)
fun asignarRepartidor(repartidores: List<Repartidor>): Repartidor? {
    return repartidores.sortedBy { it.distanciaKm }.find { it.disponible }
}
