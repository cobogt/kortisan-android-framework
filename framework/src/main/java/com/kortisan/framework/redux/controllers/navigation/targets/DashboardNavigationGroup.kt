package com.kortisan.framework.redux.controllers.navigation.targets
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.navigation.PrimitiveTargetActivityIdentifier
import com.kortisan.framework.redux.gates.FingerprintGate

class DashboardNavigationGroup(override val prefixPath: String = "dashboard"):
    NavigationTargetGroup {
    object Dashboard: TargetActivity(
        "/main", DashboardActivityTarget.target ) {
        object DashboardCompose: TargetCompose<Dashboard>(Dashboard, "/")

        // override val accessGate = FingerprintGate
    }

    object ShowError: TargetActivity("/errorMessage", ShowErrorActivityTarget.target)
    object AskBiometrics: TargetActivity("/biometrics", AskBiometricsActivityTarget.target)

    object DashboardActivityTarget: SealedActivityClassList(
        PrimitiveTargetActivityIdentifier(
            "com.kortisan.content.presentation.MainActivity",
            flowName = "MainActivityFlowName")
    )

    object ShowErrorActivityTarget: SealedActivityClassList(
        PrimitiveTargetActivityIdentifier(
            "com.kortisan.framework.redux.controllers.errorHandler.ShowErrorActivity",
            flowName = "MainActivityFlowName")
    )

    object AskBiometricsActivityTarget: SealedActivityClassList(
        PrimitiveTargetActivityIdentifier(
            "com.kortisan.framework.redux.controllers.securityAssets.BiometricPromptActivity",
            "AskBiometricsFlowName"
        )
    )
}
