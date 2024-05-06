package monster.scoop.android.app.di

import com.apollographql.apollo3.ApolloClient
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import monster.scoop.android.app.circuit.ScoopPresenterFactory
import monster.scoop.android.app.circuit.ScoopScreenFactory
import monster.scoop.android.app.circuit.ScoopUiFactory
import monster.scoop.android.app.circuit.ScreenFactory
import monster.scoop.android.app.market.MutableScoopMarket
import monster.scoop.android.app.market.RealMutableScoopMarket
import monster.scoop.android.app.market.RealScoopDispatcher
import monster.scoop.android.app.market.RootReducer
import monster.scoop.android.app.market.reducers.storiesReducer
import monster.scoop.xplat.common.market.ScoopDispatcher
import monster.scoop.xplat.domain.story.api.StoriesPager
import monster.scoop.xplat.domain.story.impl.StoriesPagerFactory
import monster.scoop.xplat.feat.homeTab.api.HomeTab
import monster.scoop.xplat.feat.homeTab.impl.HomeTabDataLayer
import monster.scoop.xplat.feat.homeTab.impl.RealHomeTab
import monster.scoop.xplat.feat.homeTab.impl.RealHomeTabDataLayer
import monster.scoop.xplat.foundation.di.Singleton
import monster.scoop.xplat.foundation.di.UserScope
import monster.scoop.xplat.foundation.networking.api.NetworkingClient
import monster.scoop.xplat.foundation.networking.impl.Env
import monster.scoop.xplat.foundation.networking.impl.RealNetworkingClient
import org.mobilenativefoundation.market.combineReducers

@UserScope
@Component
abstract class CoreComponent {

    abstract val presenterFactory: Presenter.Factory
    abstract val screenFactory: ScreenFactory
    abstract val uiFactory: Ui.Factory

    @Provides
    fun bindPresenterFactory(impl: ScoopPresenterFactory): Presenter.Factory = impl

    @Provides
    fun bindUiFactory(impl: ScoopUiFactory): Ui.Factory = impl

    @Provides
    fun bindScreenFactory(impl: ScoopScreenFactory): ScreenFactory = impl

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
        .serverUrl(Env.API_URL)
        .addHttpHeader(Env.API_KEY, Env.API_SECRET)
        .build()

    @Provides
    fun bindNetworkingClient(impl: RealNetworkingClient): NetworkingClient = impl

    @Provides
    @UserScope
    @Singleton
    fun bindScoopMarket(impl: RealMutableScoopMarket): MutableScoopMarket = impl

    @Provides
    fun provideRootReducer(): RootReducer {
        return combineReducers(
            storiesReducer
        )
    }

    @Provides
    fun bindScoopDispatcher(impl: RealScoopDispatcher): ScoopDispatcher = impl

    @Provides
    @UserScope
    @Singleton
    fun provideStoriesPager(
        factory: StoriesPagerFactory
    ): StoriesPager = factory.create()

    @Provides
    @UserScope
    @Singleton
    fun provideHomeTabDataLayer(
        coroutineDispatcher: CoroutineDispatcher,
        pager: StoriesPager
    ): HomeTabDataLayer {
        return RealHomeTabDataLayer(coroutineDispatcher, pager)
    }

    @Provides
    fun provideHomeTab(): HomeTab = RealHomeTab

    companion object
}