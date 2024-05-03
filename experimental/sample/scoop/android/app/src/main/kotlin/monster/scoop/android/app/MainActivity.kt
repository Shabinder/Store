package monster.scoop.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import coil3.annotation.ExperimentalCoilApi
import me.tatarka.inject.annotations.Inject


@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Inject
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
        }
    }
}
