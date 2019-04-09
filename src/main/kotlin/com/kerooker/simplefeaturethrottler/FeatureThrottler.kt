package com.kerooker.simplefeaturethrottler

import com.kerooker.simplefeaturethrottler.internal.SystemPercentageFetcher

/**
 * Utility class to throttle values per-feature
 *
 * This class enhances the [Throttler] class as it can be configured feature-based. When you're using multiple features
 * that require throttling, this class can be useful.
 *
 * @see [Throttler]
 */
object FeatureThrottler {
    
    internal val featureThrottles = HashMap<String, Double>()
    
    /**
     * Verifies if [featureName] should be throttled
     *
     * If [featureName] was pre-configured, either via [setThrottlePercentage], System Properties or System Environment,
     * this will verify whether you should allow the execution or not. If this feature was never configured, the default
     * throttling is 100% (no execution will be allowed).
     *
     * Example:
     *
     * ```
     *
     * fun initialization() {
     *    FeatureThrottler.setThrottlePercentage("my.feature", 90.0)  // Block 90% of the executions
     * }
     *
     * fun executeNewFunctionThatCallsBackend() {
     *
     *  if (FeatureThrottler.shouldThrottle("my.feature")) return
     *
     *  callBackend()
     *
     * }
     *
     * fun executeAnotherNewFunctionThatCallsBackend() {
     *
     *  if (FeatureThrottler.shouldThrottle("my.other.feature")) return // Configured via Environment variable 'my.other.feature'
     *
     *  callBackend()
     *
     * }
     *
     * ```
     */
    public fun shouldThrottle(featureName: String): Boolean {
        val percentageToThrottle = getThrottlePercentage(featureName)
        return Throttler.shouldThrottle(percentageToThrottle)
    }
    
    /**
     * Configures the throttling percentage of [featureName] to [percentage]
     *
     * When configuring the throttling of a feature, you can use this method to define how much you're going to throttle
     * it. 100.0 means nothing will go through (100% throttling) while 0.0 means allow everything.
     *
     * Another way to define the percentage of features is to use System Environment Variables or System Properties, where
     * the key is [featureName] and the percentage is the value. For example `my.feature=33.3`
     */
    public fun setThrottlePercentage(featureName: String, percentage: Double): Unit {
        featureThrottles[featureName] = percentage
    }
    
    /**
     * Obtains the current throttle percentage for [featureName]
     *
     * This method will return the current throttle value for [featureName]. If it was previously not configured,
     * this will set it's value to 100.0 (deny everything) and return.
     *
     * This method will first look if the feature was already set. If it wasn't, it'll check the System Environment
     * Variables. If the feature is also not there, this will check System Properties. This will only return 100.0
     * if the feature wasn't found in any of those places.
     *
     * @see [setThrottlePercentage]
     */
    public fun getThrottlePercentage(featureName: String): Double {
        return featureThrottles.getOrPut(featureName) {
            fetchPercentageFromSystem(featureName) ?: 100.0
        }
    }
    
    private fun fetchPercentageFromSystem(featureName: String) =
        SystemPercentageFetcher(featureName).fetchFromSystem()
}