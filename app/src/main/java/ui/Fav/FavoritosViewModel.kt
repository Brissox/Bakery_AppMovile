package ui.Fav


import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.prueba.model.Producto

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<Producto>()
    val favoritos: List<Producto> get() = _favoritos

    fun toggleFavorito(producto: Producto) {
        if (_favoritos.contains(producto)) {
            _favoritos.remove(producto)
        } else {
            _favoritos.add(producto)
        }
    }

    fun esFavorito(producto: Producto): Boolean {
        return _favoritos.contains(producto)
    }
}
