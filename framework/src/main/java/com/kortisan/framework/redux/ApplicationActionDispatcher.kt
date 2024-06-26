package com.kortisan.framework.redux
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.util.Log
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.actions.ReDispatchAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.middleware.*
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A diferencia del patrón redux tradicional en este la reducción de estados se realiza en dos
 * lugares diferentes, en el estado general de la aplicación a la cual se le envían las acciones
 * a reducir directamente desde los controladores, esto se hace para poder controlar los flujos
 * y las dependencias de acciones antes de proceder a l reducción, y en los BLoC existentes
 * dentro del viewmodel en donde cad uno de ellos contiene sus estados y se encarga de su
 * propia reducción.
 *
 * Por tanto en este despachador solo se realiza el seguimiento y reemplazo (reducción) de la
 * acción actual para que ésta sea observada desde el viewModel, no reemplaza los estados.
 */
class ApplicationActionDispatcher private constructor(): ViewModelActionDispatcher {
    private val applicationState = ApplicationStateStore

    // Cada despachador debe tener su acción actual
    private val currentActionM = MutableStateFlow<ReduxAction>( ReduxAction.EmptyAction )
    override val currentAction: StateFlow<ReduxAction> = currentActionM

    private val middlewares = listOf<MiddlewareInterface>(
        TaggingMiddleware,
        GatesMiddleware(this),

        SensorsMiddleware,
        RemoteConfigMiddleware,
        UpdatesMiddleware,
        CalculatePerformanceScoreMiddleware,
        PositionMiddleware,
        NotificationMiddleware,
        BiometricsMiddleware,

        NavigationMiddleware,
        ErrorHandlerMiddleware
    )

    companion object {
        private var applicationActionDispatcher: ApplicationActionDispatcher? = null

        fun getInstance(
            currentAction: ReduxAction? = null
        ): ApplicationActionDispatcher {
            applicationActionDispatcher =
                applicationActionDispatcher ?: ApplicationActionDispatcher()

            // Establece la acción inicial para una actividad recién instanciada
            currentAction?.run {
                applicationActionDispatcher?.currentActionM?.value = currentAction
            }

            return applicationActionDispatcher !!
        }

        fun dispatch( action: ReduxAction ) = getInstance().dispatch( action )
    }

    /**
     * Procesamos la acción en todos los viewModel antes de hacerla observable por la vista
     */
    override fun dispatch( action: ReduxAction ) {
        var currentReduxAction: ReduxAction = action
        // Cuántas veces se puede repetir el ciclo para evitar loops infinitos
        var refreshLimit = 5

        // Este ciclo permite repetir el ciclo del dispatcher sin que llegue al emisor original
        do {
            if( currentReduxAction is ReDispatchAction ) {
                if( refreshLimit == 0)
                    break

                currentReduxAction = currentReduxAction.action

                refreshLimit --
            }

            middlewares.forEach {
                currentReduxAction = it.doNext( currentReduxAction, applicationState )
            }

            Log.d( javaClass.simpleName, currentReduxAction.toString() )
        } while ( currentReduxAction is ReDispatchAction )

        currentActionM.value = currentReduxAction
    }
}