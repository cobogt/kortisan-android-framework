package com.kortisan.framework.redux.controllers.navigation.targets

import com.kortisan.framework.redux.controllers.navigation.PrimitiveTargetActivityIdentifier

class AuthenticationNavigationGroup (override val prefixPath: String = "Authentication"):
    NavigationTargetGroup {
    object Authentication: TargetActivity("/", AuthenticationActivityTarget.target ) {
        object LoginCompose:              TargetCompose<Authentication>(
            Authentication, "/login")

        object ReadBiometricsCompose:     TargetCompose<Authentication>(
            Authentication, "/read_biometrics")

        object ConfirmPasswordCompose:    TargetCompose<Authentication>(
            Authentication, "/confirm_password")

        object AskForPasswordCompose:     TargetCompose<Authentication>(
            Authentication, "/ask_for_password")

        object AskForEmailCompose:        TargetCompose<Authentication>(
            Authentication, "/ask_for_email")

    }

    object AuthenticationActivityTarget: SealedActivityClassList(
        PrimitiveTargetActivityIdentifier(
            "com.kortisan.authentication.presentation.AuthenticationMainActivity",
            flowName = "AuthenticationMainActivityFlowName",
            section = "" )
    )
}