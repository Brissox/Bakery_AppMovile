package ui.pago

import Data.Remote.RetrofitInstance1
import Data.Remote.dto.DetallePedidoDTO
import Data.Remote.dto.PedidoDto
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.prueba.ui.carrito.CartItem
import kotlinx.coroutines.launch
import ui.app.AppViewModel


class PagoViewModel(
    private val appViewModel: AppViewModel,
    cartItemsInicial: List<CartItem> = emptyList() // agregamos el carrito inicial
) : ViewModel() {

    private var _cartItems by mutableStateOf(cartItemsInicial)
    val cartItems: List<CartItem> get() = _cartItems

    var total by mutableStateOf(0.0)
        private set

    init {
        calcularTotal()
    }

    // Permite actualizar los productos desde CartViewModel
    fun actualizarCarrito(items: List<CartItem>) {
        _cartItems = items
        calcularTotal()
    }

    private fun calcularTotal() {
        total = cartItems.sumOf { it.productos.precio.toDouble() * it.cantidad }
    }

    fun realizarPagoConIdUsuario(
        metodoDePago: String,
        descuentos: Int? = 0
    ) {
        val idUsuario = appViewModel.idUsuario.value
        if (idUsuario == null || cartItems.isEmpty()) return

        val detalles = cartItems.map { item ->
            DetallePedidoDTO(idProducto = item.productos.id_producto, cantidad = item.cantidad)
        }

        val crearPedidoDTO = PedidoDto(
            idUsuario = idUsuario.toLong(),
            cantidad_productos = cartItems.sumOf { it.cantidad },
            metodo_de_pago = metodoDePago,
            descuentos = descuentos ?: 0,
            detalles = detalles
        )

        viewModelScope.launch {
            try {
                val response = RetrofitInstance1.apip.crearPedido(crearPedidoDTO)
                if (response.isSuccessful) {
                    _cartItems = emptyList()
                    total = 0.0
                    println("✅ Pedido enviado correctamente")
                } else {
                    println("⚠ Error al enviar pedido: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                println("❌ Fallo en la conexión: ${e.message}")
            }
        }
    }
}
