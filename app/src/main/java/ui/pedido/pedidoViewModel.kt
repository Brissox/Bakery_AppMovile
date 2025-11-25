package ui.pedido

import Data.Remote.dto.PedidoResp
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class PedidoViewModel : ViewModel() {

    val pedidos = mutableStateListOf<PedidoResp>()

    private val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    init {
        // Ejemplos de pedidos
        pedidos.addAll(
            listOf(
                PedidoResp(
                    FECHA = Date(),
                    CANTIDAD_PRODUCTOS = 3,
                    TOTAL = 15990,
                    METODO_DE_PAGO = "Tarjeta Débito",
                    Descuentos = 1000,
                    CANTIDAD = 1,
                    PRECIO_UNITARIO = 7990,
                    SUBTOTAL = 6990
                ),
                PedidoResp(
                    FECHA = Date(),
                    CANTIDAD_PRODUCTOS = 1,
                    TOTAL = 9990,
                    METODO_DE_PAGO = "Transferencia",
                    Descuentos = 0,
                    CANTIDAD = 1,
                    PRECIO_UNITARIO = 9990,
                    SUBTOTAL = 9990
                )
            )
        )
    }

    fun formatFecha(date: Date): String {
        return fechaFormat.format(date)
    }
}