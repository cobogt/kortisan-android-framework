package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.kortisan.framework.redux.controllers.gates.BaseGate
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

/**
 * Clase base para empaquetar los comandos y enviarlos a los despachadores
 * @property tagging Decorador de escena en donde ocurre la acción
 * @property revertAction Acción opcional que se encarga de deshacer los efectos de la acción
 * @property gates Lista de Compuertas a analizar antes de que se procese la acción
 */
abstract class ReduxAction {
    /**
     * Decorador de la acción
     * @see SceneDecoratorInterface
     */
    var tagging:      SceneDecoratorInterface? = null

    /**
     * Acción opcional para revertir los efectos de la acción inicial.
     */
    var revertAction: RevertAction? = null

    /**
     * Lista de compuertas a validar antes de que se ejecute, se procesan en el middleware de gates.
     * @see BaseGate Compuerta base para el middleware
     * @see com.kortisan.framework.redux.controllers.gates.GatesController
     */
    open var gates:   List<BaseGate> = listOf()

    /**
     * Sirve para acciones sin un resultado, predeterminadas o vacías.
     */
    object EmptyAction: ReduxAction()

    companion object {
        init {
            // exploreActions( ReduxAction::class.sealedSubclasses )
        }

        // Relación entre la acción y los parámetros necesarios para instanciarla
        private val actionCatalog = mutableMapOf<String, Map<String, String>>()

        /**
         * Buscamos la acción a partir de su nombre y su constructor.
         *
         * Debe regresar una instancia de la acción redux.
         *
         * El catálogo debe contener como llave el nombre del atributo del constructor y como valor
         * debe contener el className del tipo de dato necesario para instanciarlo.
         * En caso de que no sea un tipo nativo se va a buscar la llave como tipo string y el
         * contenido se va a intentar parsear como un json hacia el objeto esperado por la acción.
         *
         * Params contiene la relacción del nombre de parámetro y su valor a utilizar como instancia
         */
        fun getAction(name: String, params: Map<String, String> = mapOf()): ReduxAction {
            actionCatalog
                // Filtramos el nombre.
                .filter { it.key.contains( name ) }
                // Filtramos todos los elementos cuyos parametros de constructor
                // se encuentren en los atributos dados en la llamada al método.
                .filter { params.keys.containsAll( it.value.keys ) }
                // Comprobamos los tipos de datos necesarios para crear la instancia
                // Convertimos los nombres del catálogo a objetos
                .map {
                    Class.forName( it.key ) to it.value
                }
                .toList()
                // Creamos las instancias y las devolvemos
                .map { actionParam ->
                    // Nombre del parámetro: Tipo => Valor
                    val constructorParams: Map<KParameter, Any?> =

                    // Llave: Nombre del parametro
                        // Valor: Tipo de dato
                        actionParam.second.map {
                            val type = Class.forName( it.value )

                            type.kotlin.starProjectedType as KParameter to
                                when( it.key ) {
                                    String::class.simpleName -> params[ it.key ]?: ""
                                    Int::class.simpleName    -> params[ it.key ]?.toInt() ?: 0
                                    Float::class.simpleName  -> params[ it.key ]?.toFloat() ?: 0F
                                    else -> {
                                        try {
                                            // Intentamos convertir a JSON
                                            Gson().fromJson(
                                                params[ it.key ],
                                                type
                                            )
                                        } catch (e: JsonSyntaxException) {
                                            params[ it.key ] ?: ""
                                        }
                                    }
                                }
                        }.toMap()

                    actionParam
                        .first
                        .kotlin
                        .primaryConstructor
                        ?.callBy( constructorParams ) as ReduxAction
                }

            return EmptyAction
        }

        /**
         * Analizamos de forma recursiva todas las subclases de reduxAction
         */
        private fun<T: ReduxAction> exploreActions(subClasses: List<KClass<out T>>) {
            subClasses.forEach {
                it.simpleName?.also { name ->
                    val params = mutableMapOf<String, String>()

                    if( it.isSubclassOf( ReduxAction::class ) ) {
//                        if ( it.isFinal )
//                            stringActionCatalog[ name ] = (it.createInstance() as ReduxAction)
//
                        // Analizamos los constructores de las acciones
                        if( it.isData )
                            it.primaryConstructor
                                ?.parameters
                                ?.filterNot { p -> p.isOptional }
                                ?.forEach { p ->
                                    params[ "${p.name}" ] =
                                        "${p.type.classifier?.javaClass?.simpleName}"
                                }

                        actionCatalog[ name ] = params

                    }

                    exploreActions( it.sealedSubclasses )
                }
            }
        }
    }
}

/**
 * Considerar una entidad llamada ActionProducer que es la que crea la acción
 *
 * Ejemplos de estas entidades es User, Service, Worker, Intent ya que son aquellos que pueden
 * crear acciones y enviarlas al dispatcher.
 * Controlar estos "Actores" o "Productores" de las acciones permite controlar el tipo de respuesta
 * que se va a producir una vez que la acción sea consumida.
 */