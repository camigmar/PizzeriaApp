package com.example.pizzeriaapp.modelo

import com.example.pizzeriaapp.logica.DESCUENTO_PROMOCION

// Marca una clase de producto que puede entrar en promociones
// Se lee en tiempo de ejecución (reflection) para aplicar el descuento solo a estos productos
// motivo: por qué es promocionable; porcentaje: descuento que se le aplica durante la promoción
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Promocionable(val motivo: String = "", val porcentaje: Double = DESCUENTO_PROMOCION)
