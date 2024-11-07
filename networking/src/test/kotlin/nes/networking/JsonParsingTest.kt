package nes.networking

import com.google.common.truth.Truth.assertThat
import dagger.Component
import kotlinx.serialization.json.Json
import nes.networking.phishin.model.Show
import nes.networking.phishin.model.SuccessfulResponse
import nes.networking.phishin.model.YearData
import nes.networking.phishnet.model.PhishNetWrapper
import nes.networking.phishnet.model.Review
import nes.networking.phishnet.model.SetListResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.test.BeforeTest
import kotlin.test.Test

class JsonParsingTest {


    @Inject
    lateinit var json: Json

    @BeforeTest
    fun init() {
        DaggerJsonParsingTestDeps.create().inject(this)
    }

    @Test
    fun `should parse years`() {
        val result = json.decodeFromString<SuccessfulResponse<List<YearData>>>(yearsJson.readUtf8())
        assertThat(result.data).isInstanceOf(List::class.java)
    }

    @Test
    fun `should parse shows`() {
        val result = json.decodeFromString<SuccessfulResponse<List<Show>>>(showsJson.readUtf8())
        assertThat(result.data).isInstanceOf(List::class.java)
    }

    @Test
    fun `should parse show`() {
        val result = json.decodeFromString<SuccessfulResponse<Show>>(showJson.readUtf8())
        assertThat(result.data).isInstanceOf(Show::class.java)
    }

    @Test
    fun `should parse reviews`() {
        val result = json.decodeFromString<PhishNetWrapper<Review>>(reviews.readUtf8())
        assertThat(result.data).isInstanceOf(List::class.java)
    }

    @Test
    fun `should parse setlist`() {
        val result = json.decodeFromString<PhishNetWrapper<SetListResponse>>(setlist.readUtf8())
        assertThat(result.data).isInstanceOf(List::class.java)
    }
}

@Singleton
@Component(modules = [TestModule::class, NetworkingModule::class])
interface JsonParsingTestDeps {
    fun inject(test: JsonParsingTest)
}
