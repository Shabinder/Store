package monster.scoop.xplat.feat.homeTab.api

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter
import com.slack.circuit.runtime.ui.Ui as CircuitUi

interface HomeTab : Screen {

    data class State(
        val stories: List<HomeTabStory>,
        val loadingStatus: LoadingStatus = LoadingStatus(start = false, end = false),
        val errors: List<Error> = emptyList(),
        val warnings: List<Warning> = emptyList(),
        val eventSink: (Event) -> Unit,
        val onAnchorPositionChange: (anchorPosition: Int) -> Unit,
    ) : CircuitUiState {

        /**
         * Indicates data is being loaded.
         * It's possible for [start] and [end] to be true.
         * @property start True if loading from the start of the list
         * @property end True if loading from the end of the list
         */
        data class LoadingStatus(
            val start: Boolean,
            val end: Boolean,
        )
    }

    sealed interface Error {
        data object SomethingWentWrong : Error
    }

    sealed interface Warning {
        data object Offline : Warning
    }

    sealed interface Event : CircuitUiEvent {
        data object Refresh : Event
    }

    interface Ui : CircuitUi<State>
    interface Presenter : CircuitPresenter<State>
}