#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}#end

#parse("File Header.java")
@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.SOURCE )
annotation class ${NAME}Annotation(
    val param1: String
)