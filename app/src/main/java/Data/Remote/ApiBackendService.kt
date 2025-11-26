package Data.Remote

import Data.Remote.dto.LoginRequestDto
import Data.Remote.dto.LoginResponseDto
import Data.Remote.dto.PedidoDto
import Data.Remote.dto.PedidoResp
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

    @GET("/api/v1/Pedidos/User/{idUsuario}")
    suspend fun getPedidos(@Path("idUsuario") idUsuario: Int): List<PedidoResp>


    @POST("Pedidos")
    suspend fun crearPedido(@Body pedido: PedidoDto): Response<ResponseBody>

    @GET("Pedidos")
    suspend fun getPedidos(): List<PedidoResp>

    @GET("api/v1/Productos")
    suspend fun getProducto(): ProductoResponse


    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>




    @Multipart
    @PUT("Usuarios/{run}")
    suspend fun actualizarUsuarioConImagen(
        @Path("run") run: String,
        @Part("idFirebase") idFirebase: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("Usuarios/{rut}/imagen")
    suspend fun getImageByRut(@Path("rut") rut: String): Response<ResponseBody>

    @PUT("Usuarios/{rut}/nombre")
    suspend fun updateNombre(
        @Path("rut") rut: String,
        @Body body: Map<String, String>
    ): Response<UsuarioResp>
}