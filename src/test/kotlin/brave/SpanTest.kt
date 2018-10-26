package brave

import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SpanTest {

    private val span: Span = mockk()

    @Nested
    inner class Use {

        @Test
        fun `normal usecase`() {
            val returnValue = 100

            every { span.start() } returns span
            every { span.finish() } just Runs

            val result = span.use {
                returnValue
            }

            verify(exactly = 1) { span.start() }
            verify(exactly = 1) { span.finish() }
            assertThat(result).isEqualTo(returnValue)
        }

        @Test
        fun `start finish call when Exception thrown`() {
            every { span.start() } returns span
            every { span.finish() } just Runs

            try {
                span.use {
                    throw RuntimeException("Error!!!")
                }
            } catch (e: RuntimeException) {
            }

            verify(exactly = 1) { span.start() }
            verify(exactly = 1) { span.finish() }
        }

    }

    @Nested
    inner class UseWithTime {

        @Test
        fun `normal usecase`() {
            val returnValue = 100

            every { span.start(any()) } returns span
            every { span.finish() } just Runs

            val result = span.use(100L) {
                returnValue
            }

            verify(exactly = 1) { span.start(any()) }
            verify(exactly = 1) { span.finish() }
            assertThat(result).isEqualTo(returnValue)
        }

        @Test
        fun `start finish call when Exception thrown`() {
            every { span.start(any()) } returns span
            every { span.finish() } just Runs

            try {
                span.use(100L) {
                    throw RuntimeException("Error!!!")
                }
            } catch (e: RuntimeException) {
            }

            verify(exactly = 1) { span.start(any()) }
            verify(exactly = 1) { span.finish() }
        }

    }
}