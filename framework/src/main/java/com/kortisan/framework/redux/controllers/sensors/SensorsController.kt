package com.kortisan.framework.redux.controllers.sensors

import com.kortisan.framework.redux.controllers.ReduxControllerAbstract

class SensorsController: ReduxControllerAbstract() {
    private var refreshRate: Int = 60
    override fun start() {
        super.start()
    }

    fun setRefreshRate( refreshRate: Int = 60 ) {
        this.refreshRate = refreshRate
    }
}