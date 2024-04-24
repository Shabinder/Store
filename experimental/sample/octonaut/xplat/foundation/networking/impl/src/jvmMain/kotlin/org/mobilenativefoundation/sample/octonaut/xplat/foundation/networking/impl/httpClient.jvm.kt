package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl

import io.ktor.client.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Apache5) {
    engine {
        followRedirects = true
        socketTimeout = 10_000
        connectTimeout = 10_000
        connectionRequestTimeout = 20_000
    }
}