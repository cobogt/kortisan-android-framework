package com.kortisan.framework.redux.controllers.notifications

import android.app.NotificationManager

enum class NotificationChannel(val cId: String, val cName: String, val cPriority: Int ) {
    MYAPP(
        "CHANNEL_ID_MYAPP",
        "InfoMyApp",
        NotificationManager.IMPORTANCE_DEFAULT ),

}