package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.CoroutineScope

interface JobCoordinator {
    fun launch(key: Any, block: suspend CoroutineScope.() -> Unit)
    fun launchIfNotActive(key: Any, block: suspend CoroutineScope.() -> Unit)
    fun cancel(key: Any)
    fun cancelAll()
}