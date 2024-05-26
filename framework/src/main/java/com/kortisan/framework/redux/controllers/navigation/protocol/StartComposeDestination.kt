package com.kortisan.framework.redux.controllers.navigation.protocol
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

/**
 * Define una ruta TargetCompose como primaria para el controlador de rutas
 * Si hay más de una anotación dentro de un TargetActivity se usará la primera como predeterminada
 */
@Retention( AnnotationRetention.RUNTIME )
annotation class StartComposeDestination
