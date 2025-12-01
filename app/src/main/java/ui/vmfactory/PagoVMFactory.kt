package ui.pago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import com.example.prueba.ui.carrito.CartItem
import ui.app.AppViewModel

class PagoVMFactory(
    private val authRepo: FirebaseAuthDataSource,
    private val cartItems: List<CartItem>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PagoViewModel(
                authRepo,
                cartItems
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
