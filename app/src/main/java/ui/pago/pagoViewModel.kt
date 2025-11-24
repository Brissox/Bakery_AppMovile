package ui.pago

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.prueba.ui.carrito.CartItem

class PagoViewModel : ViewModel() {
    private var _cartItems by mutableStateOf(listOf<CartItem>())
    val cartItems: List<CartItem> get() = _cartItems

    var total by mutableStateOf(0.0)
        private set

    fun setCartItems(items: List<CartItem>) {
        _cartItems = items
        calcularTotal()
    }

    private fun calcularTotal() {
        total = cartItems.sumOf { it.productos.precio.toDouble() * it.cantidad }
    }


    fun realizarPago() {
        // Simular pago
        _cartItems = emptyList()
        total = 0.0
    }
}
