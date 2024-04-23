package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter
import com.slack.circuit.runtime.ui.Ui as CircuitUi

interface HomeTab : Screen {
    sealed interface State : CircuitUiState {
        data object Initial : State
        data class Loading(
            val event: (Event) -> Unit
        ): State
        data class Loaded(
            val user: User,
            val event: (Event) -> Unit
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Refresh: Event
    }

    interface Ui : CircuitUi<State>
    interface Presenter : CircuitPresenter<State>
}