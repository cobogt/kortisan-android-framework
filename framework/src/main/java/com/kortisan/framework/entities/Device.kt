package com.kortisan.framework.entities
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import java.util.*

data class Device(
    var id: String          = "", // Id del dispositivo
    var pushId: String      = "", // Id de la notificación push
    var model: String       = "",
    var hashInfo: String    = UUID.randomUUID().toString(),
    var deviceName: String  = "",
    var carrier: String     = "",
    var idfa: String        = "",
    val type: String        = "android",
    var countryCode: String = "+52", // Código de país
)
