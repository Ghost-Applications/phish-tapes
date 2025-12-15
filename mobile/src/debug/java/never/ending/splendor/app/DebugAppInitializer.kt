package never.ending.splendor.app

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nes.app.AppInitializer
import timber.log.Timber
import javax.inject.Inject

/** Debug specific initialization things. */
class DebugAppInitializer @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : AppInitializer {
    override fun invoke() {
        Timber.plant(Timber.DebugTree())
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DebugAppInitializerModule {
    @Binds
    abstract fun providesAppInitializer(debugAppInitializer: DebugAppInitializer): AppInitializer
}

