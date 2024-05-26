package com.kortisan.framework.redux.state.productionrules

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.state.ReduxState

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 03/01/23.
 * * * * * * * * * * **/

/**
 * Regla de porducción base, recibe un estado y una acción; produce un nuevo estado.
 * @see ReduxAction
 * @see ReduxState
 */
typealias ProductionRule = (ReduxState, ReduxAction) -> ReduxState