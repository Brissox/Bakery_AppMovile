package Data.Remote

import Data.Remote.dto.FeriadoResponse
import Data.model.Post
import Data.model.ProductoResponse
import Data.model.Productos
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Post>

    @GET("api/v1/Productos")
    suspend fun getProducto(): ProductoResponse

    @GET("holidays.json")
    suspend fun getFeriados(): FeriadoResponse
}