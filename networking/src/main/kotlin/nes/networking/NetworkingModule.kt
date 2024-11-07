package nes.networking

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Module
interface NetworkingModule {
    companion object {
        val PHISHIN_API_URL: HttpUrl = requireNotNull("https://phish.in/".toHttpUrlOrNull())
        val PHISH_NET_API_URL: HttpUrl = requireNotNull("https://api.phish.net/v5/".toHttpUrlOrNull())

        @Singleton
        @Provides
        fun provideJson() = Json {
            ignoreUnknownKeys = true
        }

        @Singleton
        @Provides
        fun providesOkhttpClient(
            cache: Cache?,
            interceptors: Set<@JvmSuppressWildcards Interceptor>
        ) = OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(5.seconds.toJavaDuration())
            .apply { interceptors.forEach { addInterceptor(it) } }
            .build()
    }
}
