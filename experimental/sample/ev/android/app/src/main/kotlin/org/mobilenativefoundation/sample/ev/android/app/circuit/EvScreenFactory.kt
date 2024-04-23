package org.mobilenativefoundation.sample.ev.android.app.circuit

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTab
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class EvScreenFactory(
    override val chargingStationsTab: ChargingStationsTab
) : ScreenFactory