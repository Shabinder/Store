package monster.scoop.xplat.foundation.networking.api

interface NetworkingClient {

    suspend fun getStories(query: GetStoriesQuery): GetStoriesQuery.Data?
    suspend fun getStory(query: GetStoryQuery): GetStoryQuery.Data?
}