package monster.scoop.xplat.feat.homeTab.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import monster.scoop.xplat.domain.story.api.StoriesPager
import monster.scoop.xplat.feat.homeTab.api.HomeTabStory
import org.mobilenativefoundation.market.warehouse.Warehouse
import org.mobilenativefoundation.storex.paging.StoreX


sealed interface DataLayerError
sealed interface DataLayerWarning

data class HomeTabViewData(
    val stories: List<HomeTabStory> = emptyList(),
) : Warehouse.State

sealed interface HomeTabAction : Warehouse.Action {
    data object Refresh : HomeTabAction
}

interface HomeTabDataLayer {
    val viewData: StateFlow<HomeTabViewData>
    val errors: StateFlow<List<DataLayerError>>
    val warnings: StateFlow<List<DataLayerWarning>>

    fun startPaging(anchorPosition: Flow<Int>)
    fun refreshHomeTab()
}


class RealHomeTabDataLayer(
    coroutineDispatcher: CoroutineDispatcher,
    private val pager: StoriesPager
) : HomeTabDataLayer {

    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    private val _viewData = MutableStateFlow(HomeTabViewData())

    override val viewData: StateFlow<HomeTabViewData> = _viewData.asStateFlow()

    private val _warnings = MutableStateFlow<List<DataLayerWarning>>(emptyList())
    override val warnings: StateFlow<List<DataLayerWarning>> = _warnings.asStateFlow()

    private val _errors = MutableStateFlow<List<DataLayerError>>(emptyList())
    override val errors: StateFlow<List<DataLayerError>> = _errors.asStateFlow()

    override fun startPaging(anchorPosition: Flow<Int>) {
        pager.start(anchorPosition)

        coroutineScope.launch {
            pager.pagingItems.collect { pagingItems ->
                val nextStories = pagingItems.value.map {
                    if (it.origin == StoreX.Paging.DataSource.PLACEHOLDER) {
                        HomeTabStory.Placeholder
                    } else {
                        HomeTabStory.Loaded(
                            id = it.value.id,
                            title = it.value.data.title,
                            imageUrl = it.value.data.story_thumbnails.firstOrNull()?.url
                        )
                    }
                }

                _viewData.update { HomeTabViewData(nextStories) }
            }
        }
    }

    override fun refreshHomeTab() {
        // TODO
    }
}
