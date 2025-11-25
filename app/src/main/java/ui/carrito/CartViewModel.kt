package com.example.prueba.ui.carrito

import Data.CartPreferences
import Data.model.Productos
import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel

data class CartItem(
    val productos: Productos,
    var cantidad: Int = 1
)

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartPrefs = CartPreferences(application)

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> = _cartItems

    init {
        val guardado = cartPrefs.getCart()
        _cartItems.addAll(guardado)
    }

    fun agregarProducto(producto: Productos) {
        val existente = _cartItems.find { it.productos.id_producto == producto.id_producto }
        if (existente != null) {
            existente.cantidad++
        } else {
            _cartItems.add(CartItem(producto))
        }
        guardar()
    }

    fun eliminarProducto(producto: Productos) {
        _cartItems.removeAll { it.productos.id_producto == producto.id_producto }
        guardar()
    }

    fun increaseQuantity(producto: Productos) {
        val index = _cartItems.indexOfFirst { it.productos.id_producto == producto.id_producto }
        if (index != -1) {
            _cartItems[index] = _cartItems[index].copy(
                cantidad = _cartItems[index].cantidad + 1
            )
        }
        guardar()
    }

    fun decreaseQuantity(producto: Productos) {
        val index = _cartItems.indexOfFirst { it.productos.id_producto == producto.id_producto }
        if (index != -1) {
            val item = _cartItems[index]
            if (item.cantidad > 1) {
                _cartItems[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                _cartItems.removeAt(index)
            }
        }
        guardar()
    }

    fun clearCart() {
        _cartItems.clear()
        cartPrefs.clearCart()
    }

    fun calcularTotal(): Long {
        return _cartItems.sumOf { it.productos.precio * it.cantidad }
    }

    private fun guardar() {
        cartPrefs.saveCart(_cartItems.toList())
    }
}
