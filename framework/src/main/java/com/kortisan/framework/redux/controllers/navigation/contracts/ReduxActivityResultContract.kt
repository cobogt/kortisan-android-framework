package com.kortisan.framework.redux.controllers.navigation.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.ExitGateResult
import com.kortisan.framework.toBundle
import com.kortisan.framework.toMap

class ReduxActivityResultContract(
    private val activityClassName: String
): ActivityResultContract<Map<String, String>, ExitGateResult<Bundle?>>() {
    override fun createIntent( context: Context, input: Map<String, String> ): Intent =
        Intent()
            .setClassName( context, activityClassName )
            .putExtras( input.toBundle() )

    /**
     * Convierte el resultado del activity lanzado en una acción Redux
     */
    override fun parseResult(resultCode: Int, intent: Intent?): ExitGateResult<Bundle?> =
        resultCode.let {
            val intentExtras = intent?.extras?.toMap() ?: mapOf()

            val resultAction: ReduxAction? = intentExtras["action"]?.let { actionName ->
                ReduxAction.getAction( actionName, intentExtras )
            }

            when( it ) {
                Activity.RESULT_OK -> ExitGateResult.Success( resultAction, intent?.extras )
                //Activity.RESULT_CANCELED -> ExitGateResult.Fail( resultAction, intent?.extras )
                else -> ExitGateResult.Fail( resultAction, intent?.extras )
            }
        }
}