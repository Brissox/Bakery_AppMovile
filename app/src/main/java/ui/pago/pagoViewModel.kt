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

    // Estos estados podrían manejarse en el ViewModel o en la UI. 
    // Si se pasan como parámetro al 'realizarPago', no es estrictamente necesario tenerlos aquí como state observable 
    // a menos que quieras validarlos en tiempo real.
    // Para simplificar y coincidir con tu pantalla, recibiré los valores en la función realizarPago.

    fun setCartItems(items: List<CartItem>) {
        _cartItems = items
        calcularTotal()
    }

    private fun calcularTotal() {
        // Corrección: 'producto' en singular, y conversión correcta
        total = cartItems.sumOf { it.productos.precio.toDouble() * it.cantidad }
    }

    // Modificamos la función para recibir los datos del formulario
    fun realizarPago(direccion: String, recibe: String, contacto: String) {
        println("➡ Procesando pago...")
        println("Dirección: $direccion")
        println("Recibe: $recibe")
        println("Contacto: $contacto")
        println("Total pagado: $total")

        // Aquí iría la lógica real (enviar al backend, etc.)

        // Limpieza post-pago
        _cartItems = emptyList()
        total = 0.0
    }
}