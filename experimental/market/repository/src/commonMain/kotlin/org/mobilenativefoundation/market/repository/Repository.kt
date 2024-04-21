package org.mobilenativefoundation.market.repository

interface Repository<K : Any, D : Any, E : Any> :
    Stream<K, D, E>,
    Read<K, D, E>,
    Delete<K, D, E>