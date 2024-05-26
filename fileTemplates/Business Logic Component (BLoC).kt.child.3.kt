#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.domain.models#end

#parse("File Header.java")
data class ${NAME}Model(
    val demoText: String = "Hello, I'am ${NAME}",
    val demoInt: Int = 123,
)