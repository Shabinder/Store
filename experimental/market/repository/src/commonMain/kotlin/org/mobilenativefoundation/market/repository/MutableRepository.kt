package org.mobilenativefoundation.market.repository

interface MutableRepository<K : Any, D : Any, E : Any> : Repository<K, D, E>, Write<K, D, E>