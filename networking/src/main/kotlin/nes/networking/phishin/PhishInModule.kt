package nes.networking.phishin

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import nes.networking.Config
import nes.networking.NetworkingModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class PhishIn

@Module(includes = [NetworkingModule::class])
interface PhishInModule {
    companion object {
        @Singleton
        @Provides
        fun providePhishInRepository(phishInService: PhishInService) = PhishInRepository(phishInService)

        @Singleton
        @Provides
        fun providePhishInService(
            @PhishIn retrofit: Retrofit
        ): PhishInService = retrofit.create(PhishInService::class.java)

        @Singleton
        @Provides
        @PhishIn
        fun providesPhishInRetrofit(
            url: PhishInUrl,
            okHttpClient: OkHttpClient,
            json: Json,
            phishInApiKey: PhishinApiKey
        ): Retrofit = Retrofit.Builder()
            .client(
                okHttpClient
                    .newBuilder()
                    .addInterceptor(PhishinAuthInterceptor(phishInApiKey))
                    .build()
            )
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=UTF8".toMediaType())
            )
            .baseUrl(url.httpUrl)
            .build()

        @Singleton
        @Provides
        fun providesPhishInApiKey() = PhishinApiKey(Config.PHISH_IN_API_KEY)
    }
}
