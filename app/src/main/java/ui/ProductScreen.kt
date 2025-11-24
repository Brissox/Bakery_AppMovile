package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.ui.carrito.CartViewModel
import com.example.prueba.ui.principal.components.UiProductosCard
import ui.Fav.FavoritosViewModel
import viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductoViewModel,
    // Opcional: puedes pasar estos VMs desde fuera si quieres compartir estado
    cartViewModel: CartViewModel = viewModel(), 
    favoritosViewModel: FavoritosViewModel = viewModel()
) {
    val productos = viewModel.productoList.collectAsState().value
    val error = viewModel.error.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Productos") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (error != null) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else if (productos.isEmpty()) {
                Text(
                    text = "Cargando o no hay productos...",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Usamos LazyVerticalGrid con 2 columnas fijas, igual que en la pantalla Principal
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos, key = { it.id_producto }) { producto ->
                        UiProductosCard(
                            producto = producto,
                            cartViewModel = cartViewModel,
                            esFavorito = favoritosViewModel.esFavorito(producto),
                            onFavoritoClick = { favoritosViewModel.toggleFavorito(producto) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}