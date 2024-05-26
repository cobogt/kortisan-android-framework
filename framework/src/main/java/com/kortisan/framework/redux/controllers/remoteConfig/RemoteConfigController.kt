package com.kortisan.framework.redux.controllers.remoteConfig
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.*
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.redux.actions.RemoteConfigActions
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.redux.controllers.ReduxControllerState
import com.kortisan.framework.redux.controllers.remoteConfig.parsers.RemoteConfigDirectoryMap

class RemoteConfigController: ReduxControllerAbstract() {
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private val _objectMap: MutableMap<String, Any?> = mutableMapOf()

    override fun start() {
        FirebaseApp.initializeApp(FrameworkApplicationBinding.appContext)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        firebaseRemoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings
                .Builder()
                .setMinimumFetchIntervalInSeconds( 0 )
                .setFetchTimeoutInSeconds( 30 )
                .build()
        )

        firebaseRemoteConfig.setDefaultsAsync(
            RemoteConfigDirectoryMap.defaultAsyncMap
        )

        firebaseRemoteConfig.ensureInitialized().run {
            addOnSuccessListener {
                setStartStatus( ReduxControllerState.Started )
                reloadRemoteConfig()
            }

            addOnFailureListener {
                reduceApplicationState(
                    RemoteConfigActions.StoreRemoteConfigAction.LoadFailAction( it )
                )

                setStartStatus( ReduxControllerState.ErrorOnStart(
                "Error al inicializar: ${it.localizedMessage}", it
                ))
            }
        }
    }

    fun reloadRemoteConfig() {
        firebaseRemoteConfig.fetchAndActivate().apply {
            addOnCompleteListener {
                if ( it.isSuccessful ) {
                    loadRemoteValues()

                    reduceApplicationState(
                        RemoteConfigActions.StoreRemoteConfigAction.SuccessLoadAction( _objectMap )
                    )
                }
            }

            addOnFailureListener {
                // Error al obtener la información, se convierte en caché
                reduceApplicationState(
                    RemoteConfigActions.StoreRemoteConfigAction.RefreshFailAction( it )
                )
            }
        }
    }

    private fun loadRemoteValues() {
        RemoteConfigDirectoryMap.parserMap.forEach { (key, _) ->
            val fetchedObject = try {
                when (RemoteConfigDirectoryMap.parserMap[ key ]) {
                    JsonObject::class -> firebaseRemoteConfig.getString(key).let {
                        try { JsonParser.parseString(it).asJsonObject }
                        catch (e: JsonParseException) { JsonObject() }
                        catch (e: JsonSyntaxException) { JsonObject() }
                    }
                    String::class -> firebaseRemoteConfig.getString(key)
                    Boolean::class -> firebaseRemoteConfig.getBoolean(key)
                    Long::class -> firebaseRemoteConfig.getLong(key)
                    else -> Gson()
                        .fromJson(
                            firebaseRemoteConfig.getString(key),
                            RemoteConfigDirectoryMap.parserMap[ key ]?.javaObjectType
                        )
                }
            } catch (e: JsonSyntaxException) {
                null // Error de sintaxis en JSON
            } catch (e: Exception) {
                null // Excepción desconocida al cargar valores remoto
            }

            fetchedObject?.run { _objectMap[ key ] = fetchedObject }
        }
    }
}
