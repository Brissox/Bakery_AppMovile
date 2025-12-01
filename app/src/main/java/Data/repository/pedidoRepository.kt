package Data.repository

import Data.Remote.RetrofitClient
import Data.Remote.RetrofitInstance1
import Data.Remote.dto.PedidoDto
import Data.Remote.dto.PedidoResp
import model.Feriado
import okhttp3.ResponseBody
import retrofit2.Response

class pedidoRepository{


    suspend fun listarPedidos(uid: String): List<PedidoResp> {
        val response = RetrofitInstance1.apip.ListarPedidos(uid)

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(
                "Error ${response.code()} al listar pedidos"
            )
        }
    }

    suspend fun crearPedido(pedido: PedidoDto): Boolean {
        return try {
            RetrofitInstance1.apip.crearPedido(pedido).isSuccessful
        } catch (e: Exception) {
            false
        }
    }


}