package org.mobilenativefoundation.sample.ev.android.app.di

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.CoreComponent
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.ScreenFactory
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.impl.EvPresenterFactory
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.impl.EvUiFactory
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.impl.RealScreenFactory

@Component
abstract class RealCoreComponent(
) : CoreComponent {

    @Provides
    fun bindPresenterFactory(impl: EvPresenterFactory): Presenter.Factory = impl

    @Provides
    fun bindUiFactory(impl: EvUiFactory): Ui.Factory = impl

    @Provides
    fun bindScreenFactory(impl: RealScreenFactory): ScreenFactory = impl

    companion object
}