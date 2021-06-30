package `in`.vikram.nytimes.data.remote

import `in`.vikram.nytimes.BuildConfig
import `in`.vikram.nytimes.data.remote.responses.GetNewsResults
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {


    companion object Factory {

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        var gson = GsonBuilder()
            .setLenient()
            .create()

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    /****************** RELATED TO OTP ***********************/


    @GET(Endpoints.GET_SEARCH_RESULTS)
    suspend fun getSearchResults(@Query("q") query : String, @Query("page") page : Int): Response<GetNewsResults>


}