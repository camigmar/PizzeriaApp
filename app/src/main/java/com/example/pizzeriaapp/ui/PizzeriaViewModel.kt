package com.example.pizzeriaapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.data.PizzeriaRepositorio
import com.example.pizzeriaapp.data.ResumenGeneral
import com.example.pizzeriaapp.modelo.Cliente
import com.example.pizzeriaapp.modelo.Pedido
import kotlinx.coroutines.launch

// Todo lo que la pantalla necesita mostrar
data class EstadoPizzeria(
    val cargando: Boolean = false,
    val mensaje: String = "",
    val resumen: ResumenGeneral? = null,
    val pedidos: List<Pedido> = emptyList(),
    val clientes: List<Cliente> = emptyList()
)

// Pide los datos al repositorio y guarda el estado; Compose se redibuja cuando cambia
class PizzeriaViewModel(
    private val repositorio: PizzeriaRepositorio = PizzeriaRepositorio()
) : ViewModel() {

    // Solo el ViewModel puede cambiar el estado
    var estado by mutableStateOf(EstadoPizzeria())
        private set

    fun cargarResumen() {
        cargar("Generando resumen...") {
            estado = estado.copy(resumen = repositorio.cargarResumen())
        }
    }

    fun cargarPedidos() {
        cargar("Cargando pedidos...") {
            estado = estado.copy(pedidos = repositorio.cargarPedidos())
        }
    }

    fun cargarClientes() {
        cargar("Cargando clientes...") {
            estado = estado.copy(clientes = repositorio.cargarClientes())
        }
    }

    // Muestra la carga con su mensaje, ejecuta la tarea y al terminar (aunque falle) la oculta
    private fun cargar(mensaje: String, tarea: suspend () -> Unit) {
        viewModelScope.launch {
            estado = estado.copy(cargando = true, mensaje = mensaje)
            try {
                tarea()
            } finally {
                estado = estado.copy(cargando = false, mensaje = "")
            }
        }
    }
}
