package never.ending.splendor.app

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val releaseModule = DI.Module("Release Module") {
    bind<AppInitializer>() with singleton { ReleaseAppInitializer(instance()) }
    bind<CrashlyticsTree>() with singleton { CrashlyticsTree(instance()) }
}

val buildSpecificModules = listOf(releaseModule)
