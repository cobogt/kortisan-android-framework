#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.presentation.tagging#end

import com.kortisan.framework.redux.controllers.tagging.SceneBuilderBase

#parse("File Header.java")
class ${NAME}SceneBuilder private constructor(): SceneBuilderBase() {
    class Builder: SceneBuilderBase.Builder() {
        override val taggingScene: SceneBuilderBase = ${NAME}SceneBuilder()

        fun setRootName( name: String ) = apply {
            taggingScene.baseParams["rootName"] = name
        }
    }

    override fun getData(): Map<String, String> = baseParams
}