package com.kortisan.authentication.presentation

import com.kortisan.framework.redux.controllers.tagging.SceneBuilderBase

class AuthenticationSceneBuilder private constructor(): SceneBuilderBase() {
    class Builder: SceneBuilderBase.Builder() {
        override val taggingScene: SceneBuilderBase = AuthenticationSceneBuilder()
        fun setCurrentFlow( name: String ) = apply {
            taggingScene.baseParams["currentFlowName"] = name
        }
    }

    override fun getData(): Map<String, String> = baseParams
}
