# brave-kt

![](https://github.com/ubie-inc/brave-kt/actions/workflows/build.yml/badge.svg)

brave-kt is the library that adds Kotlin friendly API for [brave](https://github.com/openzipkin/brave)

## How to use it

### Tracer with ScopedSpan

Previously,

```
val span = Tracing.currentTracer().startScopedSpan()
try {
    // whatever you want to do to trace with span
} finally {
    span.finish()
}
```

Now we can do like this

```kotlin
val result = tracer.scopedSpan("DB ACCESS") {
    // whatever you want to do to trace with span
}
```

You can pass start timestamp, too

```kotlin
tracer.scopedSpan("DB ACCESS", startTimestamp) {
    // whatever you want to do to trace with span
}
```

### Span

Previously,

```
span.start()
try {
    // whatever you want to do to trace with span
} finally {
    span.finish()
}
```

Now we can do like this

```kotlin
span.use {
    // whatever you want to do to trace with span
}
```

You can pass start timestamp, too

```kotlin
span.use(startTimestamp) {
    // whatever you want to do to trace with span
}
```

## How to Install

Use gradle!

```gradle
dependencies {
    compile("io.zipkin.brave:brave:${brave.version}")
    compile("app.ubie:brave-kt:1.0.0")
}
```

You can of course use this library with `spring-cloud-gcp-starter-trace`

```gradle
dependencies {
    compile("org.springframework.cloud:spring-cloud-gcp-starter-trace:${cloudgcp.version}")
    compile("app.ubie:brave-kt:1.0.0")
}
```
