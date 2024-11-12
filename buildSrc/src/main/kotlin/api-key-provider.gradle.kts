/**
 * Loads the Phish.in api key from gradle/system properties
 *
 * `never.ending.splendor.phishinApiKey=apiKey` for gradle properties.
 * `PHISH_TAPES_PHISHIN_API_KEY=apiKey` for system property.
 *
 * Can then be used from the project's extras with `phishinApiKey`
 */
loadPropertyIntoExtra(
        extraKey = "phishinApiKey",
        projectPropertyKey = "phishinApiKey",
        systemPropertyKey = "PHISHIN_API_KEY",
        defaultValue = ""
)

loadPropertyIntoExtra(
        extraKey = "phishNetApiKey",
        projectPropertyKey = "phishNetApiKey",
        systemPropertyKey = "PHISH_NET_API_KEY",
        defaultValue = ""
)
