package ui.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel : ViewModel() {
    private val _uidUsuario = MutableStateFlow<String?>(null)
    val uidUsuario: StateFlow<String?> = _uidUsuario


    private val _idUsuario = MutableStateFlow<Int?>(null)
    val idUsuario: StateFlow<Int?> = _idUsuario

    fun setUid(uid: String) {
        _uidUsuario.value = uid
    }

    fun setIdUsuario(id: Int) {
        _idUsuario.value = id

} }