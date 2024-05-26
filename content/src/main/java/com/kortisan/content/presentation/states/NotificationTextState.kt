package com.kortisan.content.presentation.states

import com.kortisan.framework.redux.actions.NotificationActions
import com.kortisan.framework.redux.state.productionrules.ProductionRule
import com.kortisan.framework.redux.state.ReduxState

sealed class NotificationTextState: ReduxState() {
    data class TextToShow( val currentShow: String ): NotificationTextState() {
        override val productionRules = sharedProductionRules
    }

    internal val sharedProductionRules = listOf<ProductionRule>(
        { state, action ->
            if( action is NotificationActions.ShowNotification )
                TextToShow( "Mostrando notificacion" )
            else state
        },
        { state, action ->
            if( action is NotificationActions.HideAllNotifications )
                TextToShow( "Notificaciones borradas" )
            else state
        }
    )
}
