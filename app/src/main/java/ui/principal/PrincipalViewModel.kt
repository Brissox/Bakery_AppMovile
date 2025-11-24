package com.example.prueba.ui.principal


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import Data.model.Productos
import Data.repository.ProductoRepository
import Data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

data class PrincipalUiState(
    val nombre: String = "Desconocido",
    val email: String? = "usuario@demo.com",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedOut: Boolean = false
)

class PrincipalViewModel : ViewModel() {

    private val repository = ProductoRepository()
    private val usuarioRepository = UsuarioRepository()


    // ---------- Estado general ----------
    private val _ui = MutableStateFlow(PrincipalUiState())
    val ui: StateFlow<PrincipalUiState> = _ui.asStateFlow()

    // ---------- Fuente y filtros ----------
    private val _fuente = MutableStateFlow<List<Productos>>(emptyList())

    private val _categorias = MutableStateFlow<List<String>>(listOf("Todos"))
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    private val _categoriaSel = MutableStateFlow("Todos")
    val categoriaSel: StateFlow<String> = _categoriaSel.asStateFlow()

    private val _productosFiltrados = MutableStateFlow<List<Productos>>(emptyList())
    val productosFiltrados: StateFlow<List<Productos>> = _productosFiltrados.asStateFlow()

    init {
        val user = FirebaseAuth.getInstance().currentUser
        val uidFb = user?.uid
        val nombreMostrar = user?.displayName ?: "Usuario"

        _ui.value = _ui.value.copy(
            email = user?.email ?: "usuario desconocido",
            nombre = nombreMostrar
        )

        if (uidFb != null) {
            cargarDatosUsuario(uidFb) // <--- Nueva función para traer datos del usuario
        }
        cargarProductos()
    }

    // ---------- Acciones ----------
    fun setCategoria(cat: String) {
        _categoriaSel.value = cat
        aplicarFiltro()
    }

    /** Carga/recarga la grilla (desde el Backend). */
    fun cargarProductos() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, error = null)
            try {
                // Llamada al repositorio real
                val lista = repository.getProducto()
                _fuente.value = lista
                
                // Actualizar categorías basadas en lo que llega
                // Aseguramos que "Todos" siempre esté al principio
                val cats = listOf("Todos") + lista.mapNotNull { it.categoria }.distinct()
                _categorias.value = cats

                aplicarFiltro()
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(error = e.message ?: "Error al cargar productos")
                e.printStackTrace()
            } finally {
                _ui.value = _ui.value.copy(loading = false)
            }
        }
    }



    fun cargarDatosUsuario(uid: String) {
        viewModelScope.launch {
            try {
                // Suponiendo que agregaste este método en tu repositorio como te expliqué antes
                val respuesta = usuarioRepository.buscarPorFirebase(uid)
                // Aquí manejas la respuesta (ej: guardar datos extra del usuario en _ui)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** Reset al tocar Inicio: categoría base + recarga. */
    fun refreshHome() {
        _categoriaSel.value = "Todos"
        cargarProductos()
    }

    fun logout() {
        _ui.value = _ui.value.copy(loading = true)
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = false, loggedOut = true)
        }
    }

    // ---------- Helper ----------
    private fun aplicarFiltro() {
        val cat = _categoriaSel.value
        val source = _fuente.value
        _productosFiltrados.value = if (cat == "Todos") {
            source
        } else {
            source.filter { it.categoria == cat }
        }
    }
}

