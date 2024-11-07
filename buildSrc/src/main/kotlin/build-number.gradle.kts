val defaultBuildNumber: String = properties["phish.tapes.defaultBuildNumber"] as String

loadPropertyIntoExtra(
    extraKey = "buildNumber",
    projectPropertyKey = "buildNumber",
    systemPropertyKey = "BUILD_NUMBER",
    defaultValue = defaultBuildNumber
)
