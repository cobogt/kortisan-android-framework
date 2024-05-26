package com.kortisan.framework.redux.state.productionrules.dsl

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 30/mar./2024.
 * * * * * * * * * * **/

/**
 * EJEMPLO (PENDIENTE VALIDAR):
 *
nextNode( currentState ) {
    when { actionDispatched }

    resultState {
        success -> NewState,
        fail -> NewStateFail // Opcional
    }

    validations { // Opcional
        field( "Email" ) {
            emailFormat { }
            minLength { 10 }
        }
    }
}
*/