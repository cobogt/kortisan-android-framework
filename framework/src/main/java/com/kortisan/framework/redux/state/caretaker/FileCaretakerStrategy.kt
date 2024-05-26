package com.kortisan.framework.redux.state.caretaker

import com.kortisan.framework.redux.state.ReduxState
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.kortisan.framework.FrameworkApplicationBinding
import java.io.*

class FileCaretakerStrategy(
    override val defaultState: ReduxState
): CaretakerStrategy() {
    private val gson = Gson()

    override fun persist( currentState: ReduxState ) {
        try {
            val decomposedState = decomposeState( currentState )
            val context = FrameworkApplicationBinding.appContext
            val outputStream = context.openFileOutput(
                decomposedState.qualifiedClassName.replace('.', '_') + ".dat",
                Context.MODE_PRIVATE
            )

            OutputStreamWriter( outputStream ).run {
                write(
                    securityStrategy.encrypt(
                        gson.toJson( decomposedState )
                    )
                )

                close()
            }
        } catch (e: Exception) {
            Log.e("JsonFileHandlerP", "Error saving data to JSON file: $e")
        }
    }

    override fun recover( stateClassName: String ): ReduxState? =
        try {
            val context       = FrameworkApplicationBinding.appContext
            val inputStream   = context.openFileInput(
                stateClassName.replace('.', '_') + ".dat" )
            val reader        = InputStreamReader( inputStream )
            val decryptedData = securityStrategy.decrypt( reader.readText() )

            reader.close()

            recomposeState(
                data = gson.fromJson(
                   decryptedData, RecoverModel::class.java
                )
            )
        } catch (e: Exception) {
            Log.e("JsonFileHandlerR", "Error reading data from JSON file: $e")
            null
        }
}