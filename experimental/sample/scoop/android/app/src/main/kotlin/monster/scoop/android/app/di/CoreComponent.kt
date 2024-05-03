package monster.scoop.android.app.di

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import monster.scoop.android.app.circuit.ScoopPresenterFactory
import monster.scoop.android.app.circuit.ScoopScreenFactory
import monster.scoop.android.app.circuit.ScoopUiFactory
import monster.scoop.android.app.circuit.ScreenFactory
import monster.scoop.xplat.foundation.di.UserScope

@UserScope
@Component
abstract class CoreComponent {

    abstract val presenterFactory: Presenter.Factory
    abstract val screenFactory: ScreenFactory
    abstract val uiFactory: Ui.Factory

    @Provides
    fun bindPresenterFactory(impl: ScoopPresenterFactory): Presenter.Factory = impl

    @Provides
    fun bindUiFactory(impl: ScoopUiFactory): Ui.Factory = impl

    @Provides
    fun bindScreenFactory(impl: ScoopScreenFactory): ScreenFactory = impl

    companion object
}