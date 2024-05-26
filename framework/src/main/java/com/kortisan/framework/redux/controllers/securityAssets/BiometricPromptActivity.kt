package com.kortisan.framework.redux.controllers.securityAssets

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.actions.ExitGateAction
import com.kortisan.framework.redux.controllers.gates.ExitGateResult
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL

class BiometricPromptActivity: AppCompatActivity() {
    private val TAG: String = javaClass.simpleName
    private lateinit var biometricPrompt: BiometricPrompt

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricPrompt = createBiometricPrompt()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        //val cryptoObject: CryptoObject? = null //androidx.biometric.BiometricPrompt.CryptoObject()

        val promptInfo = createPromptInfo()
        if (BiometricManager.from(context).canAuthenticate(
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                ) == BiometricManager.BIOMETRIC_SUCCESS) {
            biometricPrompt.authenticate( promptInfo )
//            biometricPrompt.authenticate(promptInfo, cryptoObject)
        } else {
//            loginWithPassword()
            ApplicationActionDispatcher.dispatch(
                ExitGateAction(
                    ExitGateResult.Fail(
                        null, null
                    )
                )
            )

//            finish()
        }
        return super.onCreateView(name, context, attrs)
    }

    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "$errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {

//                    finish()
                }

                ApplicationActionDispatcher.dispatch(
                    ExitGateAction(
                        ExitGateResult.Fail(
                            null, null
                        )
                    )
                )
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication failed for an unknown reason")
                ApplicationActionDispatcher.dispatch(
                    ExitGateAction(
                        ExitGateResult.Fail(
                            null, null
                        )
                    )
                )
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "Authentication was successful")
                // Proceed with viewing the private encrypted message.
                ApplicationActionDispatcher.dispatch(
                    ExitGateAction(
                        ExitGateResult.Success(
                            null, null
                        )
                    )
                )
//                finish()
            }
        }

//        val bPrompt = android.hardware.biometrics.BiometricPrompt
//            .Builder( this )
//
//            .build()

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            callback)

        return biometricPrompt
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Titulo")
            .setSubtitle("Subtitulo")
            .setDescription("Descripción")
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText("Usar password")
            .build()

        return promptInfo
    }
}