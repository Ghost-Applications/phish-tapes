package nes.networking.phishnet

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.runBlocking
import nes.networking.TestModule
import nes.networking.reviews
import nes.networking.setlist
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.test.assertNotNull

class PhishNetRepositoryTest {

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var classUnderTest: PhishNetRepository

    @BeforeEach
    fun init() {
        DaggerPhishNetTestDeps.builder()
            .phishNetUrl(PhishNetUrl(mockWebServer.url("/")))
            .build()
            .inject(this)
    }

    @Test
    fun `should get setlists`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(setlist.readUtf8()))

        classUnderTest.setlist("2020-02-22").run {
            assertNotNull(getOrNull(), message = leftOrNull()?.let { "message = ${it.message}, statcktrace = ${it.stackTrace.joinToString()}" })
        }
    }

    @Test
    fun `should get reviews`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(reviews.readUtf8()))

        classUnderTest.reviews("1560881138").run {
            assertNotNull(getOrNull(), message = leftOrNull()?.message)
        }
    }
}

@Singleton
@Component(modules = [TestModule::class, PhishNetModule::class])
interface PhishNetTestDeps {
    fun inject(test: PhishNetRepositoryTest)

    @Component.Builder
    interface Builder {
        @BindsInstance fun phishNetUrl(url: PhishNetUrl): Builder
        fun build(): PhishNetTestDeps
    }
}

