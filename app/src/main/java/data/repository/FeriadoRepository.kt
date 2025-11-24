package Data.repository

import Data.Remote.RetrofitClient
import model.Feriado

class FeriadoRepository {
    suspend fun obtenerFeriados(): List<Feriado> {
        val resp = RetrofitClient.api.getFeriados()
        return resp.data.map {
            Feriado(
                date = it.date,
                title = it.title,
                type = it.type,
                inalienable = it.inalienable,
                extra = it.extra
            )
        }
    }

}