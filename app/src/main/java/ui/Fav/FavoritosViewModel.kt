package ui.Fav


import Data.model.Productos
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf


class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<Productos>()
    val favoritos: List<Productos> get() = _favoritos

    fun toggleFavorito(producto: Productos) {
        if (_favoritos.contains(producto)) {
            _favoritos.remove(producto)
        } else {
            _favoritos.add(producto)
        }
    }

    fun esFavorito(producto: Productos): Boolean {
        return _favoritos.contains(producto)
    }
}
