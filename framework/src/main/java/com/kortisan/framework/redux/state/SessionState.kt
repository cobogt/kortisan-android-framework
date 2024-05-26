package com.kortisan.framework.redux.state

import com.kortisan.framework.redux.state.caretaker.CaretakerStrategy
import com.kortisan.framework.redux.state.caretaker.FileCaretakerStrategy
import com.kortisan.framework.redux.state.productionrules.ProductionRule

sealed class SessionState: ReduxState() {
    data class Logged    ( val user: SessionState) :   SessionState() {
        override val productionRules: List<ProductionRule> = sharedProductionRules }
    object LoggedOut: SessionState() {
        override val productionRules: List<ProductionRule> = sharedProductionRules }
    object Loading: SessionState() {
        override val productionRules: List<ProductionRule> = sharedProductionRules }

    protected val sharedProductionRules: List<ProductionRule> = listOf()
    override val caretakerStrategy:      CaretakerStrategy? by lazy {
        FileCaretakerStrategy( defaultState = LoggedOut )
    }
}