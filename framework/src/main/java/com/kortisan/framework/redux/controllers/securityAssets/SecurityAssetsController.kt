package com.kortisan.framework.redux.controllers.securityAssets

import android.content.Intent

import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.redux.controllers.navigation.strategies.NavigationStrategy
import com.kortisan.framework.redux.controllers.navigation.targets.DashboardNavigationGroup

class SecurityAssetsController: ReduxControllerAbstract() {
    private val TAG: String = javaClass.simpleName
    fun askForBiometrics() {
        if( checkBiometricsAvailable() )
            ApplicationActionDispatcher.dispatch(
                NavigationActions.NavigateToTarget(
                    DashboardNavigationGroup.AskBiometrics
                )
            )
    }

    fun checkBiometricsAvailable(): Boolean {
        val biometricManager = BiometricManager.from( FrameworkApplicationBinding.appContext )
        when (biometricManager.canAuthenticate(
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                promptUserToCreateCredentials()
            }
//            BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {}
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {}
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {}
        }
        return false
    }

    fun promptUserToCreateCredentials() {
        val biometricEnrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        }

        NavigationStrategy.currentActivity?.run {
            startActivityForResult(
                biometricEnrollIntent,
                300
            )
        }
    }
}