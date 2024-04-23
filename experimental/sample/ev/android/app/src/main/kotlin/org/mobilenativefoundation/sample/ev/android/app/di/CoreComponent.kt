package org.mobilenativefoundation.sample.ev.android.app.di

import com.apollographql.apollo3.ApolloClient
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.mobilenativefoundation.sample.ev.android.app.circuit.EvPresenterFactory
import org.mobilenativefoundation.sample.ev.android.app.circuit.EvScreenFactory
import org.mobilenativefoundation.sample.ev.android.app.circuit.EvUiFactory
import org.mobilenativefoundation.sample.ev.android.app.circuit.ScreenFactory
import org.mobilenativefoundation.sample.ev.android.app.market.MutableEvMarket
import org.mobilenativefoundation.sample.ev.android.app.market.RealEvDispatcher
import org.mobilenativefoundation.sample.ev.android.app.market.RealMutableEvMarket
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvDispatcher
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvMarket
import org.mobilenativefoundation.sample.ev.xplat.domain.chargingStations.api.ChargingStationsComponent
import org.mobilenativefoundation.sample.ev.xplat.domain.chargingStations.api.GetNearbyChargingStationsData
import org.mobilenativefoundation.sample.ev.xplat.domain.chargingStations.api.NearbyChargingStationsStore
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTab
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTabComponent
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.impl.*
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.UserScope
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.GetNearbyChargingStationsQuery
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.NetworkingComponent
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.impl.Env
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.impl.RealNetworkingClient
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.StoreBuilder

@UserScope
@Component
abstract class CoreComponent : ChargingStationsComponent, ChargingStationsTabComponent, NetworkingComponent {

    abstract val screenFactory: ScreenFactory
    abstract val presenterFactory: Presenter.Factory
    abstract val uiFactory: Ui.Factory
    abstract val evMarket: EvMarket
    abstract val mutableEvMarket: MutableEvMarket
    abstract val evDispatcher: EvDispatcher
    abstract val coroutineDispatcher: CoroutineDispatcher

    @UserScope
    @Provides
    fun bindScreenFactory(impl: EvScreenFactory): ScreenFactory = impl

    @UserScope
    @Provides
    fun bindPresenterFactory(impl: EvPresenterFactory): Presenter.Factory = impl

    @UserScope
    @Provides
    fun bindUiFactory(impl: EvUiFactory): Ui.Factory = impl

    @UserScope
    @Provides
    fun provideMutableEvMarket(): MutableEvMarket = RealMutableEvMarket()

    @UserScope
    @Provides
    fun provideEvMarket(mutableEvMarket: MutableEvMarket): EvMarket = mutableEvMarket

    @UserScope
    @Provides
    fun provideEvDispatcher(mutableEvMarket: MutableEvMarket): EvDispatcher {
        return RealEvDispatcher(mutableEvMarket)
    }

    @UserScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @UserScope
    fun bindChargingStationsTabUi(impl: ChargingStationsTabUi): ChargingStationsTab.Ui = impl

    @Provides
    @UserScope
    fun bindChargingStationsTabPresenter(impl: ChargingStationsTabPresenter): ChargingStationsTab.Presenter = impl

    @Provides
    @UserScope
    fun bindChargingStationsTabWarehouse(impl: RealChargingStationsTabWarehouse): ChargingStationsTabWarehouse = impl

    @Provides
    @UserScope
    fun provideChargingStationsTab(): ChargingStationsTab = RealChargingStationsTab

    @Provides
    @UserScope
    fun provideNearbyChargingStationsStore(networkingClient: NetworkingClient): NearbyChargingStationsStore {
        val storeBuilder = StoreBuilder.from<GetNearbyChargingStationsQuery, GetNearbyChargingStationsData>(
            fetcher = Fetcher.of { query ->
                networkingClient
                    .getNearbyChargingStations(query)
                    ?.stationAround
                    ?.filterNotNull()
                    ?.let { GetNearbyChargingStationsData(it) }
                    ?: throw IllegalStateException("Empty response")
            }
        )

        return storeBuilder.build()
    }

    @Provides
    @UserScope
    fun bindNetworkingClient(impl: RealNetworkingClient): NetworkingClient = impl

    @Provides
    @UserScope
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
        .serverUrl("https://api.chargetrip.io/graphql")
        .addHttpHeader(
            "x-app-id", Env.X_APP_ID
        )
        .build()

    companion object
}