package never.ending.splendor.app.utils

/**
 * In debug mode wakes up the device and launches the app
 * even over the lock screen, making like as a dev a little
 */
interface DeviceWakeUp {
    fun riseAndShine(): Unit
}
