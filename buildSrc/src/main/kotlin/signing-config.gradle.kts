/**
 * Plugin to read keystore properties from the system or project properties and add them to the
 * project's extras.
 *
 * If values are not provided default values for the debug keystore are added.
 */

loadPropertyIntoExtra(
    extraKey = "keystorePassword",
    projectPropertyKey = "keystorePassword",
    environmentPropertyKey = "KEYSTORE_PASSWORD",
    defaultValue = "android"
)

loadPropertyIntoExtra(
    extraKey = "aliasKeyPassword",
    projectPropertyKey = "aliasKeyPassword",
    environmentPropertyKey = "KEY_PASSWORD",
    defaultValue = "android"
)

loadPropertyIntoExtra(
    extraKey = "storeKeyAlias",
    projectPropertyKey = "storeKeyAlias",
    environmentPropertyKey = "KEY_ALIAS",
    defaultValue = "androiddebugkey"
)

loadPropertyIntoExtra(
    extraKey = "keystoreLocation",
    projectPropertyKey = "keystoreLocation",
    environmentPropertyKey = "KEYSTORE_LOCATION",
    defaultValue = "keys/debug.keystore"
)
