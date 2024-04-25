package org.mobilenativefoundation.sample.octonaut.xplat.foundation.webview

import kotlinx.coroutines.flow.MutableStateFlow

interface WebViewUrlStateHolder {
    val url: MutableStateFlow<String?>
}