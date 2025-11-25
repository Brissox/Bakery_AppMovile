package Data.repository

import Data.Remote.ApiBackendService
import Data.Remote.RetrofitClient
import Data.Remote.dto.UsuarioDto
import Data.Remote.dto.UsuarioResp

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody

import retrofit2.Response
import retrofit2.HttpException
import retrofit2.*

import java.io.File

class UsuarioRepository(
    private val api: ApiBackendService =
        RetrofitClient.retrofitBackend.create(ApiBackendService::class.java)
) {

    suspend fun crearUsuario(dto: UsuarioDto): Boolean {
        val r = api.crearUsuario(dto)
        return r.isSuccessful
    }

    suspend fun subirImagen(run: String, idFirebase: String, file: File): Boolean {
        val idPart = idFirebase.toRequestBody("text/plain".toMediaTypeOrNull())
        val imgPart = MultipartBody.Part.createFormData(
            name = "imagen",
            filename = file.name,
            body = file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        val r = api.actualizarUsuarioConImagen(run, idPart, imgPart)
        if (!r.isSuccessful) throw HttpException(r)
        return true
    }

    suspend fun buscarPorFirebase(uid: String): UsuarioResp? =
        api.getByFirebase(uid).body()

    suspend fun actualizarNombre(rut: String, nuevoNombre: String): UsuarioResp? {
        val r = api.updateNombre(rut, mapOf("nombre" to nuevoNombre))
        if (!r.isSuccessful) throw HttpException(r)
        return r.body()
    }

    suspend fun cargarPorFirebase(uid: String): UsuarioResp? {
        val r = api.getByFirebase(uid)
        if (!r.isSuccessful) throw HttpException(r)
        return r.body()
    }

    suspend fun obtenerUsuarioPorFirebase(idFirebase: String): UsuarioResp? {
        val res = api.getByFirebase(idFirebase)
        return if (res.isSuccessful) res.body() else null
    }
}