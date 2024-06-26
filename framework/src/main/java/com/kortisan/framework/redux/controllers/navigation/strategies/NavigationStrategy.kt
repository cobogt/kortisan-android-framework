package com.kortisan.framework.redux.controllers.navigation.strategies
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCaller
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.bloc.SingleActivityWithViewModel
import com.kortisan.framework.redux.actions.DefaultErrorAction
import com.kortisan.framework.redux.actions.ExitGateAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.ExitGateResult
import com.kortisan.framework.redux.controllers.navigation.contracts.CustomActivityContract
import com.kortisan.framework.redux.controllers.navigation.contracts.ResultContractStatus
import com.kortisan.framework.redux.controllers.navigation.targets.TargetClass
import com.kortisan.framework.toBundle
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

@SuppressLint("StaticFieldLeak")
object NavigationStrategy: Application.ActivityLifecycleCallbacks {
    private val TAG = javaClass.simpleName
    var currentActivity: Activity? = null

    private fun startActivityContextAware(
        intent: Intent,
        params: Map<String, String> = emptyMap(),
        options: Bundle? = null
    ) {
        // Callback en compose, en caso de que implemente ActivityResultCaller
//        if( currentActivity != null
//            && currentActivity is ActivityResultCaller
//            && currentActivity is SingleActivityWithViewModel )
//                registerActivityContract( (currentActivity as ActivityResultCaller) )
//                    .launch( params )
//        else
        // Agregamos los parámetros al bundle de la actividad
        options?.putAll(
            params.toBundle()
        )

        FrameworkApplicationBinding
            .appContext
            .startActivity( intent, options )
    }

    /**
     * Registra el contrato de la actividad para capturar su resultado
     */
    private fun registerActivityContract( activity: ActivityResultCaller ) =
        activity.registerForActivityResult(
            CustomActivityContract( activity.javaClass.canonicalName !! )
        ) { result ->
            if ( result is ResultContractStatus )
                // Asumimos que las actividades que sean lanzadas por otras actividades
                // son producto de un subflujo.
                ExitGateAction(
                    when ( result ) {
                        is ResultContractStatus.CustomStatus -> ExitGateResult.Success(
                            // En éste caso se puede parsear la respuesta a un nuevo tipo de acción
                            null, result.resultData
                        )
                        is ResultContractStatus.Success -> ExitGateResult.Success(
                            ReduxAction.EmptyAction, result.resultData
                        )
                        is ResultContractStatus.Error -> ExitGateResult.Fail(
                            DefaultErrorAction(""), null
                        )
                    }
                ).also {
                    ( activity as SingleActivityWithViewModel ).singleActivityViewModel
                        .getDispatcher().run {
                            runBlocking { currentAction.firstOrNull() }
                                .also { currentAction: ReduxAction? ->
                                // Despachamos la acción en caso de que la actividad haya muerto
                                if( currentAction == null )
                                    dispatch( it )
                            }
                        }
                }
        }

    /**
     * Iniciamos un activity usando su className
     * Se inicia la actividad a partir del Activity actual
     */
    fun navigateToActivityClassWithoutResult(
        className: String,
        params: Map<String, String>
    ) = try {
        Intent().run {
            setClassName(
                FrameworkApplicationBinding.appContext,
                className
            )

            flags = when (
                params
                .getOrDefault("codeReturn", "-1")
                .toInt()
            ) {
                // Se convierte en el inicio de una nueva pila.
                -11 -> Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                // Si ya existe se trae al frente, se quitan todas las otras que esten
                // encima y se envía el intent.
                -10 -> Intent.FLAG_ACTIVITY_CLEAR_TOP
                // Si ya existe se omite.
                else -> Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            params.forEach { (key, value) -> putExtra(key, value) }

            startActivityContextAware( this, params )
        }
    }catch (e: Exception) {
        Log.e(TAG, "navigateToClass: $className $params " +
                    "Error: ${e.localizedMessage}")
    }

    /**
     * Invocamos un método de una clase existente en el proyecto
     */
    fun navigateToInternalClass(
        target: TargetClass,
        params: Map<String, String>
    ) = try {
            Class.forName( target.classInPackage ).run {
                val instance = getDeclaredConstructor().newInstance()
                return@run methods
                    .firstOrNull { it.isAccessible && it.name == target.method }
                    ?.invoke( instance, params.values.toTypedArray() )
            }
        }catch (e: ClassNotFoundException) {
            Log.e(
                TAG, "navigateToInternalClass " +
                        "ClassNotFoundException $target  $params Error: ${e.localizedMessage}")
        }catch (e: IllegalAccessException) {
            Log.e(
                TAG, "navigateToInternalClass " +
                        "IllegalAccessException $target $params Error: ${e.localizedMessage}")
        }catch (e: Exception) {
            Log.e(
                TAG, "navigateToInternalClass: " +
                        "TARGET: $target PARAMETROS: $params Error: ${e.localizedMessage}")
        }

    /**
     * Inicia un intent implicito con los parámetros indicados
     */
    fun navigateToExternal(
        action: String,
        uri: Uri?,
        params: Map<String, String> = mapOf()
    ) = try {
            val intent = if( uri != null )
                Intent(action, uri)
            else
                Intent(action)

            currentActivity?.also { currentActivity ->
                intent.run {
                    params.forEach { (key, value) ->
                        putExtra(key, value)
                    }

                    currentActivity.startActivityForResult(this, 999)
                }
            }
        }catch (e: Exception) {
            Log.e(
                TAG, "navigateToExternal: " +
                        "$action URI: $uri $params ${e.localizedMessage}")
        }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if( currentActivity != activity )
            currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        if( currentActivity != activity )
            currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        if( currentActivity != activity )
            currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if( currentActivity == activity )
            currentActivity = null
    }

    override fun onActivityStopped(activity: Activity) {
        if( currentActivity == activity )
            currentActivity = null
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        /* ??? */
    }

    override fun onActivityDestroyed(activity: Activity) {
        if( currentActivity == activity )
            currentActivity = null
    }
}