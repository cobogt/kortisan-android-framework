package com.kortisan.framework.redux.controllers.navigation.protocol
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.RevertAction
import com.kortisan.framework.redux.controllers.gates.BaseGate
import java.util.*

/**
 * Esta clase define todas las rutas posibles disponible dentro de la aplicación
 * Los métodos disponibles para crear las instancias sirven para documentar los parámetros
 * requeridos por cada una de las vistas.
 * Así también existe una ruta genérica para las rutas no disponibles.
 */
open class NavigationTarget {
    val actionTargetSack: Stack<RevertAction> = Stack()

    open val accessGate: BaseGate? = null
}
