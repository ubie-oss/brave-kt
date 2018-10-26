package brave

import brave.propagation.TraceContext

inline fun <T> Tracer.scopedSpan(name: String, block: Tracer.() -> T): T {
    val scopedSpan = startScopedSpan(name)
    return try {
        block()
    } finally {
        scopedSpan.finish()
    }
}

inline fun <T> Tracer.scopedSpanWithParent(name: String, parent: TraceContext, block: Tracer.() -> T): T {
    val scopedSpan = startScopedSpanWithParent(name, parent)
    return try {
        block()
    } finally {
        scopedSpan.finish()
    }
}