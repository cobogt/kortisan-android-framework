package com.kortisan.framework.redux.controllers.tagging.builders

import com.kortisan.framework.redux.controllers.tagging.SceneBuilderBase

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 02/01/23.
 * * * * * * * * * * **/
class EntryPointSceneBuilder private constructor(): SceneBuilderBase() {
    class Builder private constructor (): SceneBuilderBase.Builder() {

        override val taggingScene: SceneBuilderBase = EntryPointSceneBuilder()

        init {
            taggingScene.baseParams.putAll(
                mapOf(
                    "moduleName" to "",
                    "flowName"   to "",
                    "eventName"  to "",
                    "origin"     to "",
                )
            )
        }

        constructor ( moduleName: String ): this() {
            setModuleName( moduleName )
        }

        fun setModuleName( moduleName: String ) = apply {
            taggingScene.baseParams["moduleName"]  = moduleName
        }

        fun setFlowName( flowName: String ) = apply {
            taggingScene.baseParams["flowName"]    = flowName
        }

        fun setEvent( event: String ) = apply {
            taggingScene.baseParams["eventName"]    = event
        }

        fun setOrigin( origin: String ) = apply {
            taggingScene.baseParams["origin"]       = origin
        }
    }
}