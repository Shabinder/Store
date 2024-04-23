package org.mobilenativefoundation.sample.octonaut.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.slack.circuit.foundation.Circuit
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.android.app.di.CoreComponent
import org.mobilenativefoundation.sample.octonaut.android.app.di.create
import org.mobilenativefoundation.sample.octonaut.android.app.theme.OctonautTheme


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

            OctonautTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

                        Text(
                            "Octonaut",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Button(onClick = {}, shape = RectangleShape) {
                            Text("Octonaut")
                        }

                        Button(
                            onClick = {},
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Text("Octonaut", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}
