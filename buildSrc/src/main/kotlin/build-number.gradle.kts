val defaultBuildNumber: String = properties["phish.tapes.defaultBuildNumber"] as String

loadPropertyIntoExtra(
    extraKey = "buildNumber",
    projectPropertyKey = "buildNumber",
    environmentPropertyKey = "BUILD_NUMBER",
    defaultValue = defaultBuildNumber
)
