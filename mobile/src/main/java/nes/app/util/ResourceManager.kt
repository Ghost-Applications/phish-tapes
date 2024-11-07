package nes.app.util

import android.content.res.Resources
import nes.app.R
import okio.buffer
import okio.source
import javax.inject.Inject

interface ResourceManager {
    suspend fun loadAboutText(): String
}

class RealResourceManager @Inject constructor(
    private val resources: Resources
): ResourceManager {
    override suspend fun loadAboutText(): String {
        return resources.openRawResource(R.raw.about).source()
            .buffer()
            .readUtf8()
    }
}