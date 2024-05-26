package com.kortisan.framework.redux.controllers.notifications

import android.app.NotificationManager

enum class NotificationChannel(val cId: String, val cName: String, val cPriority: Int ) {
    SUPER_APP(
        "CHANNEL_ID_SUPERAPP",
        "Informativa",
        NotificationManager.IMPORTANCE_DEFAULT ),

    CLEVERTAP(
        "CHANNEL_ID_SUPERAPP",
        "Informativa",
        NotificationManager.IMPORTANCE_MAX ),
}