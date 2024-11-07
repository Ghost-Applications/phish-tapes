package nes.app.ui.about

import com.google.common.truth.Truth.assertThat
import nes.app.MainDispatcherRule
import nes.app.util.ResourceManager
import org.junit.Rule
import org.junit.Test

class AboutViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should load text`() {
        val classUnderTest = AboutViewModel(
            resourceManager = object : ResourceManager {
                override suspend fun loadAboutText(): String = "test text!"
            }
        )

        assertThat(classUnderTest.aboutText.value).isEqualTo(AboutText("test text!"))
    }
}