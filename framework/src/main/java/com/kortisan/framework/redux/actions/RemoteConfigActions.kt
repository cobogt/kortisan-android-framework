package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

sealed class RemoteConfigActions: ReduxAction() {
    object ReloadAction: RemoteConfigActions()

    sealed class StoreRemoteConfigAction: RemoteConfigActions() {
        data class LoadFailAction( val exception: Exception ): StoreRemoteConfigAction()
        data class RefreshFailAction( val exception: Exception ): StoreRemoteConfigAction()
        data class SuccessLoadAction( val content: Map<String, Any?> ): StoreRemoteConfigAction()
    }
}