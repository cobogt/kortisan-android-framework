package com.kortisan.framework.redux.controllers.notifications

import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kortisan.framework.camelCaseKeys
import com.kortisan.framework.collapseJsonMap
import com.kortisan.framework.entrypoints.PushNotificationEntrypointStrategy
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.actions.NotificationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.tagging.builders.EntryPointSceneBuilder
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import com.kortisan.framework.storage.security.AesStrategy
import com.kortisan.framework.toBundle

class FirebaseMessagingReceiver: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var action: ReduxAction? = null
        val data = remoteMessage.data
        val exposedData = data
            .collapseJsonMap()
            .camelCaseKeys( /*true*/ )

        var scene: SceneDecoratorInterface = EntryPointSceneBuilder
            .Builder("firebaseMessagingReceiver")
            .build()

        scene = FirebaseMessagingDecorator( scene )
            .apply {
                exposedData["targetFlowName"]  ?.also { setFlowName( it ) }
            }

        val notificationBundle = data.toBundle()
        when {
            // Cuando es una notificación CON interfaz gráfica
            remoteMessage.notification != null -> {
                remoteMessage.notification?.also {
                    // Creamos una acción para mostrar la notificación
                    action = NotificationActions.ShowNotification(
                        "${ it.title }",
                        "${ it.body }",
                        exposedData["parentGroup"] ?: "defaultGroup",
                        false,
                        0,
                        AesStrategy.decrypt(
                            remoteMessage.data["urlImage"] ?: "",
                        ),
                        action = PushNotificationEntrypointStrategy( exposedData ).getAction()
                    )
                }
            }
            else -> action = PushNotificationEntrypointStrategy( exposedData ).getAction()
        }

        action?.run {
            ApplicationActionDispatcher.dispatch(
                apply { tagging = scene }
            )
        }
    }

    override fun onNewToken(newFirebaseToken: String) {
        super.onNewToken(newFirebaseToken)
        CleverTapAPI.getDefaultInstance(applicationContext)
            ?.pushFcmRegistrationId( newFirebaseToken, true )
    }
}
