package nes.networking.integration

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import nes.networking.phishin.PhishInModule
import nes.networking.phishin.PhishInUrl
import nes.networking.phishnet.PhishNetModule
import nes.networking.phishnet.PhishNetUrl
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module(includes = [PhishNetModule::class, PhishInModule::class])
interface IntegrationTestModule {
    companion object {

        @Provides
        fun providePhishNetUrl(): PhishNetUrl = PhishNetUrl()

        @Provides
        fun providePhishInUrl(): PhishInUrl = PhishInUrl()

        @Provides
        fun providesCache(): Cache? = null

        @Provides
        @IntoSet
        fun providesInterceptors(): Interceptor = HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
    }
}
