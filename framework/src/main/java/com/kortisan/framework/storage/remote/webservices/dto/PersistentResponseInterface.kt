package com.kortisan.framework.storage.remote.webservices.dto
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

interface PersistentResponseInterface {
    fun<T> toDatabaseModel(): T
    fun<S> toDatastore(): S
}