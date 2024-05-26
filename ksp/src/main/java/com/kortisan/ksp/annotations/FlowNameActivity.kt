package com.kortisan.ksp.annotations

@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.SOURCE )
annotation class FlowNameActivity(
    val flowName: String,
    // Gates
)