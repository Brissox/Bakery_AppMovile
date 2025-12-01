package ui.pago

import Data.Remote.RetrofitInstance1
import Data.Remote.dto.DetallePedidoDTO
import Data.Remote.dto.PedidoDto
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import com.example.prueba.ui.carrito.CartItem
import kotlinx.coroutines.launch
import ui.app.AppViewModel
import ui.pedido.PedidoUiState


class PagoViewModel(
    private val authRepo: FirebaseAuthDataSource,
    cartItemsInicial: List<CartItem> = emptyList()
) : ViewModel() {

    private var _cartItems by mutableStateOf(cartItemsInicial)
    val cartItems: List<CartItem> get() = _cartItems

    var total by mutableStateOf(0.0)
        private set

    init {
        calcularTotal()
    }

    fun actualizarCarrito(items: List<CartItem>) {
        _cartItems = items
        calcularTotal()
    }

    private fun calcularTotal() {
        total = cartItems.sumOf {
            it.productos.precio * it.cantidad
        }.toDouble()
    }

    fun realizarPagoConIdUsuario(
        metodoDePago: String,
        descuentos: Int = 0
    ) {
        val user = authRepo.currentUser() ?: return
        if (cartItems.isEmpty()) return

        val detalles = cartItems.map {
            DetallePedidoDTO(
                idProducto = it.productos.id_producto,
                cantidad = it.cantidad
            )
        }

        val pedido = PedidoDto(
            uid = user.uid,
            cantidad_productos = cartItems.sumOf { it.cantidad },
            metodo_de_pago = metodoDePago,
            descuentos = descuentos,
            detalles = detalles
        )

        viewModelScope.launch {
            try {
                val response = RetrofitInstance1.apip.crearPedido(pedido)
                if (response.isSuccessful) {
                    _cartItems = emptyList()
                    total = 0.0
                }
            } catch (_: Exception) {}
        }
    }
}
