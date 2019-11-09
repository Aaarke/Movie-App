import android.content.Context
import com.example.mvvmbaseproject.api.ApiInterface
import com.example.mvvmbaseproject.api.TLSSocketFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.ConnectionSpec
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object RestClient {
    private var retrofitApi: Retrofit? = null
    val BASE_URL = "http://www.omdbapi.com"
    private val SOCKET_TIMEOUT = 60


    fun getApiInterface(context: Context): ApiInterface {
        if (retrofitApi == null) {
            try {
                val gson = GsonBuilder()
                    .setLenient().setPrettyPrinting()
                    .create()
                retrofitApi = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient().build())
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return retrofitApi!!.create(ApiInterface::class.java)
    }

    private fun httpClient(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 2
        httpClient.dispatcher(dispatcher)
        httpClient.addInterceptor(logging)
        httpClient.readTimeout(SOCKET_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(SOCKET_TIMEOUT.toLong(), TimeUnit.SECONDS)
        return httpClientTLS(httpClient)!!
    }


    private fun httpClientTLS(httpClient: OkHttpClient.Builder): OkHttpClient.Builder? {
        try {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                httpClient.sslSocketFactory(TLSSocketFactory())
            } else {
                val trustManager = trustManagers[0] as X509TrustManager
                httpClient.sslSocketFactory(TLSSocketFactory(), trustManager)
            }
            val cs = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

            val specs = ArrayList<ConnectionSpec>()
            specs.add(cs)
            specs.add(ConnectionSpec.COMPATIBLE_TLS)
            specs.add(ConnectionSpec.CLEARTEXT)

            httpClient.connectionSpecs(specs)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }

        return httpClient
    }


}