package Data.Remote

import Data.Remote.dto.PedidoDto
import Data.Remote.dto.PedidoResp
import Data.Remote.dto.ProductoResp
import Data.Remote.dto.UsuarioDto
import Data.Remote.dto.UsuarioResp
import Data.model.ProductoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiBackendService {

    @POST("Usuarios/Reg")
    suspend fun crearUsuario(@Body body: UsuarioDto): Response<ResponseBody>

    @GET("Usuarios/uid/{uidFb}")
    suspend fun getByFirebase(@Path("uidFb") uidFb: String): Response<UsuarioResp>

    @GET("pedidos/{idUsuario}")
    suspend fun getPedidos(@Path("idUsuario") idUsuario: Int): List<PedidoResp>

    @GET("Productos")
    suspend fun listarProductos(): Response<List<ProductoResp>>


    @POST("pedidos/crear")
    suspend fun crearPedido(@Body pedido: PedidoDto): Response<ResponseBody>


    @GET("Productos")
    suspend fun getProducto(): ProductoResponse




    @Multipart
    @PUT("Usuarios/{uidFb}")
    suspend fun actualizarUsuarioConImagen(
        @Path("run") run: String,
        @Part("idFirebase") idFirebase: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("Usuarios/{uidFb}/imagen")
    suspend fun getImageByRut(@Path("uidFb") rut: String): Response<ResponseBody>

    @PUT("Usuarios/{uidFb}/nombre")
    suspend fun updateNombre(
        @Path("uidFb") rut: String,
        @Body body: Map<String, String>
    ): Response<UsuarioResp>
}