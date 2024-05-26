#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.presentation.tagging.decorators#end

import com.kortisan.framework.redux.controllers.tagging.decorators.SceneBaseDecorator
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface

#parse("File Header.java")
class ${NAME}SceneDecorator(
    override val sceneDecorator: SceneDecoratorInterface,
    // Option 1: Decorate from constructor
    val demoParam: String = ""
): SceneBaseDecorator() {
    init {
        decoratedValues["demoParam"] = demoParam
    }

    // Option 2: Decorate from builder
    fun setParam( demoParam: String ) =
        apply { 
            decoratedValues["demoParam"] = demoParam
        }
}