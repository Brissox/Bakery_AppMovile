package com.example.prueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.prueba.ui.theme.PruebaTheme
import ui.ProductScreen
import ui.app.AppNavHost
import viewmodel.ProductoViewModel
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hace que la app ocupe toda la pantalla (detrás de la barra de estado)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PruebaTheme {
                // Inicializamos el ViewModel y cargamos la pantalla de productos directamente
                val productoViewModel: ProductoViewModel = viewModel()
                ProductScreen(viewModel = productoViewModel)
            }
        }
    }
}*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebaTheme {
                AppNavHost()
            }
        }
    }
}