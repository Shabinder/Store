package org.mobilenativefoundation.storex.paging


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import kotlin.math.max


typealias Id = String
typealias K = Timeline.GetFeedRequest
typealias V = Timeline.Post
typealias E = Timeline.Error
typealias SPV = PagingSource.LoadResult.Data<Id, K, V, E>
typealias SIV = StoreX.Paging.Data.Item<Id, V>

object Timeline {

    const val PLACEHOLDER_TAG = "PLACEHOLDER"

    data class GetFeedRequest(
        val cursor: String,
        val size: Int,
        val headers: MutableMap<String, String> = mutableMapOf()
    )

    data class Post(
        override val id: Id
    ) : Identifiable<Id>

    data class Feed(
        val posts: List<Post>,
        val postsBefore: Int,
        val postsAfter: Int,
        val nextCursor: String?
    )

    sealed interface Error {
        data class Exception(
            val throwable: Throwable,
            val origin: StoreX.Paging.DataSource
        ) : Error
    }

    class AuthMiddleware(private val authTokenProvider: () -> String) : Middleware<GetFeedRequest> {
        override suspend fun apply(
            params: PagingSource.LoadParams<GetFeedRequest>,
            next: suspend (params: PagingSource.LoadParams<GetFeedRequest>) -> PagingSource.LoadParams<GetFeedRequest>
        ): PagingSource.LoadParams<GetFeedRequest> {
            TODO("Not yet implemented")
        }
    }

    object Backend {
        interface PostService {
            suspend fun get(key: K): Post?
            suspend fun update(key: K, value: Post)
        }


        interface FeedService {
            suspend fun get(request: K): Feed
        }

        class FakePostService(
            private val posts: MutableMap<K, Post>,
            private val error: StateFlow<Throwable?>
        ) : PostService {
            override suspend fun get(key: K): Post? {
                error.value?.let { throw it }

                return posts[key]
            }

            override suspend fun update(key: K, value: Post) {
                error.value?.let { throw it }

                posts[key] = value
            }
        }

        class FakeFeedService(
            private val posts: List<Post>,
            private val error: StateFlow<Throwable?>,
            private val incrementTriesFor: (request: K) -> Unit,
            private val setHeaders: (request: K) -> Unit
        ) : FeedService {
            override suspend fun get(request: K): Feed = try {
                setHeaders(request)


                error.value?.let {
                    incrementTriesFor(request)
                    throw it
                }

                val startIndexInclusive = posts.indexOfFirst { it.id == request.cursor }
                val endIndexExclusive = startIndexInclusive + request.size
                val feedPosts = posts.subList(startIndexInclusive, endIndexExclusive)
                Feed(
                    posts = feedPosts,
                    postsBefore = startIndexInclusive,
                    postsAfter = posts.size - endIndexExclusive + 1,
                    nextCursor = posts[endIndexExclusive].id
                )
            } catch (error: Throwable) {
                throw error
            }
        }

        data class Event(
            val name: String,
            val message: String
        )


        class Server {
            private val posts = mutableMapOf<String, Post>()
            private val error = MutableStateFlow<Throwable?>(null)
            private val attempts: MutableMap<K, Int> = mutableMapOf()
            private val logs = mutableListOf<Event>()
            private val headers: MutableMap<K, MutableMap<String, String>> = mutableMapOf()

            init {
                (1..200).map { Post(it.toString()) }
                    .forEach { this.posts[it.toString()] = it }
            }

            val feedService: FeedService = FakeFeedService(
                posts = posts.values.toList(),
                error = error,
                incrementTriesFor = { key ->
                    if (key !in attempts) {
                        attempts[key] = 0
                    }

                    attempts[key] = attempts[key]!! + 1
                },
                setHeaders = { key ->
                    if (key !in headers) {
                        headers[key] = key.headers
                    } else {
                        headers[key]?.putAll(key.headers)
                    }
                })

            fun failWith(error: Throwable) {
                this.error.value = error
            }

            fun clearError() {
                this.error.value = null
            }

            fun countAttemptsFor(request: K): Int {
                val attempts = this.attempts[request] ?: 0
                val retries = attempts - 1
                return max(retries, 0)
            }

            fun getHeadersFor(request: K): Map<String, String> {
                return this.headers[request] ?: mapOf()
            }

            fun log(name: String, message: String) {
                logs.add(Event(name, message))
            }

            fun getLogs() = logs
        }

    }

    object StoreFactory {

        object Feed {
            fun create(
                feedService: Backend.FeedService
            ): Store<K, SPV> {
                return StoreBuilder.from<K, SPV>(
                    fetcher = Fetcher.of { request: GetFeedRequest ->
                        val response = feedService.get(request)
                        val items = response.posts.map { StoreX.Paging.Data.Item(it) }
                        PagingSource.LoadResult.Data(
                            items = items,
                            key = request,
                            nextOffset = response.nextCursor?.let { request.copy(cursor = it) },
                            itemsBefore = response.postsBefore,
                            itemsAfter = response.postsAfter,
                            origin = StoreX.Paging.DataSource.NETWORK,
                            extras = mapOf()
                        )
                    }
                ).build()
            }
        }
    }
}