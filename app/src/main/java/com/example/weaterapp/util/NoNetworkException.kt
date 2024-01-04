package com.example.weaterapp.util

import java.io.IOException

class NoNetworkException : IOException() {
    override val message: String
        get() =
            "No network available, please check your WiFi or Data connection"
}