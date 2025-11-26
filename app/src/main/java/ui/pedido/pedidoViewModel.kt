package ui.pedido

import Data.Remote.RetrofitInstance1
import Data.Remote.dto.PedidoResp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ui.app.AppViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PedidoViewModel(private val appViewModel: AppViewModel) : ViewModel() {

    val pedidos = mutableStateListOf<PedidoResp>()
    private val fechaFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    init {
        viewModelScope.launch {
            appViewModel.uidUsuario.collectLatest { uid ->
                if (!uid.isNullOrEmpty()) {
                    obtenerPedidos(appViewModel)
                } else {
                    pedidos.clear()
                }
            }
        }
    }

    private fun obtenerPedidos(appViewModel: AppViewModel) {
        viewModelScope.launch {
            try {
                val idUsuario = appViewModel.uidUsuario.value
                if (idUsuario != null) {
                    val lista = RetrofitInstance1.apip.getPedidos(idUsuario.toInt())
                    pedidos.clear()
                    pedidos.addAll(lista)
                } else {
                    pedidos.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                pedidos.clear()
            }
        }
    }



    fun formatFecha(fecha: LocalDateTime?): String {
        return fecha?.format(fechaFormat) ?: "Sin fecha"
    }
}