package com.herargos.herargosadmistrativo

/**
 * Represents the platform the application is currently running on.
 */
interface Platform {
    /**
     * The name of the current platform (e.g., "Android", "iOS", "Desktop").
     */
    val name: String
}

/**
 * Gets the platform-specific implementation of [Platform].
 *
 * @return An instance of [Platform] for the current platform.
 */
expect fun getPlatform(): Platform
