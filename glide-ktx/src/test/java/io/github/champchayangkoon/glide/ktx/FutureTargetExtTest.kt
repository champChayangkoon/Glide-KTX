package io.github.champchayangkoon.glide.ktx

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import io.github.champchayangkoon.glide.ktx.utils.MainCoroutineRule
import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
internal class FutureTargetExtTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    private lateinit var context: Context

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(Glide::class)
    }

    @After
    fun teardown() {
        unmockkStatic(Glide::class)
    }

    @Test
    fun `Should return result by resource type when getOnCoroutine success`() {
        mainCoroutineRule.runBlockingTest {
            val url = "www.test.com/image.jpg"
            val mockDrawable = mockk<Drawable>()
            every {
                Glide.with(context)
                    .load(url)
                    .submit()
                    .get()
            } returns mockDrawable

            val drawable = Glide.with(context)
                .load(url)
                .submit()
                .getOnCoroutine(testDispatcher)

            Truth.assertThat(drawable).isEqualTo(mockDrawable)
        }
    }

    @Test
    fun `Should throw Exception when getOnCoroutine failed`() {
        mainCoroutineRule.runBlockingTest {
            val url = "www.test.com/image.jpg"
            val exceptionMessage =
                "Unable to resolve host test.com: No address associated with hostname"
            every {
                Glide.with(context)
                    .load(url)
                    .submit()
                    .get()
            } throws UnknownHostException(exceptionMessage)

            val actualException = assertFailsWith<UnknownHostException> {
                Glide.with(context)
                    .load(url)
                    .submit()
                    .getOnCoroutine(testDispatcher)
            }

            Truth.assertThat(actualException.message).isEqualTo(exceptionMessage)
        }
    }

    @Test
    fun `Should return result by resource type when getOnCoroutine success and within specify time`() {
        mainCoroutineRule.runBlockingTest {
            val url = "www.test.com/image.jpg"
            val mockDrawable = mockk<Drawable>()
            val timeout = 10L

            every {
                Glide.with(context)
                    .load(url)
                    .submit()
                    .get(timeout,TimeUnit.MILLISECONDS)
            } returns mockDrawable

            val drawable = Glide.with(context)
                .load(url)
                .submit()
                .getOnCoroutine(timeout, TimeUnit.MILLISECONDS, testDispatcher)

            Truth.assertThat(drawable).isEqualTo(mockDrawable)
        }
    }

    @Test
    fun `Should throw TimeoutException if can not get resource within specify time`() {
        mainCoroutineRule.runBlockingTest {
            val url = "www.test.com/image.jpg"
            val timeoutException = TimeoutException("time out")
            val timeout = 10L

            every {
                Glide.with(context)
                    .load(url)
                    .submit()
                    .get(timeout,TimeUnit.MILLISECONDS)
            } throws timeoutException

            val actualException = assertFailsWith<TimeoutException> {
                Glide.with(context)
                    .load(url)
                    .submit()
                    .getOnCoroutine(timeout, TimeUnit.MILLISECONDS, testDispatcher)
            }

            Truth.assertThat(actualException.message).isEqualTo("time out")
        }
    }
}