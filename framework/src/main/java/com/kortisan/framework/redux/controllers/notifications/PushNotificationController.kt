package com.kortisan.framework.redux.controllers.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.messaging.FirebaseMessagingService
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.redux.actions.NotificationActions
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.redux.controllers.notifications.NotificationChannel.*
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 06/01/23.
 * * * * * * * * * * **/
class PushNotificationController: ReduxControllerAbstract() {
    private var notificationId: Int = 0
    private val context = FrameworkApplicationBinding.appContext
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun showNotification( notification: NotificationActions.ShowNotification ): Int {
        val notificationBuilder = NotificationCompat.Builder(
            FrameworkApplicationBinding.appContext,
            notification.channel.cId
        )
            .setGroup( notification.group )
            .setSmallIcon( notification.smallIcon )
            .setAutoCancel( notification.autoCancel )
            .setContentText( notification.bodyText )
            .setContentTitle( notification.title )
            .setPriority( notification.channel.cPriority )

        notificationBuilder.build()

        if( notification.urlImage != null ) {
            // Descargamos la imagen
            // Asociamos la imágen a la notificación
            // Asociamos el ReduxAction al intent de la notificación
        }

        notificationManager.createNotificationChannel(
            NotificationChannel(
                notification.channel.cId,
                notification.channel.cName,
                notification.channel.cPriority)
        )

        notificationManager.notify( ++ notificationId, notificationBuilder.build() )
        turnOnScreen()

        return notificationId
    }

    fun hideNotificationById( id: Int ) =
        notificationManager.cancel( id )

    fun hideAllNotifications() =
        notificationManager.cancelAll()

    private fun turnOnScreen() {
        val power: PowerManager = FrameworkApplicationBinding
            .appContext
            .getSystemService(FirebaseMessagingService.POWER_SERVICE) as PowerManager

        power.run {
            if( isInteractive )
                newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "myApp:koreFrame"
                ).also {
                    it.acquire(3000)
                    it.release()
                }
        }
    }
}