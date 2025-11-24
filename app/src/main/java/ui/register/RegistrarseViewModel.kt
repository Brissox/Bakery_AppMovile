package com.example.prueba.ui.register

import Data.Remote.dto.UsuarioDto
import Data.repository.AuthRepository
import Data.repository.UsuarioRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

// UiState actualizado con los nuevos campos requeridos
data class RegistrarseUiState(
    val run: String = "", // Usamos String para la UI (Input), se convierte a Int al enviar
    val dv: String = "",
    val usuario: String = "",
    val fechaNacimiento: String = "",
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val imagenFile: File? = null,
    val loading: Boolean = false,
    val ok: Boolean = false,
    val msg: String? = null
)

class RegistrarseViewModel(
    private val authRepo: AuthRepository = AuthRepository(),
    private val userRepo: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(RegistrarseUiState())
    val ui: StateFlow<RegistrarseUiState> = _ui

    // Funciones para actualizar el estado desde la UI
    fun onRun(v: String) = _ui.update { it.copy(run = v.filter { char -> char.isDigit() }) }

    fun onDv(v: String) = _ui.update { it.copy(dv = v.take(1)) }
    fun onUsuario(v: String) = _ui.update { it.copy(usuario = v) }

    fun onNombre(v: String) = _ui.update { it.copy(nombre = v) }

    fun onFechaNacimiento(v: String) = _ui.update { it.copy(fechaNacimiento = v) }
    fun onEmail(v: String) = _ui.update { it.copy(email = v) }
    fun onPass(v: String) = _ui.update { it.copy(password = v) }
    fun onImagenFile(f: File?) = _ui.update { it.copy(imagenFile = f) }
    fun consumeMsg() = _ui.update { it.copy(msg = null) }

    fun registrar() = viewModelScope.launch {
        _ui.update { it.copy(loading = true, ok = false, msg = null) }

        try {
            // 1) Registrar usuario en Firebase Auth
            val firebaseUser = authRepo.signUp(_ui.value.email, _ui.value.password)
                ?: throw IllegalStateException("No se pudo registrar en Firebase")
            val uid = firebaseUser.uid!!

            // 2) Enviar datos al backend (Mapeo al DTO actualizado)
            val dto = UsuarioDto(
                // CORRECCIÓN: Usamos los nombres en MAYÚSCULAS del DTO
                RUN = _ui.value.run,
                DV = _ui.value.dv,
                USUARIO = _ui.value.usuario,
                CORREO = _ui.value.email,
                CONTRASENA = _ui.value.password,
                U_ID = uid, 
                FECHA_NACIMIENTO = _ui.value.fechaNacimiento,
                NOMBRE = _ui.value.nombre
            )
            
            val ok = userRepo.crearUsuario(dto)
            if (!ok) throw IllegalStateException("Fallo al guardar usuario en backend")

            // 3) Subir imagen si existe
            _ui.value.imagenFile?.let { file ->
                userRepo.subirImagen(
                    run = _ui.value.run,
                    idFirebase = uid,
                    file = file
                )
            }

            _ui.update { it.copy(loading = false, ok = true, msg = "Registro exitoso") }

        } catch (e: Exception) {
            _ui.update {
                it.copy(
                    loading = false,
                    ok = false,
                    msg = "Error al registrar: ${e.message}"
                )
            }
        }
    }
}