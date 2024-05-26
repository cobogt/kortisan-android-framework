package com.kortisan.framework.redux.state

import com.karumi.dexter.DexterBuilder

sealed class PermissionState: ReduxState() {
    data class AllGranted(
        val permissions: List<DexterBuilder.Permission>
    ): PermissionState()
    object NoPermission: PermissionState()

    data class PermissionNeeded(
        val granted: List<DexterBuilder.Permission>,
        val notGranted: List<DexterBuilder.Permission>
    ): PermissionState()
}