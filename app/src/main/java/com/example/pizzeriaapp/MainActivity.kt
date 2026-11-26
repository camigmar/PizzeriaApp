package com.example.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
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
        enableEdgeToEdge()
        setContent {
            PizzeriaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navegacion(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
