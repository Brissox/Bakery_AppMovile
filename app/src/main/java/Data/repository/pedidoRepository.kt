package Data.repository

import Data.Remote.RetrofitInstance1
import Data.Remote.dto.PedidoDto
import Data.Remote.dto.PedidoResp
import okhttp3.ResponseBody
import retrofit2.Response

class pedidoRepository{


    suspend fun ListarPedidos(idUsuario: Int): List<PedidoResp> {
        return RetrofitInstance1.apip.getPedidos(idUsuario)
    }

    suspend fun crearPedido(pedido: PedidoDto): Boolean {
        return try {
            RetrofitInstance1.apip.crearPedido(pedido).isSuccessful
        } catch (e: Exception) {
            false
        }
    }


}