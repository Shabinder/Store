package org.mobilenativefoundation.sample.octonaut.android.app.di

import com.apollographql.apollo3.ApolloClient
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.mobilenativefoundation.market.impl.MarketActionFactory
import org.mobilenativefoundation.market.impl.RealMarketContributor
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.OctonautPresenterFactory
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.OctonautScreenFactory
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.OctonautUiFactory
import org.mobilenativefoundation.sample.octonaut.android.app.circuit.ScreenFactory
import org.mobilenativefoundation.sample.octonaut.android.app.market.MutableOctonautMarket
import org.mobilenativefoundation.sample.octonaut.android.app.market.RealMutableOctonautMarket
import org.mobilenativefoundation.sample.octonaut.android.app.market.RealOctonautMarketDispatcher
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.*
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserMarketContributor
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserStore
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.impl.UserStoreFactory
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.*
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.di.api.UserScope
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingComponent
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl.Env
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl.RealNetworkingClient
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
    abstract val userMarketContributor: UserMarketContributor

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

    @UserScope
    @Provides
    fun provideMarketDispatcher(mutableMarket: MutableOctonautMarket): OctonautMarketDispatcher {
        return RealOctonautMarketDispatcher(mutableMarket)
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
    @UserScope
    fun provideUserMarketContributor(
        coroutineDispatcher: CoroutineDispatcher,
        userStore: UserStore,
        marketDispatcher: OctonautMarketDispatcher,
    ): UserMarketContributor {

        val marketActionFactory = MarketActionFactory<GetUserQuery.User, OctonautMarketAction> { storeOutput ->
            OctonautMarketAction.UpdateUser(storeOutput)
        }

        return RealMarketContributor(
            coroutineDispatcher,
            userStore,
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
        userMarketContributor: UserMarketContributor,
        warehouseBuilderFactory: WarehouseBuilderFactory
    ): HomeTabWarehouseFactory {
        return HomeTabWarehouseFactory(userMarketContributor, warehouseBuilderFactory)
    }

    @Provides
    @UserScope
    fun provideHomeTabWarehouse(
        homeTabWarehouseFactory: HomeTabWarehouseFactory
    ): HomeTabWarehouse {
        return homeTabWarehouseFactory.create()
    }

    @Provides
    fun provideHomeTabPresenter(
        warehouse: HomeTabWarehouse
    ): HomeTabPresenter {
        return HomeTabPresenter(warehouse)
    }

    @Provides
    fun provideHomeTabUi(): HomeTabUi = HomeTabUi()

    @Provides
    fun provideHomeTab(): HomeTab = RealHomeTab

    companion object
}

