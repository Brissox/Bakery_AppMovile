package ui.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import ui.pedido.PedidoViewModel

class PedidoVMFactory(
    private val authRepo: FirebaseAuthDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PedidoViewModel::class.java)) {
            return PedidoViewModel(authRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

