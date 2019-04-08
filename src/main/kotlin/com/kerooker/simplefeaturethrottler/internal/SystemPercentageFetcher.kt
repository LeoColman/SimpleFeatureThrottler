package com.kerooker.simplefeaturethrottler.internal

/**
 * This class is for internal use only. It's used to fetch the key [featureName] from the System
 */
internal class SystemPercentageFetcher(
    private val featureName: String
) {
    
    /**
     * Fetches [featureName] from the System.
     *
     * First tries to see if the key [featureName] is present in the System Environment. If it isn't, this will
     * try to fetch the value from the System Properties. If neither is present, this will return null.
     */
    fun fetchFromSystem(): Double? {
        return percentageFromSystemEnvironment() ?: percentageFromSystemProperties()
    }
    
    private fun percentageFromSystemEnvironment(): Double? {
        return System.getenv().getOrDefault(featureName, null)?.toDoubleOrNull()
    }
    
    private fun percentageFromSystemProperties(): Double? {
        return System.getProperty(featureName, null)?.toDoubleOrNull()
    }
}