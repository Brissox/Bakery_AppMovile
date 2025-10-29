package com.example.prueba.ui.carrito

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.prueba.model.Producto

data class CartItem(
    val producto: Producto,
    var cantidad: Int = 1
)

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> = _cartItems

    fun agregarProducto(producto: Producto) {
        val itemExistente = _cartItems.find { it.producto.id == producto.id }
        if (itemExistente != null) {
            itemExistente.cantidad++
        } else {
            _cartItems.add(CartItem(producto))
        }
    }

    fun eliminarProducto(producto: Producto) {
        _cartItems.removeAll { it.producto.id == producto.id }
    }

    fun increaseQuantity(producto: Producto) {
        val index = _cartItems.indexOfFirst { it.producto.id == producto.id }
        if (index != -1) {
            val item = _cartItems[index]
            _cartItems[index] = item.copy(cantidad = item.cantidad + 1)
        }
    }

    fun decreaseQuantity(producto: Producto) {
        val index = _cartItems.indexOfFirst { it.producto.id == producto.id }
        if (index != -1) {
            val item = _cartItems[index]
            if (item.cantidad > 1) {
                _cartItems[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                _cartItems.removeAt(index)
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun calcularTotal(): Int {
        return _cartItems.sumOf { it.producto.precio.filter { it.isDigit() }.toInt() * it.cantidad }
    }


}
