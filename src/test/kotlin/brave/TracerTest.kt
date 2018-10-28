package brave

import brave.propagation.TraceContext
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TracerTest {
    private val scopedSpan: ScopedSpan = mockk()
    private val tracer: Tracer = mockk()
    private val parent: TraceContext = mockk()

    @Nested
    inner class ScopedSpanTest {
        @Test
        fun `normal usecase`() {
            val returnValue = 100

            every { tracer.startScopedSpan(any()) } returns scopedSpan
            every { scopedSpan.finish() } just Runs

            val result = tracer.scopedSpan("foo") {
                returnValue
            }

            verify(exactly = 1) { tracer.startScopedSpan(any()) }
            verify(exactly = 1) { scopedSpan.finish() }
            Assertions.assertThat(result).isEqualTo(returnValue)
        }

        @Test
        fun `start finish call when Exception thrown`() {
            every { tracer.startScopedSpan(any()) } returns scopedSpan
            every { scopedSpan.finish() } just Runs

            try {
                tracer.scopedSpan("foo") {
                    throw RuntimeException("Error!!!")
                }
            } catch (e: RuntimeException) {
            }

            verify(exactly = 1) { tracer.startScopedSpan(any()) }
            verify(exactly = 1) { scopedSpan.finish() }
        }
    }

    @Nested
    inner class ScopedSpanTestWithParent {
        @Test
        fun `normal usecase`() {
            val returnValue = 100

            every { tracer.startScopedSpanWithParent(any(), any()) } returns scopedSpan
            every { scopedSpan.finish() } just Runs

            val result = tracer.scopedSpanWithParent("foo", parent) {
                returnValue
            }

            verify(exactly = 1) { tracer.startScopedSpanWithParent(any(), any()) }
            verify(exactly = 1) { scopedSpan.finish() }
            Assertions.assertThat(result).isEqualTo(returnValue)
        }

        @Test
        fun `start finish call when Exception thrown`() {
            every { tracer.startScopedSpanWithParent(any(), any()) } returns scopedSpan
            every { scopedSpan.finish() } just Runs

            try {
                tracer.scopedSpanWithParent("foo", parent) {
                    throw RuntimeException("Error!!!")
                }
            } catch (e: RuntimeException) {
            }

            verify(exactly = 1) { tracer.startScopedSpanWithParent(any(), any()) }
            verify(exactly = 1) { scopedSpan.finish() }
        }
    }
}