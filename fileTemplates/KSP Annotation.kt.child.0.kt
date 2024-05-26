package com.kortisan.ksp.annotations

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import kotlin.reflect.KClass

#parse("File Header.java")
class ${NAME}Processor(
    private val environment: SymbolProcessorEnvironment
): SymbolProcessor {
    private fun Resolver.findAnnotations(
        kClass: KClass<*>,
    ) = getSymbolsWithAnnotation(
        kClass.qualifiedName.toString())
        .filterIsInstance<KSClassDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val listedAnnotations: Sequence<KSClassDeclaration> =
            resolver.findAnnotations(${NAME}Annotation::class)

        if( ! listedAnnotations.iterator().hasNext() )
            return emptyList()

        val listedNames = listedAnnotations.map{ it.simpleName.asString() }

        val fileText = buildString {
            append("// ")
            append(listedNames.joinToString(", "))
        }

        val file = environment
            .codeGenerator
            .createNewFile(
                Dependencies(false),
                "com.kortisan.framework.<PACKAGE OF FILE>",
                "${NAME}Generated",
            )

        file.write(fileText.toByteArray())
        file.close()

        return (listedAnnotations).filterNot { it.validate() }.toList()
    }
}