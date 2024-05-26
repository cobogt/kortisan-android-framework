package com.kortisan.framework.redux.controllers.tagging

import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 01/01/23.
 * * * * * * * * * * **/
open class SceneBuilderBase: SceneDecoratorInterface {
    val baseParams: MutableMap<String, String> = mutableMapOf()

    abstract class Builder {
        abstract val taggingScene: SceneBuilderBase

        fun build(): SceneBuilderBase = taggingScene
    }

    override fun getData(): Map<String, String> = baseParams
}