package com.kerooker.simplefeaturethrottler

import io.kotlintest.matchers.numerics.shouldBeInRange
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class ThrottlerTest : FunSpec() {

    init {
        test("Default throttle should be 0.0 (never throttle)") {
            val throttler = Throttler()
            
            repeat(10_000) {
                throttler.shouldThrottle() shouldBe false
            }
        }
        
        test("Should throttle X% if percentage was set") {
            val throttler = Throttler(percentageToThrottle = 33.3)
            
            var throttled = 0
            var unthrottled = 0
            
            repeat(100_000) {
                if(throttler.shouldThrottle()) throttled++ else unthrottled++
            }
    
            throttled shouldBeInRange 32_000..34_000
            unthrottled shouldBeInRange 65_000..67_000
        }
        
        test("Should throttle 100% if percentage is set to 100%") {
            val throttler = Throttler(percentageToThrottle = 100.0)
            
            repeat(100_000) {
                throttler.shouldThrottle() shouldBe true
            }
        }
        
        test("Should throttle the specified") {
            var throttled = 0
            var unthrottled = 0
    
            repeat(100_000) {
                if(Throttler.shouldThrottle(percentageToThrottle = 33.3)) throttled++ else unthrottled++
            }
    
            throttled shouldBeInRange 32_000..34_000
            unthrottled shouldBeInRange 65_000..67_000
        }
    }

}