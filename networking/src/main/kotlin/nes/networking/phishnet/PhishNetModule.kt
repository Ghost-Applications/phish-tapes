package nes.networking.phishnet

import Config
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import nes.networking.NetworkingModule
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class PhishNet

@Module(includes = [NetworkingModule::class])
interface PhishNetModule {
    companion object {
        @Provides
        fun providesPhishNetService(
            @PhishNet retrofit: Retrofit
        ): PhishNetService = retrofit.create(PhishNetService::class.java)

        @PhishNet
        @Provides
        internal fun providePhishNetRetrofit(
            url: PhishNetUrl,
            okHttpClient: OkHttpClient,
            authInterceptor: PhishNetAuthInterceptor,
            json: Json
        ): Retrofit = Retrofit.Builder()
            .client(
                okHttpClient
                    .newBuilder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=UTF8".toMediaType())
            )
            .baseUrl(url.httpUrl)
            .build()



        @Singleton
        @Provides
        fun providesPhishNetApiKey() = PhishNetApiKey(Config.PHISH_NET_API_KEY)
    }
}
