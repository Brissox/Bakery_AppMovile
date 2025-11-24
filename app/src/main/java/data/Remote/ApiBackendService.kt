package Data.Remote

import Data.Remote.dto.UsuarioDto
import Data.Remote.dto.UsuarioResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiBackendService {

    @POST("api/v1/Usuarios")
    suspend fun crearUsuario(@Body body: UsuarioDto): Response<ResponseBody>

    @GET("api/v1/Usuarios/uid/{uidFb}")
    suspend fun getByFirebase(@Path("uidFb") uidFb: String): Response<UsuarioResp>

    @Multipart
    @PUT("api/v1/Usuarios/{run}")
    suspend fun actualizarUsuarioConImagen(
        @Path("run") run: String,
        @Part("idFirebase") idFirebase: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<ResponseBody>

    // --- Corrección aquí ---

    @GET("api/v1/Usuarios/{rut}/imagen")
    suspend fun getImageByRut(@Path("rut") rut: String): Response<ResponseBody>

    @PUT("api/v1/Usuarios/{rut}/nombre")
    suspend fun updateNombre(
        @Path("rut") rut: String,
        @Body body: Map<String, String>
    ): Response<UsuarioResp>
}