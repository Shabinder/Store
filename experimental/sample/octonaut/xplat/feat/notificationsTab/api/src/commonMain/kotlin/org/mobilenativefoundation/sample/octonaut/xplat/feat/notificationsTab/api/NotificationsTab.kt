package org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.Notification
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter
import com.slack.circuit.runtime.ui.Ui as CircuitUi

interface NotificationsTab : Screen {
    sealed interface State : CircuitUiState {
        data object Initial : State
        data class Loaded(
            val notifications: List<Notification>,
            val eventSink: (Event) -> Unit
        ) : State
        data object Loading: State

    }

    sealed interface Event : CircuitUiEvent {
        data class MarkDone(val notificationId: String): Event
    }

    interface Ui : CircuitUi<State>
    interface Presenter : CircuitPresenter<State>
}