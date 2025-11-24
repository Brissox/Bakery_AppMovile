package viewmodel

import Data.model.Productos
import Data.repository.ProductoRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {
    private val repository = ProductoRepository()

    private val _productList = MutableStateFlow<List<Productos>>(emptyList())
    val productoList: StateFlow<List<Productos>> = _productList

    // Estado para errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchProductos()
    }

    private fun fetchProductos() {
        viewModelScope.launch {
            try {
                Log.d("ProductoViewModel", "Iniciando petición...")
                val productos = repository.getProducto()
                Log.d("ProductoViewModel", "Productos recibidos: ${productos.size}")
                _productList.value = productos
                _error.value = null
            } catch (e: Exception) {
                Log.e("ProductoViewModel", "Error fetching products", e)
                _error.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }
}