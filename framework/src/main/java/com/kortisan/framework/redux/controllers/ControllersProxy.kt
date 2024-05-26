package com.kortisan.framework.redux.controllers

import com.kortisan.framework.redux.actions.ReduxAction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 03/01/23.
 * * * * * * * * * * **/
object ControllersProxy {
    val controllersInstance = mutableMapOf<String, ReduxControllerAbstract>()

    inline fun <reified T: ReduxControllerAbstract>getController(
        action: ReduxAction? = null
    ): T? =
        try {
            val className: String = T::class.qualifiedName ?: ""

            T::class.primaryConstructor?.isAccessible = true

            if(
                T::class.primaryConstructor?.isAccessible == true &&
                T::class.primaryConstructor?.parameters?.size == 0 &&
                T::class.primaryConstructor?.visibility == KVisibility.PUBLIC
            ) {
                if( ! controllersInstance.containsKey( className ) )
                    controllersInstance[ className ] = T::class.java.getDeclaredConstructor().newInstance()

                val controllerInstance: T? = controllersInstance[ className ] as T?

                controllerInstance?.let {
                    when( it.isStarted.value ) {
//                        is ReduxControllerState.ErrorOnStart -> {}
//                        ReduxControllerState.Started -> {}
                        ReduxControllerState.Stopped -> it.start()
                        else -> { /* Nada por hacer */ }
                    }

                    action?.also { currentAction ->
                        controllerInstance.currentAction = ( currentAction )
                    }

                    it
                }
            } else
                null
        }catch ( e: Exception ) {
            // Error al inicializar el controlador
            println("${javaClass.simpleName} Error: ${e.localizedMessage}")
            null
        }
}