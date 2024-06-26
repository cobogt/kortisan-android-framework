package com.kortisan.framework.bloc
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import java.lang.Exception

sealed class Resource<out T> {
    data class Success<T>(val data: T?, val message: String? = ""): Resource<T>()
    data class Error<T>(val exception: Exception, val folio: String? = "", val message: String? = ""): Resource<T>()
    data class Loading<T>(val data: Any? = null): Resource<T>()

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(exception: Exception, folio: String? = "", message: String? = ""): Resource<T> = Error(exception, folio, message)
        fun <T> loading(data: Any? = null): Resource<T> = Loading(data)
    }
}