package com.kerooker.simplefeaturethrottle

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.system.withEnvironment
import io.kotlintest.extensions.system.withSystemProperty
import io.kotlintest.matchers.numerics.shouldBeInRange
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class FeatureThrottlerTest : FunSpec() {

    init {
        test("Should throttle 100% of an unconfigured feature") {
            repeat(1_000) {
                FeatureThrottler.shouldThrottle(featureName = "my.feature") shouldBe true
            }
        }
        
        test("Should correctly configure throttle percentage for feature") {
            FeatureThrottler.setThrottlePercentage(featureName = "my.feature", percentage = 33.3)
            
            FeatureThrottler.getThrottlePercentage(featureName = "my.feature") shouldBe 33.3
        }
        
        test("Should throttle the specified percentage for the feature") {
            FeatureThrottler.setThrottlePercentage(featureName = "my.feature", percentage = 33.3)
    
            var throttled = 0
            var unthrottled = 0
    
            repeat(100_000) {
                if(FeatureThrottler.shouldThrottle(featureName = "my.feature")) throttled++ else unthrottled++
            }
    
            throttled shouldBeInRange 31_500..34_500
            unthrottled shouldBeInRange 64_500..67_500
        }
        
        test("Should default throttler percentage to 100% for unconfigured features") {
            FeatureThrottler.getThrottlePercentage("my.feature") shouldBe 100.0
        }
        
        test("Should get percentage from System Environment if feature is unset") {
            withEnvironment("my.feature", "36.0") {
                FeatureThrottler.getThrottlePercentage("my.feature") shouldBe 35.0
            }
        }
        
        test("Should get percentage from System Properties if feature is unset") {
            withSystemProperty("my.feature", "35.0") {
                FeatureThrottler.getThrottlePercentage("my.feature") shouldBe 35.0
            }
        }
    }
    
    override fun afterTest(testCase: TestCase, result: TestResult) {
        FeatureThrottler.featureThrottles.clear()
    }
}