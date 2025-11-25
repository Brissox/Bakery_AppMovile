package Data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.prueba.ui.carrito.CartItem

class CartPreferences(context: Context) {

    private val shared = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveCart(items: List<CartItem>) {
        val json = gson.toJson(items)
        shared.edit().putString("cart_items", json).apply()
    }

    fun getCart(): List<CartItem> {
        val json = shared.getString("cart_items", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearCart() {
        shared.edit().remove("cart_items").apply()
    }
}
