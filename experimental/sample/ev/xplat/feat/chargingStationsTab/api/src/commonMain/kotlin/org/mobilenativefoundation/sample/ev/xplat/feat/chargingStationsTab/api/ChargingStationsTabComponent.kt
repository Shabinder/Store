package org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api

interface ChargingStationsTabComponent {
    val chargingStationsTabUi: ChargingStationsTab.Ui
    val chargingStationsTabPresenter: ChargingStationsTab.Presenter
    val chargingStationsTab: ChargingStationsTab
}