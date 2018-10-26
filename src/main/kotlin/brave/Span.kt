package brave

inline fun <T> Span.use(block: Span.() -> T): T {
    return try {
        start()
        block()
    } finally {
        finish()
    }
}

inline fun <T> Span.use(startTimestamp: Long, block: Span.() -> T): T {
    return try {
        start(startTimestamp)
        block()
    } finally {
        finish()
    }
}