# Simple-Feature-Throttler

[![Build Status](https://travis-ci.com/Kerooker/SimpleFeatureThrottler.svg?branch=master)](https://travis-ci.com/Kerooker/SimpleFeatureThrottler) [![GitHub](https://img.shields.io/github/license/Kerooker/SimpleFeatureThrottler.svg)](https://github.com/Kerooker/SimpleFeatureThrottler/blob/master/LICENSE) [![Maven Central](https://img.shields.io/maven-central/v/com.kerooker.simplefeaturethrottler/simple-feature-throttler.svg)](https://search.maven.org/search?q=com.kerooker.simplefeaturethrottler)

When adding a new feature to an unstable production environment, a service might want to limit the amount of calls made to external services or procedures. With Simple Feature Throttler you can control this.

## Using in your project

To use Simple Feature Throttler in your project, include the dependency in your `build.gradle`, and you're good to go!

```
implementation("com.kerooker.simplefeaturethrottler:simple-feature-throttler:{currentVersion}")
```

## Available API

To keep it simple, this library provides two ways to throttle your feature: `Throttler` and `FeatureThrottler`.

#### Using Throttler:

The `Throttler` class draws number from a Random pool and checks if the next execution of your feature should be denied or not, based on a throttle percentage:

```kotlin

val throttler = Throttler(33.3) // I'll only allow 33.3% of the executions to go through

fun myFeatureThatShouldBeThrottled() {

    if(throttler.shouldThrottle()) return
    
    callOtherSystem()
}

```

The `Throttler` also contains a utility Companion Object, with a function that mimics its behavior:

```kotlin

fun myFeatureThatShouldBeThrottled() {

    if(Throttler.shouldThrottle(33.3)) return
    
    callOtherSystem()

}
```


#### Using Feature Throttler

The `FeatureThrottler` may be useful if you have more than one feature currently being throttled, and you want more configuration on it. You can configure a feature in 3 different ways:

1. By Environment Variables, with the key being your feature and the value being the throttle percentage, for example: `my.feature=33.3`
2. By the System Properties, in the same fashion as the Environment Variables: `my.feature=33.3`
3. By calling the function `FeatureThrottler.setThrottlePercentage("my.feature", 33.3)`


To use the value you set anywhere in the program, you can simply use `FeatureThrottler.shouldThrottle("my.feature")`


So, for example:

```kotlin

fun myFeatureThatShouldBeThrottled() {

    if(FeatureThrottler.shouldThrottle("my.feature")) return
    
    callBackend()

}
```

If for any reason you need to know what is the current throttling for a given feature, you can use `FeatureThrottler.getThrottlePercentage("my.feature")`
