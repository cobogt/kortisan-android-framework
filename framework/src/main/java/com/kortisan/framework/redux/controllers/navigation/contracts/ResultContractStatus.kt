package com.kortisan.framework.redux.controllers.navigation.contracts
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

sealed class ResultContractStatus {
    data class Success(
        val resultCode: Int,
        val resultData: Map<String, String>
    ): ResultContractStatus()

    data class Error(
        val errorCode: Int
    ): ResultContractStatus()

    data class CustomStatus(
        val resultCode: Int,
        val resultData: Map<String, String>
    ): ResultContractStatus()
}
