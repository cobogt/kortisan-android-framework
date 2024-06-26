package com.kortisan.framework.bloc
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import kotlinx.coroutines.flow.StateFlow

interface ViewModelActionDispatcher {
    val currentAction: StateFlow<ReduxAction>
    fun dispatch(action: ReduxAction)
}