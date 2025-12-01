package Data.Remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance1 {

    private const val API_KEY = "123456789ABCDEF"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("X-API-KEY", API_KEY)
            
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    val api: ApiBackendService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8084/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiBackendService::class.java)
    }

    val apip: ApiBackendService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8085/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiBackendService::class.java)
    }
    val apiu: ApiBackendService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiBackendService::class.java)
    }
    val apix: ApiBackendService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8086/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiBackendService::class.java)
    }
}