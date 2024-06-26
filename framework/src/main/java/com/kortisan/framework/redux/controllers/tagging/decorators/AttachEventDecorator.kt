package com.kortisan.framework.redux.controllers.tagging.decorators
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

class AttachEventDecorator(
    override val sceneDecorator: SceneDecoratorInterface,
    val type: String = ""
): SceneBaseDecorator() {
    init {
        decoratedValues["type"] = type
    }

    fun setType( type: String ) =
        apply { decoratedValues["type"] = type }
}