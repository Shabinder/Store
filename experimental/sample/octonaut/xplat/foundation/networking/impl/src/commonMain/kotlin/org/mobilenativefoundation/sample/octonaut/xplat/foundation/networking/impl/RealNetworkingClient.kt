package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl

import com.apollographql.apollo3.ApolloClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.*

@Inject
class RealNetworkingClient(
    private val apolloClient: ApolloClient,
    private val httpClient: HttpClient,
) : NetworkingClient {
    override suspend fun getUser(query: GetUserQuery): GetUserQuery.Data? {
        val response = apolloClient.query(query).execute()
        return response.data
    }

    override suspend fun listNotifications(queryParams: ListNotificationsQueryParams): ListNotificationsResponse {


        val httpResponse = httpClient.get("https://api.github.com/notifications") {
            method = HttpMethod.Get

            url {

                if (queryParams.all) {
                    parameters.append("all", "true")
                }

                if (queryParams.participating) {
                    parameters.append("participating", "true")
                }

                queryParams.since?.let { parameters.append("since", it) }
                queryParams.before?.let { parameters.append("before", it) }

                parameters.append("page", queryParams.page.toString())
                parameters.append("per_page", queryParams.perPage.toString())
            }

            headers {
                append(HttpHeaders.Authorization, "Bearer ${Env.X_PAT_CLASSIC}")
                append(HttpHeaders.Accept, "application/vnd.github+json")
                append("X-GitHub-Api-Version", "2022-11-28")
            }
        }

        val notifications: List<Thread> = httpResponse.body()
        return ListNotificationsResponse(notifications)
    }
}