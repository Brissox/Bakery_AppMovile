package com.example.prueba.ui.profile

import Data.Remote.dto.UsuarioResp
import Data.model.usuario
import Data.repository.FirebaseAuthDataSource
import Data.repository.UsuarioRepository
import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.media.MediaRepository
import com.example.prueba.data.media.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class ProfileUiState(
    val uid: String? = null,
    val email: String? = null,
    val run: String = "",
    val dv: String = "",
    val nombre: String? = null,
    val correo: String? = null,
    val usuario: String? = null,
    val telefono: String? = null,
    val editingNombre: String = "",
    val imageBytes: ByteArray? = null,
    val lastSavedPhoto: Uri? = null,
    val loading: Boolean = false,
    val msg: String? = null,
    val error: String? = null
)

class ProfileViewModel(
    private val authRepo: FirebaseAuthDataSource,
    private val mediaRepo: MediaRepository,
    private val userRepo: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui

    init {
        val u = authRepo.currentUser()
        _ui.update { it.copy(uid = u?.uid, email = u?.email) }
        u?.uid?.let { cargarDesdeBackend(it) }
    }

    private fun cargarDesdeBackend(uid: String) = viewModelScope.launch {
        _ui.update { it.copy(loading = true, msg = null, error = null) }
        try {
            val resp: UsuarioResp? = userRepo.cargarPorFirebase(uid)
            if (resp == null) {
                _ui.update { it.copy(loading = false, error = "Usuario no encontrado en backend") }
                return@launch
            }
            val bytes: ByteArray? = resp.imagen?.let {
                try { Base64.decode(it, Base64.DEFAULT) } catch (_: Exception) { null }
            }

            _ui.update {
                it.copy(
                    loading = false,
                    usuario = resp.usuario,
                    correo = resp.correo,
                    telefono = resp.telefono.toString(),
                    run =  "${resp.run}-${resp.dv}",
                    nombre = resp.nombre,
                    editingNombre = resp.nombre,
                    imageBytes = bytes
                )
            }
        } catch (e: Exception) {
            _ui.update { it.copy(loading = false, error = e.message) }
        }
    }

    fun onNombreEdit(v: String) = _ui.update { it.copy(editingNombre = v) }

    fun guardarNombre() {
        // De momento sólo actualizamos el estado local
        _ui.update { it.copy(nombre = it.editingNombre, msg = "Nombre actualizado (local)") }
    }

    fun setLastSavedPhoto(uri: Uri?) {
        _ui.update { it.copy(lastSavedPhoto = uri) }
    }

    fun createDestinationUriForCurrentUser(context: Context): Uri? {
        val uid = _ui.value.uid ?: return null
        return mediaRepo.createImageUriForUser(context, uid)
    }

    fun subirImagenDesdeUri(context: Context, uri: Uri) = viewModelScope.launch {
        val rut = _ui.value.run ?: return@launch
        val uid = _ui.value.uid ?: return@launch

        val file = uriToTempFile(context, uri)
        _ui.update { it.copy(loading = true, msg = null, error = null) }
        try {
            // el repo devuelve Boolean; backend responde "Usuario actualizado" (texto)
            userRepo.subirImagen(rut, uid, file)
            // tras subir, volver a cargar el usuario para obtener la imagen actualizada en Base64
            cargarDesdeBackend(uid)
            _ui.update { it.copy(msg = "Imagen actualizada") }
        } catch (e: Exception) {
            android.util.Log.e("ProfileVM", "Error al subir imagen", e)
            _ui.update { it.copy(loading = false, error = "Error al subir imagen: ${e.message}") }
        } finally {
            file.delete()
        }
    }

    fun clearMsg() = _ui.update { it.copy(msg = null) }

    private fun uriToTempFile(context: Context, uri: Uri): File {
        // Comprime y limita (1024x1024, ~700 KB)
        return Data.media.ImageCompressor
            .compressToTempFile(context, uri)
    }
}
