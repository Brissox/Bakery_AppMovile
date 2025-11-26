package Data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.example.prueba.ui.carrito.CartItem

val Context.cartDataStore by preferencesDataStore(name = "cart_store")

class CartDataStore(private val context: Context) {

    private val CART_KEY = stringPreferencesKey("cart_items")
    private val gson = Gson()

    suspend fun saveCart(items: List<CartItem>) {
        val json = gson.toJson(items)
        context.cartDataStore.edit { prefs ->
            prefs[CART_KEY] = json
        }
    }

    fun getCart(): Flow<List<CartItem>> =
        context.cartDataStore.data.map { prefs ->
            val json = prefs[CART_KEY] ?: "[]"
            val type = object : TypeToken<List<CartItem>>(){}.type
            gson.fromJson(json, type)
        }

    suspend fun clearCart() {
        context.cartDataStore.edit { prefs ->
            prefs[CART_KEY] = "[]"
        }
    }
}