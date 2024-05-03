package monster.scoop.xplat.domain.story.api

sealed interface StoriesError {
    data object Network : StoriesError
    data class Server(
        val error: Throwable
    ) : StoriesError
}