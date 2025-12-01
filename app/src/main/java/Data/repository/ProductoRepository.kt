package Data.repository

import Data.Remote.RetrofitInstance1
import Data.model.Productos
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement

class ProductoRepository {
    suspend fun getProducto(): List<Productos> {
            val respuesta = RetrofitInstance1.api.getProducto()

            return respuesta._embedded.productoList

    }

}
