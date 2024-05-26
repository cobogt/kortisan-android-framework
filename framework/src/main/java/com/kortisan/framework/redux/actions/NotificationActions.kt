package com.kortisan.framework.redux.actions/** * * * * * * * * * * Project KoreFrame * Created by Jacobo G Tamayo on 30/12/22.* * * * * * * * * * **/import android.os.Bundleimport com.kortisan.framework.redux.controllers.notifications.NotificationChannelsealed class NotificationActions: ReduxAction() {    data class ShowNotification(        val title:      String,        val bodyText:   String,        val group:      String,        val autoCancel: Boolean = true,        val smallIcon:  Int,        val urlImage:   String? = null,        val channel:    NotificationChannel = NotificationChannel.SUPER_APP,        // Se convierte de ReduxAction a Intent en el middleware        val action:     ReduxAction? = null,    ): NotificationActions()    data class ShowCleverTapNotification(        val notificationBundle: Bundle    ): NotificationActions()    data class HideNotification( val notificationId: Int ): NotificationActions()    object HideAllNotifications: NotificationActions()}