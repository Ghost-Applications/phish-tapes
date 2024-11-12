package nes.networking.phishin

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.runBlocking
import nes.networking.TestModule
import nes.networking.showJson
import nes.networking.showsJson
import nes.networking.yearsJson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.test.Test
import kotlin.test.assertNotNull

class PhishInRepositoryTest {

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var classUnderTest: PhishInRepository

    @BeforeEach
    fun init() {
        DaggerPhishInRepositoryTestDeps.builder()
            .phishInUrl(PhishInUrl(mockWebServer.url("/")))
            .build()
            .inject(this)
    }

    @Test
    fun years() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(yearsJson.readUtf8()))
        classUnderTest.years().run {
            assertNotNull(getOrNull(), message = leftOrNull()?.message)
        }
    }

    @Test
    fun show() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(showJson.readUtf8()))
        classUnderTest.show("12345").run {
            assertNotNull(getOrNull(), message = leftOrNull()?.message)
        }
    }

    @Test
    fun shows() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(showsJson.readUtf8()))
        classUnderTest.shows("2024").run {
            assertNotNull(getOrNull(), message = leftOrNull()?.message)
        }
    }
}

@Singleton
@Component(modules = [TestModule::class, PhishInModule::class])
interface PhishInRepositoryTestDeps {
    fun inject(test: PhishInRepositoryTest)

    @Component.Builder
    interface Builder {
        @BindsInstance fun phishInUrl(url: PhishInUrl): Builder
        fun build(): PhishInRepositoryTestDeps
    }
}
