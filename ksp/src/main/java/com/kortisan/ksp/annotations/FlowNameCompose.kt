package com.kortisan.ksp.annotations

@Target( AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class FlowNameCompose(
    val activityClassName: String,
    val flowName: String,
    // Lista de gates
)
