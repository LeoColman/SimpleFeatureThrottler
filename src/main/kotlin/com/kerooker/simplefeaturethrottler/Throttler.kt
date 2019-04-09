package com.kerooker.simplefeaturethrottler

import com.kerooker.simplefeaturethrottler.Throttler.Companion
import kotlin.random.Random

/**
 * Utility class to throttle executions based on [percentageToThrottle]
 *
 * The Throttler class is used to draw numbers from a random pool and use them to verify if you should execute
 * a specific action or if it should be throttled.
 *
 * The companion object contains a single method, [Companion.shouldThrottle], which will draw from a random pool
 * to see if the percentage is hit.
 *
 * @param [percentageToThrottle] How much percent of the requests should be denied (throttled). 100.0 means 100% (throttle all)
 *
 * @see [FeatureThrottler]
 */
public class Throttler(
    private val percentageToThrottle: Double = 0.0
) {
    
    /**
     * Verify if the next call should be throttled
     *
     * This method should be used to verify if you want to block the next call or if it should be allowed to execute.
     *
     * Example:
     *
     * ```
     * val throttler = Throttler(90.0)  // Blocks 90% of the executions
     *
     * fun executeNewFunctionThatCallsBackend() {
     *
     *     if(throttler.shouldThrottle()) return
     *
     *     callBackend()
     *
     * }
     * ```
     *
     * @see [Companion.shouldThrottle]
     */
    public fun shouldThrottle(): Boolean = shouldThrottle(percentageToThrottle)
    
    public companion object {
    
        /**
         * Verify if the next call should be throttled
         *
         * This method should be used to verify if you want to block the next call or if it should be allowed to execute.
         * This is a static option to [Throttler.shouldThrottle], as it doesn't require the instantiation of a [Throttler].
         *
         * Example:
         *
         * ```
         *
         * fun executeNewFunctionThatCallsBackend() {
         *
         *     if(Throttler.shouldThrottle(90.0)) return    // Blocks 90% of the executions
         *
         *     callBackend()
         *
         * }
         * ```
         *
         * @see [Throttler]
         * @see [Throttler.shouldThrottle]
         */
        public fun shouldThrottle(percentageToThrottle: Double): Boolean {
            return (100 * Random.nextDouble()) <= percentageToThrottle
        }
    }
    
}
