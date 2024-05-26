package com.kortisan.framework.redux.state.caretaker

import com.kortisan.framework.redux.actions.LoadStateAction
import com.kortisan.framework.redux.state.productionrules.ProductionRule
import com.kortisan.framework.redux.state.ReduxState
import com.kortisan.framework.storage.security.NoSecurityStrategy
import com.kortisan.framework.storage.security.SecurityStrategy
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

/**
 * Esta clase ofrece las estrategias necesarias para persistir un estado redux y poder restaurarlo
 * posteriormente por medio de una acción LoadStateAction en una regla de producción.
 */
sealed class CaretakerStrategy {
    /**
     * Regla de producción para consumir el estado persistente
     */
    open val productionRule: ProductionRule = { currentState, action ->
        if ( action is LoadStateAction )
            recover(currentState.javaClass.superclass?.canonicalName ?: "" )
                ?: defaultState
        else
            currentState
    }

    /**
     * Estado predeterminado en caso de que falle el proceso de recuperación de estado de la
     * estrategia.
     */
    abstract val defaultState: ReduxState

    /**
     * Utilizada para encriptar la información
     */
    open val securityStrategy: SecurityStrategy = NoSecurityStrategy

    /**
     * Almacena un estado en un medio persistente
     *
     * El estado actual es almacenado utilizando su nombre cualificado de clase como llave y el
     * contenido de los atributos de la instancia se extraen mediante reflexión.
     */
    abstract fun persist( currentState: ReduxState )

    /**
     * Recupera un estado de un medio persistente
     * Si la instancia no es reconocida por la estrategia, se devuelve nulo.
     * Se intenta crear una instancia mediante reflexión de la clase cualificada indicada.
     */
    abstract fun recover( stateClassName: String ): ReduxState?

    /**
     * Función para descomponer un estado en dos elementos utilizables por la estrategia
     * de persitencia.
     *
     * Devuelve un par de elementos:
     * Nombre de la clase de forma qualificada
     * Mapa de parámetros para crear el estado
     *    Nombre del campo
     *    Valor del campo
     */
    protected fun decomposeState( state: ReduxState ) =
        getRecoverModelFromInstance( state )

    /**
     * Crea una instancia de forma recursiva de RecoverModel analizando los parámetros primitivos
     * del constructor o en su defecto los objetos compuestos.
     */
    private fun getRecoverModelFromInstance( data: Any ): RecoverModel {
        val recoverModel = RecoverModel(
            data.javaClass.canonicalName ?: ""
        ).apply {
            val parameters = getConstructorParams( data )

            if( parameters.count() == 1 && knownPrimitiveTypes.contains( parameters.first().first ))
                primitiveValue = parameters.first().second?.toString() ?: ""
            else
                compoundValues.putAll(
                    parameters
                        .filter {
                            null != it.second
                        }
                        .associate {
                            it.first to getRecoverModelFromInstance( it.second !! )
                        }
                )
        }

        return recoverModel
    }

    private val knownPrimitiveTypes = arrayOf(
        String.Companion::class.java.canonicalName,
        Boolean.Companion::class.java.canonicalName,
        Int.Companion::class.java.canonicalName,
    )

    /**
     * Obtenemos los valores necesarios para crear una instancia dada
     * El mapa regresa una asociación:
     * Nombre, Valor, Tipo,
     */
    private fun getConstructorParams( objInstance: Any ): List<Triple<String, Any?, String>> =
        objInstance.javaClass.kotlin.
            primaryConstructor?.
            parameters
            ?.map {
                Triple(
                    it.name ?: "",
                    objInstance
                        .javaClass
                        .getField( "${it.name}" )
                        [ objInstance ],
                    it.type.javaClass.canonicalName ?: ""
                )
            } ?: emptyList()

    /**
     * Función opuesta a decomposeState que permite crear las instancias de los estados a partir
     * de su nombre y atributos.
     *
     * Busca en los constructores del estado para identificar la combinación de parámetros que
     * permitan crear la instancia.
     */
    protected fun recomposeState(
        data: RecoverModel
    ): ReduxState? =
        try {
            getRecoverModelInstance( data ) as ReduxState?
        }catch (e: Exception) {
            null
        }

    /**
     * Crea una instancia a partir de un modelo de recuperación con todos los valores
     * de su constructor primario definidos en el modelo.
     */
    private fun getRecoverModelInstance( data: RecoverModel ) =
        Class.forName( data.qualifiedClassName )
            .kotlin
            .primaryConstructor?.let { pConstructor ->
                pConstructor.callBy(
                    getConstructorValues( pConstructor, data )
                )
            }

    private fun getConstructorValues( constructor: KFunction<Any>, data: RecoverModel ):
        Map<KParameter, Any?> = constructor.parameters
            .filterNot { it.isOptional }
            .associateWith {
                it.javaClass.canonicalName
            }
            .mapValues {
                when( it.value ) {
                    String.Companion::class.java.canonicalName ->
                        data.primitiveValue?: ""

                    Boolean.Companion::class.java.canonicalName ->
                        data.primitiveValue?.toBoolean() ?: false

                    Int.Companion::class.java.canonicalName ->
                        data.primitiveValue?.toInt() ?: 0

                    else ->
                        data.compoundValues[ it.value ?: "" ]?.let { rModel ->
                            getRecoverModelInstance( rModel )
                        }
                }
            }

    /**
     * Clase para recuperar la información del JSON almacenado
     */
    protected class RecoverModel (
        val qualifiedClassName: String,
        var primitiveValue:     String? = null,
        // FieldName, FieldValue
        var compoundValues:      MutableMap<String, RecoverModel> = mutableMapOf()
    )
}
