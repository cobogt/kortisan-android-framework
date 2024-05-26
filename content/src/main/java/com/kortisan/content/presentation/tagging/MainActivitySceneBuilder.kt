package com.kortisan.content.presentation.tagging

import com.kortisan.framework.redux.controllers.tagging.SceneBuilderBase

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 01/01/23.
 * * * * * * * * * * **/
class MainActivitySceneBuilder private constructor(): SceneBuilderBase() {
    class Builder: SceneBuilderBase.Builder() {
        override val taggingScene: SceneBuilderBase = MainActivitySceneBuilder()

        fun setModuleName( moduleName: String ) = apply {
            taggingScene.baseParams["moduleName"] = moduleName
        }
    }

    override fun getData(): Map<String, String> = baseParams
}