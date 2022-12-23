package never.ending.splendor.networking.phishnet

import dev.forkhandles.result4k.valueOrNull
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import nes.networking.phishnet.PHISH_NET_URL_TAG
import nes.networking.phishnet.PhishNetRepository
import never.ending.splendor.networking.phishin.model.reviews
import never.ending.splendor.networking.phishin.model.setlist
import okhttp3.HttpUrl
import org.junit.Test
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import kotlin.test.assertNotNull

class PhishNetRepositoryTest: DIAware {

    override val di = DI.lazy {
        bind<HttpUrl>(tag = PHISH_NET_URL_TAG) with singleton {
            instance<MockWebServer>().url("/")
        }
        bind<MockWebServer>() with singleton { MockWebServer() }
    }

    private val mockWebServer: MockWebServer by instance()
    private val classUnderTest: PhishNetRepository by instance()

    @Test
    fun `should get setlists`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(
            setlist.buffer
        ))

        classUnderTest.setlist("2020-02-22").run {
            assertNotNull(valueOrNull())
        }
    }

    @Test
    fun `should get reviews`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setBody(
            reviews.buffer
        ))

        classUnderTest.reviews("1560881138").run {
            assertNotNull(valueOrNull())
        }
    }
}
