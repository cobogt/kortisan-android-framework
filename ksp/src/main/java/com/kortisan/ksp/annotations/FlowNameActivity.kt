package com.kortisan.ksp.annotations

@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.SOURCE )
annotation class FlowNameActivity(
    /**
     * Name to identify this activity
     */
    val flowName: String,
    /**
     * Path to find this activity, optional
     */
    val path: String = "",
)