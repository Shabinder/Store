package org.mobilenativefoundation.sample.ev.xplat.foundation.di.api

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.NetworkingClient

interface CoreComponent {
    val presenterFactory: Presenter.Factory
    val uiFactory: Ui.Factory
    val screenFactory: ScreenFactory
    val networkingClient: NetworkingClient
}