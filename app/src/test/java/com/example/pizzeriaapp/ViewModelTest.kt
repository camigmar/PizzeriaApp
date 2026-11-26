package com.example.pizzeriaapp

import com.example.pizzeriaapp.ui.PizzeriaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// viewModelScope usa el hilo principal; en las pruebas se reemplaza por uno de prueba
// que controla el tiempo virtual (cada carga tarda 2000 ms)
class ViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun sigueCargandoMientrasQuedeUnaCargaEnCurso() = runTest(dispatcher) {
        val viewModel = PizzeriaViewModel()

        // El resumen parte en t=0 y termina en t=2000
        viewModel.cargarResumen()
        runCurrent()
        assertTrue(viewModel.estado.cargando)

        // Los pedidos parten en t=1000 y terminan en t=3000
        advanceTimeBy(1000)
        viewModel.cargarPedidos()
        runCurrent()

        // En t=2100 el resumen ya terminó, pero los pedidos no
        advanceTimeBy(1100)
        runCurrent()
        assertNotNull(viewModel.estado.resumen)
        assertTrue(viewModel.estado.cargando)

        // En t=3100 ya no queda ninguna carga
        advanceTimeBy(1000)
        runCurrent()
        assertEquals(5, viewModel.estado.pedidos.size)
        assertFalse(viewModel.estado.cargando)
        assertEquals("", viewModel.estado.mensaje)
    }
}
