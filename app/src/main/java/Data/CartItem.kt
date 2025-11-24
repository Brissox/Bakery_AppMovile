package Data

import Data.model.Productos


data class CartItem(
    val producto: Productos,
    var cantidad: Int
)
