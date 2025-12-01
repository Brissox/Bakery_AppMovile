package ui.pedido

import Data.Remote.dto.PedidoResp
import Data.repository.pedidoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ui.app.AppViewModel

data class PedidoUiState(
    val loading: Boolean = false,
    val items: List<PedidoResp> = emptyList(),
    val error: String? = null
)

class PedidoViewModel(
    private val authRepo: FirebaseAuthDataSource,
    private val repo: pedidoRepository = pedidoRepository()
) : ViewModel() {


    private val _uiState = MutableStateFlow(PedidoUiState(loading = true))
    val uiState: StateFlow<PedidoUiState> = _uiState

    init {
        val user = authRepo.currentUser()
        if (user?.uid != null) {
            cargarPedidos(user.uid)
        } else {
            _uiState.value = PedidoUiState(
                loading = false,
                error = "Usuario no autenticado"
            )
        }
    }

    fun cargarPedidos(uid: String) {
        _uiState.value = PedidoUiState(loading = true)

        viewModelScope.launch {
            try {
                val data = repo.listarPedidos(uid)
                _uiState.value = PedidoUiState(items = data)
            } catch (e: Exception) {
                _uiState.value = PedidoUiState(
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
}
