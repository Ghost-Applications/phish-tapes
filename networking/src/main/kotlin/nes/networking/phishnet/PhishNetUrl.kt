package nes.networking.phishnet

import nes.networking.NetworkingModule
import okhttp3.HttpUrl

data class PhishNetUrl(val httpUrl: HttpUrl = NetworkingModule.PHISH_NET_API_URL)
