package com.kortisan.framework

import android.app.Application
import android.content.Context
import com.kortisan.framework.redux.actions.LoadStateAction
import com.kortisan.framework.redux.controllers.navigation.strategies.NavigationStrategy
import com.kortisan.framework.redux.stores.ApplicationStateStore

open class FrameworkApplicationBinding: Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        registerActivityLifecycleCallbacks(
            NavigationStrategy
        )

        // Cargamos el estado previo de la aplicación
        ApplicationStateStore.reduceAction( LoadStateAction )
    }
}