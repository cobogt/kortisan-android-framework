package com.kortisan.framework.redux.controllers.remoteConfig.parsers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

/**
 * Objeto para obtener la configuración de etiquetado y para indicar los valores predeterminados
 * a etiquetar por los proveedores.
 * Aquí se carga la configuración del filtrado de parámetros a etiquetar en cada proveedor.
 */
data class TaggingSettingsRemoteConfig(
    val firebaseConfig: TaggingEventConfig = TaggingEventConfig(),
) {
    data class TaggingEventConfig(
        val enabled: Boolean = true,
        val excludedParams: List<String> = listOf(),
        /**
         * Si un parámetro tiene dependencias, no se incluirá en el taggeo a menos que
         * su dependencia también exista, esto aplica de forma recursiva.
         * La lista de dependencias depende, en primera instancia, de los parámetros excluidos.
         */
        val dependencies: List<DependencyRule> = listOf(),
        val logScheme: String? = null,
        /**
         * Cambiamos los nombres de los parametros por el alias
         */
        val alias: Map<String, String> = mapOf(),
    ) {
        /**
         * Filtramos los datos aplicando las exlcusiones y dependencias.
         */
        fun filterData( taggingData: Map<String, String> ): Map<String, String> =
            taggingData
                // Filtramos exclusiones
                .filterNot { excludedParams.contains( it.key ) }
                .mapKeys {
                    alias[ it.key ] ?: it.key
                }
                // Filtramos dependencias
                .let {
                    val paramKeys = it.keys.toList()
                    // Filtramos todas las dependencias que NO sean válidas
                    it.filter { tag ->
                        dependencies.map { dependencyRule ->
                            dependencyRule.validate( tag.key, paramKeys )
                        }.reduce { acc, b -> acc && b }
                    }
                }

        /**
         * Lista recursiva con las reglas para indicar si un parámetro depende de otros.
         */
        data class DependencyRule(
            // Este parámetro debe existir en la lista de parametros a enviar
            val paramName: String,
            // Lista de dependencias de este parámetro
            val dependencies: List<DependencyRule> = listOf()
        ) {
            /**
             * Validamos que, si el parametro coincide con la regla, este exista en la lista de
             * parametros dada, así como las sub dependencias.
             */
            fun validate( param: String, paramList: List<String> ): Boolean {
                // Comprobamos si el parámetro aplica al filtro
                return if( paramName == param ) {
                    // El parametro existe en la lista
                    paramList.contains( param ) && dependencies
                        .map { it.validate( it.paramName, paramList) }
                        .reduce { acc, b -> acc && b }
                } else true
            }
        }
    }
}
