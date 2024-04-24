package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl

import io.ktor.client.*

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient