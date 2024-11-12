package nes.networking.phishin

import nes.networking.NetworkingModule
import okhttp3.HttpUrl

data class PhishInUrl(val httpUrl: HttpUrl = NetworkingModule.PHISHIN_API_URL)
