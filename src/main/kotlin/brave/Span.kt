package brave

/**
 * Kotlin friendly syntax for start/finish brave.Span
 *
 * Normally, brave needs to call start/finish like this
 * ```
 * span.start()
 * // do whatever we want to trace
 * span.finish()
 * ```
 * Now, this method helps us write like this
 *
 * ```
 * span.use {
 *   // do whatever we want to trace
 * }
 * ```
 *
 */
inline fun <T> Span.use(block: Span.() -> T): T {
    return try {
        start()
        block()
    } finally {
        finish()
    }
}

/**
 * Kotlin friendly syntax for start/finish brave.Span
 *
 * Normally, brave needs to call start/finish like this
 * ```
 * span.start(startTimestamp)
 * // do whatever we want to trace
 * span.finish()
 * ```
 * Now, this method helps us write like this
 *
 * ```
 * span.use(startTimestamp) {
 *   // do whatever we want to trace
 * }
 * ```
 *
 */
inline fun <T> Span.use(startTimestamp: Long, block: Span.() -> T): T {
    return try {
        start(startTimestamp)
        block()
    } finally {
        finish()
    }
}