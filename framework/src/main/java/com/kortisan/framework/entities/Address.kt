package com.kortisan.framework.entities
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

data class Address(
    var street:        String = "",
    var numberExt:     String = "",
    var numberInt:     String = "",
    var postalAddress: String = "",
    var neighborhood:  String = "",
    var population:    String = "", // Población aka. City
    var state:         String = "",
    var country:       String = "",
    var lat:           String = "",
    var long:          String = "",
    var type:          String = "",
    var address:       String = "",
    var colony:        String = "",
    val shortAddress:  String = "${street.replaceFirstChar { it.uppercaseChar() }} $postalAddress"
)
