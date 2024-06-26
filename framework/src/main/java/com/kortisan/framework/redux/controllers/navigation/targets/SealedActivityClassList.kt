package com.kortisan.framework.redux.controllers.navigation.targets
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.navigation.PrimitiveTargetActivityIdentifier

// Contenedor para el catálogo de todas las actividades de la aplicación
sealed class SealedActivityClassList(val target: PrimitiveTargetActivityIdentifier)
