package com.kortisan.framework.redux.controllers.tagging.decorators
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

abstract class SceneBaseDecorator: SceneDecoratorInterface {
    abstract val sceneDecorator: SceneDecoratorInterface
    val decoratedValues: MutableMap<String, String> = mutableMapOf()
    final override fun getData(): Map<String, String> = sceneDecorator.getData() + decoratedValues
}