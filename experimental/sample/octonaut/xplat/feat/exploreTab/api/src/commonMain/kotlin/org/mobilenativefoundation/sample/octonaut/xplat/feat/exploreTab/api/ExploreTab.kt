package org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter
import com.slack.circuit.runtime.ui.Ui as CircuitUi

interface ExploreTab : Screen {
    sealed interface State : CircuitUiState {
        data object Initial : State
        data class Loaded(
            val user: User,
            val searchInput: String? = null,
            val eventSink: (Event) -> Unit,
        ) : State
        data class Loading(
            val eventSink: (Event) -> Unit
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data class UpdateSearchInput(val searchInput: String): Event
        data class Search(val searchInput: String) : Event
    }

    interface Ui : CircuitUi<State>
    interface Presenter : CircuitPresenter<State>
}