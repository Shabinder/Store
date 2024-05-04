package monster.scoop.xplat.domain.story.api

sealed interface StoriesError {
    data object Network : StoriesError

    sealed interface Default: StoriesError {
        data class Message(val message: String): Default
        data class Exception(val throwable: Throwable): Default
    }

    data class Server(
        val error: Throwable
    ) : StoriesError
}