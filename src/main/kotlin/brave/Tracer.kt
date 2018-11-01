package brave

import brave.propagation.TraceContext

/**
 * Kotlin friendly syntax for start/finish brave.ScopedSpan
 *
 * Normally, brave needs to call start/finish like this
 * ```
 * Tracer.startScopedSpan("DB Access")
 * // do whatever we want to trace
 * span.finish()
 * ```
 * Now, this method helps us write like this
 *
 * ```
 * span.use("DB Access") {
 *   // do whatever we want to trace
 * }
 * ```
 *
 */
inline fun <T> Tracer.scopedSpan(name: String, block: Tracer.() -> T): T {
    val scopedSpan = startScopedSpan(name)
    return try {
        block()
    } finally {
        scopedSpan.finish()
    }
}

/**
 * Kotlin friendly syntax for start/finish brave.ScopedSpan
 *
 * Normally, brave needs to call start/finish like this
 * ```
 * Tracer.startScopedSpan("DB Access", parent)
 * // do whatever we want to trace
 * span.finish()
 * ```
 * Now, this method helps us write like this
 *
 * ```
 * span.use("DB Access", parent) {
 *   // do whatever we want to trace
 * }
 * ```
 *
 */
inline fun <T> Tracer.scopedSpanWithParent(name: String, parent: TraceContext, block: Tracer.() -> T): T {
    val scopedSpan = startScopedSpanWithParent(name, parent)
    return try {
        block()
    } finally {
        scopedSpan.finish()
    }
}