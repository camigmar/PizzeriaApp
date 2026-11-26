package com.example.pizzeriaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Rutas de la app
object Rutas {
    const val PRINCIPAL = "principal"
    const val PEDIDOS = "pedidos"
    const val CLIENTES = "clientes"
}

// Une las pantallas; todas usan el mismo ViewModel que llega desde MainActivity
@Composable
fun Navegacion(viewModel: PizzeriaViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.PRINCIPAL, modifier = modifier) {
        composable(Rutas.PRINCIPAL) {
            PantallaPrincipal(
                estado = viewModel.estado,
                cargarResumen = viewModel::cargarResumen,
                verPedidos = { navController.navigate(Rutas.PEDIDOS) },
                verClientes = { navController.navigate(Rutas.CLIENTES) }
            )
        }
        composable(Rutas.PEDIDOS) {
            PantallaPedidos(
                estado = viewModel.estado,
                cargarPedidos = viewModel::cargarPedidos,
                volver = { navController.popBackStack() }
            )
        }
        composable(Rutas.CLIENTES) {
            PantallaClientes(
                estado = viewModel.estado,
                cargarClientes = viewModel::cargarClientes,
                volver = { navController.popBackStack() }
            )
        }
    }
}
