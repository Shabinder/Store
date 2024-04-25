package org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api

import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetRepositoryQuery
import org.mobilenativefoundation.store.store5.Store

typealias RepositoryStore = Store<GetRepositoryQuery, GetRepositoryQuery.Repository>
