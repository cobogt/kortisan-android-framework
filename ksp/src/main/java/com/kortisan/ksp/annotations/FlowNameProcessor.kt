package com.kortisan.ksp.annotations

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import kotlin.reflect.KClass

/**
 * When called from a module, it looks for all the annotations of flownames and creates the
 * directories for the NavigationController.
 */
class FlowNameProcessor(
    private val environment: SymbolProcessorEnvironment
): SymbolProcessor {
    private fun Resolver.findAnnotations(
        kClass: KClass<*>,
    ) = getSymbolsWithAnnotation(
        kClass.qualifiedName.toString())
        .filterIsInstance<KSClassDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // Detects all flownames for activities
        val listedActivities: Sequence<KSClassDeclaration> =
            resolver.findAnnotations(FlowNameActivity::class)

        if( ! listedActivities.iterator().hasNext() )
            return emptyList()

        val activityNames = listedActivities.map{ it.simpleName.asString() }
        // val sourceFiles = listedActivities.mapNotNull { it.containingFile }

        val fileText = buildString {
            append("// ")
            append(activityNames.joinToString(", "))
        }

        val file = environment
            .codeGenerator
            .createNewFile(
                Dependencies(false),
                "com.kortisan.framework.redux.controllers.navigation.routeExplorer",
                "GeneratedLists",
            )

        file.write(fileText.toByteArray())
        file.close()

        return (listedActivities).filterNot { it.validate() }.toList()
    }
}