package org.mobilenativefoundation.storex.paging.impl

class Injector<T : Any> {
    private var instance: T? = null

    fun inject(instance: T) {
        this.instance = instance
    }

    fun require(): T {
        return get()!!
    }

    fun get(): T? {
        return instance
    }
}
