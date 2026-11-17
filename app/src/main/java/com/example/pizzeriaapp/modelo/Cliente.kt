package com.example.pizzeriaapp.modelo

// Cliente: además del contacto tiene correo y dirección
class Cliente(nombre: String, telefono: String, val correo: String, val direccion: String) : Usuario(nombre, telefono) {

    override fun presentarse(): String {
        return "Soy el cliente $nombre | Teléfono: $contacto | Correo: $correo | Dirección: $direccion"
    }

    // Dos clientes son iguales si tienen el mismo nombre y el mismo correo
    // (el correo sin importar mayúsculas; el teléfono y la dirección no cuentan)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true          // es el mismo objeto
        if (other !is Cliente) return false      // no es un Cliente
        return nombre == other.nombre && correo.equals(other.correo, ignoreCase = true)
    }

    // Se calcula con los mismos datos que equals (correo en minúsculas), para que sea coherente
    override fun hashCode(): Int {
        return 31 * nombre.hashCode() + correo.lowercase().hashCode()
    }

    // Desestructuración: val (nombre, correo, telefono) = cliente

    // Primer valor: el nombre
    operator fun component1(): String = nombre

    // Segundo valor: el correo
    operator fun component2(): String = correo

    // Tercer valor: el teléfono (se guarda en contacto)
    operator fun component3(): String = contacto
}
