package com.kerooker.simplefeaturethrottler.internal

import io.kotlintest.extensions.system.withEnvironment
import io.kotlintest.extensions.system.withSystemProperty
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SystemPercentageFetcherTest : StringSpec() {
    
    private val target = SystemPercentageFetcher("my.feature")
    
    init {
        "Return from system environment if present" {
            withEnvironment("my.feature", "35.0") {
                target.fetchFromSystem() shouldBe 35.0
            }
        }
        
        "Return from system properties if present" {
            withSystemProperty("my.feature", "42.0") {
                target.fetchFromSystem() shouldBe 42.0
            }
        }
        
        "Prioritize System Environment if both are present" {
            withEnvironment("my.feature", "35.0") {
                withSystemProperty("my.feature", "42.0") {
                    target.fetchFromSystem() shouldBe 35.0
                }
            }
        }
        
        "Return null if nothing is present on either environment or properties" {
            target.fetchFromSystem() shouldBe null
        }
    }
    
}