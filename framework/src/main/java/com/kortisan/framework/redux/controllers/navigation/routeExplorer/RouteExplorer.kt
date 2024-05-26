@file:Suppress("UNCHECKED_CAST")

package com.kortisan.framework.redux.controllers.navigation.routeExplorer
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.util.Log
import com.google.gson.Gson
import com.kortisan.framework.BuildConfig
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.activityMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameActivityMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameAliasMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameDefaultParamsMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameTargetMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.logDirectories
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.paramsTargetsMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.routerMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.routerParamMap
import com.kortisan.framework.redux.controllers.navigation.protocol.DefaultParams
import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget
import com.kortisan.framework.redux.controllers.navigation.targets.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.*

object RouteExplorer {
    val TAG: String = javaClass.simpleName

    /**
     * Almacena un alias para un flowName determinado
     */
    fun setFlowNameAlias(flowName: String, alias: String) {
        if( flowNameTargetMap.containsKey( flowName ))
            flowNameAliasMap[ alias ] = flowName
    }

    /**
     * Devuelve los parámetros necesarios para llamar la clase navigateToClass: Clase y Parámetros
     * Del parámetro de tipo <TargetActivity> se extrae el nombre de la
     * clase y la anotación defaultParams.
     * De la instancia de 'target' se extraen los parámetros para llamar a la vista
     */
    fun getClassParamsFromNavigationTargetParams(
        target: NavigationTargetParams<*>
    ): Pair< String, Map<String, String> > {
        val params = mutableMapOf<String, String>()
        var className = ""

        // Obtenemos el primer parámetro de tipo de la instancia
        target.javaClass.typeParameters.getOrNull( 0 )?.also {
            // Creamos una instancia de la clase parámetro que extiende de TargetActivity
            TargetActivity::class.primaryConstructor
                ?.callBy( getPrimaryConstructorMandatoryParameters( it.javaClass::class ) )?.run {
                // Obtenemos el className
                className = primitiveTargetActivityIdentifier.className

                // Obtenemos la anotación defaultParams en caso de que exista
                this::class.annotations.firstOrNull { it is DefaultParams }?.also {
                    val jsonDefaultParams = (it as DefaultParams).json

                    if( jsonDefaultParams.isNotEmpty() )
                        params["KEY_PARAMS"] = jsonDefaultParams
                }
            }
        }

        // Extraemos los parámetros del objetivo
        target.javaClass.declaredFields
            .filterNot { it.isSynthetic }
            .filter { it.isAccessible }
            .forEach {
            it.get( target )?.also { field ->
                params[ it.name ] = field.toString()
            }
        }

        // Si no se definieron parámetros default, agregamos todos los parámetros como json
        if( ! params.containsKey("KEY_PARAMS") )
            params["KEY_PARAMS"] = try {
                Gson().toJson(params)
            }catch (e: java.lang.Exception) { "" }

        return Pair( className, params )
    }

    fun detectNavigationTargets() {
        Log.d(TAG, "Iniciando exploración de rutas")
        SealedActivityClassList::class.sealedSubclasses.forEach { subClass ->
            SealedActivityClassList::class.cast( subClass.objectInstance ).run {
                // Directorio 5
                if(flowNameActivityMap.containsKey( target.flowName ))
                    Log.e(
                        TAG, "Dir 5: FlowName duplicado " +
                            "${flowNameActivityMap[ target.flowName ]} == $target")
                else
                    flowNameActivityMap[ target.flowName ] = target
            }
        }

        /**
         * Para las clases anidadas dentro de un grupo
         *
         * De los TargetActivity se debe extraer:
         *  El primitiveTargetActivityIdentifier
         *      De este elemento se deben extraer:
         *          El className de la actividad que representa
         *          El flowName correspondiente
         *          En caso de aplicar, el section y los parámetros que se puedan extraer para
         *          construir el objeto de Taggeo( al menos hasta ScreenName):
         *              TaggingAction (ActionName y ScreenName)
         *              ScreenName (Name y Section)
         *              Section (Name)
         *  El path que se complementará con el path del grupo
         *  En caso de haber, la lista de parámetros para construirlo
         *
         * Tipo 1: Object: TargetActivity -> Actividad sin parámetros
         * Tipo 1.1: DataClass: NavigationTargetParams dentro de object: TargetActivity ->
         *      Configuración de parámetros para iniciar una actividad.
         * Tipo 1.2: object: TargetCompose dentro de object: TargetActivity -> Configuración
         *      de parámetros para iniciar una actividad y pasarlos al controlador de
         *      navegación de compose.
         *      Este tipo de objeto se va a utilizar, junto con el path del grupo, el path
         *      de la actividad y el path del componente, para crear el árbol de navegación.
         *      también conocido como Grafo de navegación, disponible en toda la aplicación
         *      e inyectado dentro del navGraph.
         *
         * Tipo 2: DataClass: TargetActivity -> Actividad con parámetros
         * Tipo 2.1: DataClass:NavigationTargetParams dentro de DataClass: TargetActivity
         * Tipo 2.2: DataClass: TargetCompose dentro de DataClass: TargetActivity
         *
         * Tipo 3: SealedClass: TargetActivity // NO ES NECESARIO
         */
        NavigationTargetGroup::class.sealedSubclasses.forEach { kClassNavTargetGroup ->
            // Recorremos las clases internas del grupo
            kClassNavTargetGroup.nestedClasses.forEach { kClass ->
                if(kClass.isCompanion) {
                    // En caso de companion se debe utilizar para obtener los parámetros de
                    // construcción del objetivo de navegación
                }else {
                    // Instanciamos al grupo para obtener su path
                    val ntgInstance = kClassNavTargetGroup.createInstance()

                    //  Si la clase extiende de TargetActivity, continuamos para omitir
                    //  el companion object.
                    if(kClass.isSubclassOf( TargetActivity::class ) ) {
                        // Directorio 8: Extraemos la anotaciones para los valores default que
                        // se van a enviar en el campo genérico 'KEY_PARAMS'.
                        val defaultParams: DefaultParams? =
                            (kClass.annotations.find { it is DefaultParams } as DefaultParams?)

                        // Tipo 1: Object:TargetActivity
                        if(kClass.isFinal) {
                            (kClass.objectInstance as TargetActivity?)
                                ?.let { targetActivityObject ->
                                    // Directorio 8
                                    defaultParams?.also {
                                        flowNameDefaultParamsMap[
                                                targetActivityObject
                                                    .primitiveTargetActivityIdentifier
                                                    .flowName
                                        ] = it.json
                                    }

                                    analyzeActivityTargetContent(
                                        targetActivityObject,
                                        kClass as KClass<TargetActivity>,
                                        ntgInstance,
                                        mapOf()
                                    )
                                }
                        }

                        // Tipo 2: DataClass TargetActivity
                        if(kClass.isData) {
                            val targetActivityDataClassInstance = kClass
                                .primaryConstructor
                                ?.callBy(
                                    getPrimaryConstructorMandatoryParameters( kClass )
                                ) as TargetActivity

                            val parameterMap = kClass
                                .primaryConstructor
                                ?.parameters
                                ?.map {
                                    it.name !! to getParamTypeName(it.type)
                                }?.toMap() ?: mapOf()

                            // Directorio 8
                            defaultParams?.also {
                                flowNameDefaultParamsMap[
                                        targetActivityDataClassInstance
                                            .primitiveTargetActivityIdentifier
                                            .flowName
                                ] = it.json
                            }

                            analyzeActivityTargetContent(
                                targetActivityDataClassInstance,
                                kClass as KClass<TargetActivity>,
                                ntgInstance,
                                parameterMap
                            )
                        }
                    }

                    if( kClass.isSubclassOf( TargetExternal::class ) && kClass.isFinal ) {
                        ( kClass.objectInstance as TargetExternal? )?.let { targetExternalObject ->
                            targetExternalObject.path?.also { path ->
                                routerMap[ path ] = targetExternalObject
                            }

                            targetExternalObject.flowName?.also { flowName ->
                                flowNameTargetMap[ flowName ] = targetExternalObject
                            }
                        }
                    }

                    if( kClass.isSubclassOf( TargetClass::class  )) {
                        if( kClass.isFinal ) {
                            ( kClass.objectInstance as TargetExternal? )?.let { targetClassObject ->
                                targetClassObject.flowName?.also { flowName ->
                                    flowNameTargetMap[ flowName ] = targetClassObject
                                }
                            }
                        }

                        if( kClass.isData ) {
                            kClass.primaryConstructor?.callBy(
                                getPrimaryConstructorMandatoryParameters(
                                    kClass,
                                    true
                                )
                            )?.let { it ->
                                ( it as TargetClass).let { targetClassDataInstance ->
                                    val mutableMapOfParams = mutableMapOf<String, String>()

                                    kClass.primaryConstructor
                                        ?.parameters
                                        ?.forEach {
                                            it.name?.also { name ->
                                                mutableMapOfParams[ name ] =
                                                    getParamTypeName( it.type )
                                            }
                                        }

                                    paramsTargetsMap[ targetClassDataInstance ] = mutableListOf(
                                        mutableMapOfParams
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if( BuildConfig.DEBUG )
            logDirectories()
    }



    /**
     * Para construir lo directorios se requieren de los siguientes valores:
     * - FlowName ??? Debe considerarse en todos los objetivos de navegación?
     * - Path del objetivo
     * - Parámetros necesarios para llegar al objetivo
     * - Identificador de actividad objetivo primitivo
     * -- FlowName
     * -- Nombre de la clase que crea el Activity
     */
    private fun analyzeActivityTargetContent(
        targetActivityInstance: TargetActivity,
        kClass: KClass<TargetActivity>,
        navigationTargetGroupInstance: NavigationTargetGroup,
        activityParams: Map<String, String> = mapOf()
    ) {
        val activityPath = "${navigationTargetGroupInstance.prefixPath.trim('/')}/" +
                targetActivityInstance.path.trim('/')

        targetActivityInstance
            .primitiveTargetActivityIdentifier
            .also { primitiveTargetActivityIdentifier ->
                if( activityMap.containsKey( primitiveTargetActivityIdentifier.flowName ).not() )
                // Directorio 4: Registramos la relación entre el flowName y el el ActivityTarget
                    targetActivityInstance.run {
                        // Diccionario 7
                        flowNameTargetMap[ primitiveTargetActivityIdentifier.flowName ] =
                            targetActivityInstance

                        // DIRECTORIO 4
                        activityMap[primitiveTargetActivityIdentifier.flowName] = this

                        // Comprobamos que exista el activity
                        val className = primitiveTargetActivityIdentifier.className
                        try {
                            Class.forName(className)
                        }catch (e: ClassNotFoundException) {
                            Log.w(TAG, "La clase $className no existe")
                        }

                        // Diccionario 3
                        flowNameMap[ primitiveTargetActivityIdentifier.flowName ] =
                            // Hace lo mismo que el diccionario 4
                            className

                        // Diccionario 9
                        primitiveTargetActivityIdentifier.flowNameAlias?.also { flowNameAlias ->
                            if( ! flowNameAlias.disabled)
                                flowNameAliasMap[ flowNameAlias.alias ] =
                                    primitiveTargetActivityIdentifier.flowName
                        }
                    }
            }

        // Si el activityTarget no tiene hijos dentro almacenamos el objetivo
        if(kClass.nestedClasses.size == 0) {
            // DIRECTORIO 6
            routerParamMap[ activityPath ] = activityParams
            // DIRECTORIO 1: Almacenamos todas las rutas usando el Path como llave, los parámetros
            // que puede recibir usando el nombre como llave y el valor corresponde al tipo
            routerParamMap[ activityPath ] = activityParams
            // DIRECTORIO 2: Almacenamos todos los objetivos de navegación usando el Path como llave
            routerMap[ activityPath ] = targetActivityInstance
        }else
            kClass.nestedClasses.forEach { subClassGroup ->
                if( ! paramsTargetsMap.containsKey( targetActivityInstance ) )
                    paramsTargetsMap[ targetActivityInstance ] = mutableListOf()

                when {
                    // Tipo 1.1 Parámetros para lanzar la actividad
                    subClassGroup.isSubclassOf( NavigationTargetParams::class ) -> {

                        // No requiere parámetros
                        if(subClassGroup.isFinal && ! subClassGroup.isData) {
                            val navigationTargetParams:
                                    NavigationTargetParams<NavigationTarget> =
                                subClassGroup.objectInstance
                                        as NavigationTargetParams<NavigationTarget>

                            val navigationTargetPath = "${activityPath.trim('/')}/" +
                                    navigationTargetParams.path.trim('/')

                            routerMap[ navigationTargetPath ] = targetActivityInstance
                            paramsTargetsMap[ targetActivityInstance ]?.add( activityParams )
                            routerParamMap[ navigationTargetPath ] = activityParams
                        }

                        // Requiere parámetros
                        if(subClassGroup.isData) {
                            val primaryConstructorMandatoryParameters =
                                getPrimaryConstructorMandatoryParameters( subClassGroup )
                            val navigationTargetParams:
                                    NavigationTargetParams<NavigationTarget> = subClassGroup
                                    .primaryConstructor
                                    ?.callBy(
                                        primaryConstructorMandatoryParameters
                                    ) as NavigationTargetParams<NavigationTarget>

                            val navigationTargetPath = "${ activityPath.trim('/') }/" +
                                    navigationTargetParams.path.trim('/')

                            routerMap[ navigationTargetPath ] = targetActivityInstance

                            // Agregar los parámetros del padre a los parámetros del hijo
                            val activityParamsMap: MutableMap<String, String> =
                                activityParams.toMutableMap()

                            // Parámetros para lanzar un compose junto a los parámetros de la
                            // actividad que lo contiene
                            subClassGroup.primaryConstructor
                                ?.parameters
                                ?.filterNot { it.isOptional }
                                ?.forEach {
                                    activityParamsMap.put(
                                        it.name !!, getParamTypeName(it.type)
                                    )
                                }

                            // DIRECTORIO 6: Almacenamos la relación entre un objetivo de
                            // navegación y las distintas configuraciones de los
                            // parámetros que recibe.
                            paramsTargetsMap[ targetActivityInstance ]?.add( activityParamsMap )

                            // DIRECTORIO 1: Almacenamos todas las rutas usando el Path como
                            // llave, los parámetros que puede recibir usando el nombre como
                            // llave y el valor corresponde al tipo.
                            routerParamMap[ navigationTargetPath ] = activityParamsMap
                        }
                    }

                    // Tipo 1.2 Parámetros para pasar a COMPOSE
                    subClassGroup.isSubclassOf( TargetCompose::class ) -> {
                        var composePath: String

                        if(subClassGroup.isData) {
                            val primaryConstructorMandatoryParameters =
                                getPrimaryConstructorMandatoryParameters( subClassGroup )
                            (subClassGroup.primaryConstructor
                                ?.callBy(
                                    primaryConstructorMandatoryParameters
                                ) as TargetCompose<TargetActivity>?
                                    )?.also { composeTargetInstance ->

                                composePath = "/${ activityPath.trim('/') }@" +
                                        composeTargetInstance.path.trim('/')

                                // DIRECTORIO 2: Almacenamos todos los objetivos de navegación
                                    // usando el Path como llave
                                routerMap[ composePath ] = composeTargetInstance

                                // Agregar los parámetros del padre a los parámetros del hijo
                                val activityParamsMap: MutableMap<String, String> =
                                    activityParams.toMutableMap()

                                // Parámetros para lanzar un compose junto a los parámetros de la
                                    // actividad que lo contiene
                                subClassGroup
                                    .primaryConstructor
                                    ?.parameters
                                    ?.filterNot { it.isOptional }
                                    ?.forEach {
                                    activityParamsMap.put( it.name !!, getParamTypeName(it.type) )
                                }

                                paramsTargetsMap[ targetActivityInstance ]?.add( activityParamsMap )
                                routerParamMap[ composePath ] = activityParamsMap

                                // Diccionario 7
                                // flowNameTargetMap[ <FLOWNAME DEL COMPOSE> ] = composeTargetInstance
                            }
                        }

                        if(subClassGroup.isFinal && !subClassGroup.isData) {
                            (subClassGroup.objectInstance as TargetCompose<TargetActivity>?)
                                ?.also { composeParamsObject ->
                                composePath = "/${ activityPath.trim('/') }@" +
                                        composeParamsObject.path.trim('/')

                                routerParamMap[ composePath ] = mapOf()

                                // DIRECTORIO 7
                                // flowNameTargetMap[ <FLOWNAME DEL COMPOSE> ] = composeParamsObject
                            }
                        }
                    }
                }
            }
    }

    private fun getPrimaryConstructorMandatoryParameters(
        kClass: KClass<*>,
        includeOptional: Boolean = false
    ) = kClass.primaryConstructor
        ?.parameters
        ?.filterNot { it.isOptional && ! includeOptional }
        ?.associate { it to intanceMandatoryParameter(it) } ?: mapOf()

    private fun intanceMandatoryParameter(param: KParameter): Any = when (param.type.classifier) {
        String::class -> "Texto aleatorio"
        Boolean::class -> false
        Int::class -> 0

        else -> error("Unsupported Type")
    }

    private fun getParamTypeName(type: KType): String = when( type.classifier ) {
        String::class -> "String"
        Boolean::class -> "Boolean"
        Int::class -> "Int"
        else -> "UNKNOWN"
    }
}