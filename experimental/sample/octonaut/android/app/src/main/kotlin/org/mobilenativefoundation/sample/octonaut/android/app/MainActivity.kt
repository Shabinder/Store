package org.mobilenativefoundation.sample.octonaut.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.slack.circuit.foundation.Circuit
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.android.app.di.CoreComponent
import org.mobilenativefoundation.sample.octonaut.android.app.di.create


@Inject
class MainActivity : ComponentActivity() {

    private val coreComponent: CoreComponent by lazy {
        CoreComponent.create()
    }

    private val circuit: Circuit by lazy {
        Circuit.Builder()
            .addPresenterFactory(coreComponent.presenterFactory)
            .addUiFactory(coreComponent.uiFactory)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Text("")
        }
    }
}
