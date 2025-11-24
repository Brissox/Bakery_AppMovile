package Data.repository

import Data.Remote.RetrofitInstance1
import Data.model.Productos
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement

class ProductoRepository {
    suspend fun getProducto(): List<Productos> {
            // 1. Obtenemos la respuesta completa (la caja grande)
            val respuesta = RetrofitInstance1.api.getProducto()

            // 2. Sacamos la lista que está dentro de _embedded
            return respuesta._embedded.productoList

    }

}
