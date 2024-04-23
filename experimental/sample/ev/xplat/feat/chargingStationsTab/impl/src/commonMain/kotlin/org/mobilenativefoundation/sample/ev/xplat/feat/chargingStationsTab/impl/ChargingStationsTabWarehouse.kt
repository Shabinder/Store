package org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.market.warehouse.Warehouse
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvAction
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvDispatcher
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvMarket
import org.mobilenativefoundation.sample.ev.xplat.domain.chargingStations.api.NearbyChargingStationsStore
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.GetNearbyChargingStationsQuery
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.type.PointInput
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.type.PointType
import org.mobilenativefoundation.store.store5.impl.extensions.get


data class ChargingStationsTabState(
    val nearbyChargingStations: List<GetNearbyChargingStationsQuery.StationAround>
) : Warehouse.State


sealed interface ChargingStationsTabAction : Warehouse.Action {
    data class UpdateNearbyChargingStations(
        val nearbyChargingStations: List<GetNearbyChargingStationsQuery.StationAround>
    ) : ChargingStationsTabAction

    sealed interface Async : Warehouse.Action.Async {
        data object GetNearbyChargingStations : Async
    }
}

typealias ChargingStationsTabWarehouse = Warehouse<ChargingStationsTabState, ChargingStationsTabAction, ChargingStationsTabAction.Async>

@Inject
class RealChargingStationsTabWarehouse(
    private val market: EvMarket,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val dispatcher: EvDispatcher,
    private val nearbyChargingStationsStore: NearbyChargingStationsStore
) : ChargingStationsTabWarehouse {

    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    override val state: StateFlow<ChargingStationsTabState> = market.state.map {
        ChargingStationsTabState(it.nearbyChargingStations)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, ChargingStationsTabState(emptyList()))

    override fun <D : Any> subscribe(selector: Warehouse.Selector<ChargingStationsTabState, D>): Flow<D> =
        state.map { selector.select(state.value) }

    override fun dispatch(worker: Warehouse.Worker<ChargingStationsTabState, ChargingStationsTabAction>) {
        worker.work(
            getState = { state.value },
            dispatch = ::dispatch
        )
    }

    override fun dispatch(action: ChargingStationsTabAction.Async) {
        return when (action) {
            ChargingStationsTabAction.Async.GetNearbyChargingStations -> {
                val worker =
                    Warehouse.Worker<ChargingStationsTabState, ChargingStationsTabAction> { getState, dispatch ->
                        coroutineScope.launch {
                            try {
                                val response = nearbyChargingStationsStore.get(
                                    GetNearbyChargingStationsQuery(
                                        location = PointInput(
                                            coordinates = listOf(9.993682, 53.551086),
                                            type = PointType.Point
                                        ),
                                        amenities = emptyList(),
                                        distance = 10
                                    )
                                )

                                dispatch(
                                    ChargingStationsTabAction.UpdateNearbyChargingStations(
                                        nearbyChargingStations = response.nearbyChargingStations
                                    )
                                )
                            } catch (error: Throwable) {
                                println("Error: $error")
                            }
                        }
                    }

                dispatch(worker)
            }
        }
    }

    override fun dispatch(action: ChargingStationsTabAction) {
        val nextState = when (action) {
            is ChargingStationsTabAction.UpdateNearbyChargingStations -> {
                market.state.value.copy(nearbyChargingStations = action.nearbyChargingStations)
            }
        }

        dispatcher.dispatch(EvAction(nextState))
    }

}