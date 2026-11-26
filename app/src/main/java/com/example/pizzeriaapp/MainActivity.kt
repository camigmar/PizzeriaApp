package com.example.pizzeriaapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.pizzeriaapp.ui.Navegacion
import com.example.pizzeriaapp.ui.PizzeriaViewModel
import com.example.pizzeriaapp.ui.theme.PizzeriaAppTheme

class MainActivity : ComponentActivity() {

    // Un solo ViewModel para toda la app; sobrevive a los giros de pantalla
    private val viewModel: PizzeriaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // El tema es siempre claro: íconos oscuros en las barras del sistema
        // aunque el teléfono esté en modo oscuro
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            PizzeriaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navegacion(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
