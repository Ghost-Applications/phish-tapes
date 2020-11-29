package never.ending.splendor.app

import timber.log.Timber

/** Release specific initialization things. */
class ReleaseAppInitializer(private val crashlyticsTree: CrashlyticsTree) : AppInitializer {
    override fun invoke() {
        Timber.plant(crashlyticsTree)
    }
}
