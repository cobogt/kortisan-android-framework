package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

sealed class AuthenticationActions: ReduxAction() {
    data class MigrateUser(val icu: String, val cellphone: String, val passcode: String): AuthenticationActions()
    object VerifyLiteAuthentication: AuthenticationActions()
}
