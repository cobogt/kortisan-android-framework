#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.storage.security#end

#parse("File Header.java")

/**
 * Estrategia de seguridad
 */
data object ${NAME}SecurityStrategy: SecurityStrategy() {
    override val transformation: String = ""

    override fun encrypt(value: String): String {
        return value
    }

    override fun decrypt(value: String): String {
        return value
    }
}