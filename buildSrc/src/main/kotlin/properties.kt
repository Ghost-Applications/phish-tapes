import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

// namespaces for properties passed to gradle
internal const val projectNamespace = "phish.tapes"
internal const val systemNamespace = "PHISH_TAPES"

/**
 * Loads properties from gradle.properties, system properties or command line.
 *
 * @see [ProjectProperties](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties)
 */
internal fun Project.loadPropertyIntoExtra(
    extraKey: String,
    projectPropertyKey: String,
    environmentPropertyKey: String,
    defaultValue: String
) {
    val namespacedProjectProperty = "$projectNamespace.$projectPropertyKey"
    val namespacedSystemProperty = "${systemNamespace}_$environmentPropertyKey"

    extra[extraKey] = when {
        hasProperty(namespacedProjectProperty) -> properties[namespacedProjectProperty]
        System.getenv(namespacedSystemProperty) != null -> System.getenv(namespacedSystemProperty)
        else -> System.getProperty(namespacedProjectProperty) ?: defaultValue
    }
}
