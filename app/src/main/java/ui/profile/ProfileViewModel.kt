package com.example.prueba.ui.profile

import Data.Remote.dto.UsuarioResp
import Data.repository.UsuarioRepository
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.media.MediaRepository
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UiState con los campos de UsuarioResp
data class ProfileUiState(
    val uid: String? = null,
    val perfilBackend: UsuarioResp? = null, // Aquí guardaremos todos los datos del backend
    val lastSavedPhoto: Uri? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val authRepo: FirebaseAuthDataSource,
    private val mediaRepo: MediaRepository,
    // Agregamos el repositorio de usuarios para llamar al backend
    private val userRepo: UsuarioRepository = UsuarioRepository() 
) : ViewModel() {

    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui

    init {
        cargarPerfil()
    }

    private fun cargarPerfil() = viewModelScope.launch {
        _ui.update { it.copy(loading = true, error = null) }
        try {
            // 1. Obtenemos el usuario actual de Firebase
            val fbUser = authRepo.currentUser()
            if (fbUser == null) {
                _ui.update { it.copy(loading = false, error = "No hay sesión activa") }
                return@launch
            }

            val uid = fbUser.uid
            _ui.update { it.copy(uid = uid) }

            // 2. Pedimos los datos al Backend usando el UID
            // Asumiendo que tienes una función 'obtenerUsuarioPorFirebase' o similar en tu Repo
            val respuestaBackend = userRepo.obtenerUsuarioPorFirebase(uid)

            if (respuestaBackend != null) {
                _ui.update { it.copy(perfilBackend = respuestaBackend, loading = false) }
            } else {
                _ui.update { it.copy(loading = false, error = "Perfil no encontrado en backend") }
            }

        } catch (e: Exception) {
            _ui.update { it.copy(loading = false, error = "Error cargando perfil: ${e.message}") }
        }
    }

    fun setLastSavedPhoto(uri: Uri?) {
        _ui.update { it.copy(lastSavedPhoto = uri) }
    }

    fun setError(message: String?) {
        _ui.update { it.copy(error = message) }
    }

    fun createDestinationUriForCurrentUser(context: android.content.Context): Uri? {
        val uid = _ui.value.uid ?: return null
        return mediaRepo.createImageUriForUser(context, uid)
    }
}