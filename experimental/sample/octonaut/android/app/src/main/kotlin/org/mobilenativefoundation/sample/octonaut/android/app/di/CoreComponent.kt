package org.mobilenativefoundation.sample.octonaut.android.app.di

import com.apollographql.apollo3.ApolloClient
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.mobilenativefoundation.market.combineReducers
import org.mobilenativefoundation.market.impl.MarketActionFactory
import org.mobilenativefoundation.market.impl.RealMarketSupplier
import org.mobilenativefoundation.market.impl.RealScheduledMarketSupplier
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.OctonautPresenterFactory
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.OctonautScreenFactory
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.OctonautUiFactory
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.ScreenFactory
import org.mobilenativefoundation.sample.octonaut.android.app.market.*
import org.mobilenativefoundation.sample.octonaut.android.app.market.reducers.*
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.*
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.FeedStore
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.FeedSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.impl.FeedStoreFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsStore
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.impl.NotificationsStoreFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api.RepositoryStore
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api.RepositorySupplier
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.impl.RepositoryStoreFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.CurrentUserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserStore
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.impl.UserStoreFactory
import org.mobilenativefoundation.sample.octonaut.xplat.feat.*
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl.*
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.HomeTabUi
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.HomeTabWarehouse
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.HomeTabWarehouseFactory
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.RealHomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api.NotificationsTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl.UserProfileScreenUi
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.di.api.UserScope
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.*
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl.Env
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl.RealNetworkingClient
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl.httpClient
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.webview.WebViewUrlStateHolder

@UserScope
@Component
abstract class CoreComponent : NetworkingComponent {

    abstract val screenFactory: ScreenFactory
    abstract val presenterFactory: Presenter.Factory
    abstract val uiFactory: Ui.Factory
    abstract val market: OctonautMarket
    abstract val mutableMarket: MutableOctonautMarket
    abstract val marketDispatcher: OctonautMarketDispatcher
    abstract val coroutineDispatcher: CoroutineDispatcher
    abstract val currentUserSupplier: CurrentUserSupplier
    abstract val scheduledNotificationsSupplier: NotificationsSupplier
    abstract val feedSupplier: FeedSupplier
    abstract val webViewUrlStateHolder: WebViewUrlStateHolder

    @UserScope
    @Provides
    fun bindScreenFactory(impl: OctonautScreenFactory): ScreenFactory = impl

    @UserScope
    @Provides
    fun bindPresenterFactory(impl: OctonautPresenterFactory): Presenter.Factory = impl

    @UserScope
    @Provides
    fun bindUiFactory(impl: OctonautUiFactory): Ui.Factory = impl

    @UserScope
    @Provides
    fun provideMutableMarket(): MutableOctonautMarket = RealMutableOctonautMarket()

    @UserScope
    @Provides
    fun provideMarket(mutableMarket: MutableOctonautMarket): OctonautMarket = mutableMarket

    @Provides
    fun provideRootReducer(): RootReducer {
        return combineReducers(
            currentUserReducer,
            notificationsReducer,
            feedReducer,
            usersReducer,
            repositoriesReducer
        )
    }

    @UserScope
    @Provides
    fun provideMarketDispatcher(mutableMarket: MutableOctonautMarket, rootReducer: RootReducer): OctonautMarketDispatcher {
        return RealOctonautMarketDispatcher(mutableMarket, rootReducer)
    }

    @UserScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default


    @Provides
    @UserScope
    fun bindNetworkingClient(impl: RealNetworkingClient): NetworkingClient = impl

    @Provides
    @UserScope
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
        .serverUrl("https://api.github.com/graphql")
        .addHttpHeader("Authorization", "bearer ${Env.X_PAT}")
        .build()

    @Provides
    @UserScope
    fun provideUserStore(userStoreFactory: UserStoreFactory): UserStore {
        return userStoreFactory.create()
    }

    @Provides
    fun provideNotificationsStoreFactory(networkingClient: NetworkingClient): NotificationsStoreFactory {
        return NotificationsStoreFactory(networkingClient)
    }

    @Provides
    @UserScope
    fun provideRepositoryStore(repositoryStoreFactory: RepositoryStoreFactory): RepositoryStore {
        return repositoryStoreFactory.create()
    }


    @Provides
    @UserScope
    fun provideNotificationsStore(notificationsStoreFactory: NotificationsStoreFactory): NotificationsStore {
        return notificationsStoreFactory.create()
    }

    @Provides
    fun provideFeedStoreFactory(networkingClient: NetworkingClient): FeedStoreFactory {
        return FeedStoreFactory(networkingClient)
    }

    @Provides
    @UserScope
    fun provideFeedStore(notificationsStoreFactory: FeedStoreFactory): FeedStore {
        return notificationsStoreFactory.create()
    }


    @Provides
    @UserScope
    fun provideUserSupplier(
        coroutineDispatcher: CoroutineDispatcher,
        userStore: UserStore,
        marketDispatcher: OctonautMarketDispatcher,
    ): UserSupplier {

        val marketActionFactory = MarketActionFactory<GetUserQuery.User, OctonautMarketAction> { storeOutput ->
            OctonautMarketAction.AddUser(storeOutput)
        }

        return RealMarketSupplier(
            coroutineDispatcher,
            userStore,
            marketDispatcher,
            marketActionFactory
        )
    }

    @Provides
    @UserScope
    fun provideRepositorySupplier(
        coroutineDispatcher: CoroutineDispatcher,
        repositoryStore: RepositoryStore,
        marketDispatcher: OctonautMarketDispatcher,
    ): RepositorySupplier {

        val marketActionFactory =
            MarketActionFactory<GetRepositoryQuery.Repository, OctonautMarketAction> { storeOutput ->
                OctonautMarketAction.AddRepository(storeOutput)
            }

        return RealMarketSupplier(
            coroutineDispatcher,
            repositoryStore,
            marketDispatcher,
            marketActionFactory
        )
    }

    @Provides
    @UserScope
    fun provideCurrentUserSupplier(
        coroutineDispatcher: CoroutineDispatcher,
        userStore: UserStore,
        marketDispatcher: OctonautMarketDispatcher,
    ): CurrentUserSupplier {

        val marketActionFactory = MarketActionFactory<GetUserQuery.User, OctonautMarketAction> { storeOutput ->
            OctonautMarketAction.UpdateCurrentUser(storeOutput)
        }

        return RealMarketSupplier(
            coroutineDispatcher,
            userStore,
            marketDispatcher,
            marketActionFactory
        )
    }

    @Provides
    @UserScope
    fun provideFeedSupplier(
        coroutineDispatcher: CoroutineDispatcher,
        userStore: FeedStore,
        marketDispatcher: OctonautMarketDispatcher,
    ): FeedSupplier {

        val marketActionFactory = MarketActionFactory<Feed, OctonautMarketAction> { storeOutput ->
            println("STORE OUTPUT = $storeOutput")
            OctonautMarketAction.UpdateFeed(storeOutput)
        }

        return RealMarketSupplier(
            coroutineDispatcher,
            userStore,
            marketDispatcher,
            marketActionFactory
        )
    }

    @Provides
    @UserScope
    fun provideNotificationsSupplier(
        coroutineDispatcher: CoroutineDispatcher,
        notificationsStore: NotificationsStore,
        marketDispatcher: OctonautMarketDispatcher,
    ): NotificationsSupplier {
        val marketActionFactory =
            MarketActionFactory<ListNotificationsResponse, OctonautMarketAction> { storeOutput ->
                OctonautMarketAction.UpdateNotifications(storeOutput)
            }

        return RealScheduledMarketSupplier(
            coroutineDispatcher,
            notificationsStore,
            marketDispatcher,
            marketActionFactory
        )
    }

    @Provides
    @UserScope
    fun provideWarehouseBuilderFactory(
        coroutineDispatcher: CoroutineDispatcher,
        market: OctonautMarket
    ): WarehouseBuilderFactory {
        return RealWarehouseBuilderFactory(coroutineDispatcher, market)
    }

    @Provides
    @UserScope
    fun provideHomeTabWarehouseFactory(
        userSupplier: UserSupplier,
        warehouseBuilderFactory: WarehouseBuilderFactory
    ): HomeTabWarehouseFactory {
        return HomeTabWarehouseFactory(userSupplier, warehouseBuilderFactory)
    }

    @Provides
    @UserScope
    fun provideHomeTabWarehouse(
        homeTabWarehouseFactory: HomeTabWarehouseFactory
    ): HomeTabWarehouse {
        return homeTabWarehouseFactory.create()
    }


    @Provides
    fun provideHomeTabUi(): HomeTabUi = HomeTabUi()

    @Provides
    fun provideHomeTab(): HomeTab = RealHomeTab

    @Provides
    fun provideExploreTabWarehouseFactory(
        warehouseBuilderFactory: WarehouseBuilderFactory
    ): ExploreTabWarehouseFactory = ExploreTabWarehouseFactory(warehouseBuilderFactory)

    @Provides
    @UserScope
    fun provideExploreTabWarehouse(
        exploreTabWarehouseFactory: ExploreTabWarehouseFactory
    ): ExploreTabWarehouse {
        return exploreTabWarehouseFactory.create()
    }

    @Provides
    fun provideExploreTabPresenter(
        warehouse: ExploreTabWarehouse
    ): ExploreTabPresenter {
        return ExploreTabPresenter(warehouse)
    }

    @Provides
    fun provideExploreTabUi(): ExploreTabUi = ExploreTabUi()

    @Provides
    fun provideExploreTab(): ExploreTab = RealExploreTab

    @Provides
    fun provideHttpClient(): HttpClient = httpClient()


    @Provides
    @UserScope
    fun provideNotificationsTabWarehouseFactory(
        notificationsSupplier: NotificationsSupplier,
        warehouseBuilderFactory: WarehouseBuilderFactory
    ): NotificationsTabWarehouseFactory {
        return NotificationsTabWarehouseFactory(notificationsSupplier, warehouseBuilderFactory)
    }

    @Provides
    @UserScope
    fun provideNotificationsTabWarehouse(
        homeTabWarehouseFactory: NotificationsTabWarehouseFactory
    ): NotificationsTabWarehouse {
        return homeTabWarehouseFactory.create()
    }

    @Provides
    fun provideNotificationsTabPresenter(
        warehouse: NotificationsTabWarehouse
    ): NotificationsTabPresenter {
        return NotificationsTabPresenter(warehouse)
    }

    @Provides
    fun provideNotificationsTabUi(): NotificationsTabUi = NotificationsTabUi()

    @Provides
    fun provideNotificationsTab(): NotificationsTab = RealNotificationsTab

    @Provides
    @UserScope
    fun provideWebViewUrlStateHolder(): WebViewUrlStateHolder {
        return object : WebViewUrlStateHolder {
            override val url = MutableStateFlow<String?>(null)
        }
    }

    @Provides
    fun provideUserProfileScreenUi(): UserProfileScreenUi = UserProfileScreenUi()

    companion object
}

