package monster.scoop.xplat.feat.homeTab.api


sealed interface HomeTabStory {
    data class Loaded(
        val id: Int,
        val title: String,
        val imageUrl: String?
    ) : HomeTabStory

    data object Placeholder : HomeTabStory
}