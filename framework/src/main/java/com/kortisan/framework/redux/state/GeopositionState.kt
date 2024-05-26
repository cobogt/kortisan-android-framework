package com.kortisan.framework.redux.state

import com.kortisan.framework.redux.state.productionrules.ProductionRule

sealed class GeopositionState: ReduxState() {
    data class PositionAdquired(
        val lat: Long,
        val lon: Long,
        val alt: Long,
        val precision: Long = 1L
    ): GeopositionState() {
        override val productionRules: List<ProductionRule> = commonProductionRules
    }
    object Loading: GeopositionState() {
        override val productionRules: List<ProductionRule> = commonProductionRules
    }
    object NoPermission: GeopositionState() {
        override val productionRules: List<ProductionRule> = commonProductionRules
    }

    protected val commonProductionRules = listOf<ProductionRule>(
        // TODO: Definir reglas de producción para los estados de la ubicación
    )
}