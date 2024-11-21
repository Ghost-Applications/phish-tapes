package nes.app.ui.about

import app.cash.paparazzi.Paparazzi
import nes.app.ui.PaparazziNightTest
import nes.app.ui.PaparazziTest
import org.junit.Test

interface AboutScreenTest {

    val paparazzi: Paparazzi

    fun runTest() {
        paparazzi.snapshot {
            AboutScreen(
                AboutText(
                    """
                    # Test
                    
                    This is an about page.
                    
                    Here is a [link](http://example.com)
                """.trimIndent()
                )
            ) { }
        }
    }
}

class AboutScreenDayTest : PaparazziTest(), AboutScreenTest {
    @Test fun aboutScreen() {
        runTest()
    }
}
class AboutScreenNightTest : PaparazziNightTest(), AboutScreenTest {
    @Test fun aboutScreen() {
        runTest()
    }
}
