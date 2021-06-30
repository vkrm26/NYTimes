package `in`.vikram.nytimes.data.remote

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Chain): Response {
        var original = chain.request()
        val url = original.url().newBuilder().addQueryParameter("api-key", "5763846de30d489aa867f0711e2b031c").build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}