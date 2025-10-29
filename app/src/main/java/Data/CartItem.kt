package Data

import com.example.prueba.model.Producto


data class CartItem(
    val producto: Producto,
    var cantidad: Int
)
