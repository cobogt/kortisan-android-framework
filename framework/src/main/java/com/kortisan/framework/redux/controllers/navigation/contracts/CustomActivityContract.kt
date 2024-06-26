package com.kortisan.framework.redux.controllers.navigation.contracts
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.kortisan.framework.toBundle
import com.kortisan.framework.toMap

/**
 * Contrato para registrar la respuesta de una actividad y pasar estos parámetros a otra actividad
 * que será la que haga la llamada.
 */
class CustomActivityContract(
    // Activity
    private val activityCannonicalName: String
): ActivityResultContract<Map<String, String>, ResultContractStatus>() {
    override fun createIntent( context: Context, input: Map<String, String> ): Intent =
        Intent()
            .setClassName( context, activityCannonicalName )
            .putExtras( input.toBundle() )

    override fun parseResult( resultCode: Int, intent: Intent? ): ResultContractStatus =
        resultCode.let {
            val intentExtras = intent?.extras?.toMap() ?: mapOf()

            when( it ) {
                Activity.RESULT_OK -> ResultContractStatus.Success( it, intentExtras )
                Activity.RESULT_CANCELED -> ResultContractStatus.Error( it )
                else -> ResultContractStatus.CustomStatus( it, intentExtras )
            }
        }
}