package io.github.champchayangkoon.glide.ktx

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import io.github.champchayangkoon.glide.ktx.utils.MainCoroutineRule
import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class RequestBuilderExtTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var mockContext: Context

    @RelaxedMockK
    private lateinit var mockImageView: ImageView

    @RelaxedMockK
    private lateinit var requestBuilder: RequestBuilder<Drawable>

    @MockK
    private lateinit var mockCustomTarget: CustomTarget<Drawable>

    @MockK
    private lateinit var mockCustomViewTarget: CustomViewTarget<ImageView, Drawable>

    @MockK
    private lateinit var mockResource: Drawable

    @MockK
    private lateinit var mockTransition: Transition<in Drawable>

    @MockK
    private lateinit var mockPlaceholderDrawable: Drawable

    @MockK
    private lateinit var mockErrorDrawable: Drawable

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
    fun `Should called onResourceReady and return RequestBuilder when load resource success with set listener`() {
        val mockUrl = "www.example.com/image.jpg"
        val mockTarget = mockk<Target<Drawable>>()
        val mockDataSource = mockk<DataSource>()
        val mockIsFirstResource = true

        every {
            requestBuilder.listener(any<RequestListener<Drawable>>())
        } answers {
            firstArg<RequestListener<Drawable>>().onResourceReady(
                mockResource,
                mockUrl,
                mockTarget,
                mockDataSource,
                mockIsFirstResource
            )
            requestBuilder
        }

        val actualRequestBuilder =
            requestBuilder.listener({ resource, model, target, datasource, isFirstResource ->
                Truth.assertThat(resource).isEqualTo(mockResource)
                Truth.assertThat(model).isEqualTo(mockUrl)
                Truth.assertThat(target).isEqualTo(mockTarget)
                Truth.assertThat(datasource).isEqualTo(mockDataSource)
                Truth.assertThat(isFirstResource).isTrue()
                true
            }, { exception, model, target, isFirstResource ->
                true
            })

        Truth.assertThat(requestBuilder).isEqualTo(actualRequestBuilder)
    }

    @Test
    fun `Should called onLoadFailed and return RequestBuilder when load resource failed with set listener`() {
        val mockUrl = "www.example.com/image.jpg"
        val mockException = mockk<GlideException>()
        val mockTarget = mockk<Target<Drawable>>()
        val mockIsFirstResource = true

        every {
            requestBuilder.listener(any<RequestListener<Drawable>>())
        } answers {
            firstArg<RequestListener<Drawable>>().onLoadFailed(
                mockException,
                mockUrl,
                mockTarget,
                mockIsFirstResource
            )
            requestBuilder
        }

        val actualRequestBuilder =
            requestBuilder.listener({ resource, model, target, datasource, isFirstResource ->
                true
            }, { exception, model, target, isFirstResource ->
                Truth.assertThat(exception).isEqualTo(mockException)
                Truth.assertThat(model).isEqualTo(mockUrl)
                Truth.assertThat(target).isEqualTo(mockTarget)
                Truth.assertThat(isFirstResource).isTrue()
                true
            })

        Truth.assertThat(requestBuilder).isEqualTo(actualRequestBuilder)
    }

    @Test
    fun `Should called onResourceReady and return CustomTarget when load resource success with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onResourceReady(mockResource, mockTransition)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget({ resource, transition ->
            Truth.assertThat(resource).isEqualTo(mockResource)
            Truth.assertThat(transition).isEqualTo(mockTransition)
        })

        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onLoadFailed and return CustomTarget when load resource failed with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onLoadFailed(mockErrorDrawable)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget({ _, _ -> }, {
            Truth.assertThat(it).isEqualTo(mockErrorDrawable)
        })
        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onLoadStarted when load is started with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onLoadStarted(mockPlaceholderDrawable)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget({ _, _ -> }, {}, {
            Truth.assertThat(it).isEqualTo(mockPlaceholderDrawable)
        })
        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onLoadCleared when load is cancelled and its resources are freed with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onLoadCleared(mockPlaceholderDrawable)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget({ _, _ -> }, {}, {}, {
            Truth.assertThat(it).isEqualTo(mockPlaceholderDrawable)
        })
        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }


    @Test
    fun `Should called onResourceReady and return CustomTarget when load resource with specify size success with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onResourceReady(mockResource, mockTransition)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget(
            100,
            100,
            { resource, transition ->
                Truth.assertThat(resource).isEqualTo(mockResource)
                Truth.assertThat(transition).isEqualTo(mockTransition)
            })

        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onLoadFailed and return CustomTarget when load resource with specify size failed with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onLoadFailed(mockErrorDrawable)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget(
            100,
            100,
            { _, _ -> }, {
                Truth.assertThat(it).isEqualTo(mockErrorDrawable)
            })
        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onLoadStarted when load with specify size is started with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onLoadStarted(mockPlaceholderDrawable)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget(
            100,
            100,
            { _, _ -> }, {}, {
                Truth.assertThat(it).isEqualTo(mockPlaceholderDrawable)
            })
        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onLoadCleared when load with specify size is cancelled and its resources are freed with intoCustomTarget`() {
        every {
            requestBuilder.into(any<CustomTarget<Drawable>>())
        } answers {
            firstArg<CustomTarget<Drawable>>().onLoadCleared(mockPlaceholderDrawable)
            mockCustomTarget
        }

        val customTarget = requestBuilder.intoCustomTarget(
            100,
            100,
            { _, _ -> }, {}, {}, {
                Truth.assertThat(it).isEqualTo(mockPlaceholderDrawable)
            })
        Truth.assertThat(customTarget).isEqualTo(mockCustomTarget)
    }

    @Test
    fun `Should called onResourceReady and return CustomViewTarget when load resource success with intoCustomViewTarget`() {
        every {
            requestBuilder.into(any<CustomViewTarget<ImageView, Drawable>>())
        } answers {
            firstArg<CustomViewTarget<ImageView, Drawable>>().onResourceReady(
                mockResource,
                mockTransition
            )
            mockCustomViewTarget
        }

        val customViewTarget = requestBuilder.intoCustomViewTarget(
            mockImageView,
            { resource, transition ->
                Truth.assertThat(resource).isEqualTo(mockResource)
                Truth.assertThat(transition).isEqualTo(mockTransition)
            })

        Truth.assertThat(mockCustomViewTarget).isEqualTo(customViewTarget)
    }

    @Test
    fun `Should called onLoadFailed and return CustomViewTarget when load resource failed with intoCustomViewTarget`() {
        every {
            requestBuilder.into(any<CustomViewTarget<ImageView, Drawable>>())
        } answers {
            firstArg<CustomViewTarget<ImageView, Drawable>>().onLoadFailed(mockErrorDrawable)
            mockCustomViewTarget
        }

        val customViewTarget = requestBuilder.intoCustomViewTarget(
            mockImageView, { _, _ -> }, {
                Truth.assertThat(it).isEqualTo(mockErrorDrawable)
            })

        Truth.assertThat(mockCustomViewTarget).isEqualTo(customViewTarget)
    }


    @Test
    fun `Should called onResourceLoading and return CustomViewTarget when load is started with intoCustomViewTarget`() {
        every {
            requestBuilder.into(any<CustomViewTarget<ImageView, Drawable>>())
        } answers {
            firstArg<CustomViewTarget<ImageView, Drawable>>().onLoadStarted(mockPlaceholderDrawable)
            mockCustomViewTarget
        }

        val customViewTarget = requestBuilder.intoCustomViewTarget(
            mockImageView, { _, _ -> }, {}, {
                Truth.assertThat(it).isEqualTo(mockPlaceholderDrawable)
            })

        Truth.assertThat(mockCustomViewTarget).isEqualTo(customViewTarget)
    }

    @Test
    fun `Should called onResourceCleared and return CustomViewTarget when resource is no longer valid and must be freed with intoCustomViewTarget`() {
        every {
            requestBuilder.into(any<CustomViewTarget<ImageView, Drawable>>())
        } answers {
            firstArg<CustomViewTarget<ImageView, Drawable>>().onLoadCleared(mockPlaceholderDrawable)
            mockCustomViewTarget
        }

        val customViewTarget = requestBuilder.intoCustomViewTarget(
            mockImageView, { _, _ -> }, {}, {}, {
                Truth.assertThat(it).isEqualTo(mockPlaceholderDrawable)
            })

        Truth.assertThat(mockCustomViewTarget).isEqualTo(customViewTarget)
    }

    @Test
    fun `Should return source ready state when load resource success with await`() =
        mainCoroutineRule.runBlockingTest {
            every {
                requestBuilder.into(any<CustomTarget<Drawable>>())
            } answers {
                firstArg<CustomTarget<Drawable>>().onResourceReady(
                    mockResource,
                    mockTransition
                )
                mockCustomTarget
            }

            val imageResult = requestBuilder.await(mockContext)

            Truth.assertThat(imageResult).isInstanceOf(ImageResult.SourceReady::class.java)
            if (imageResult is ImageResult.SourceReady<Drawable>) {
                Truth.assertThat(imageResult.resource).isEqualTo(mockResource)
                Truth.assertThat(imageResult.transition).isEqualTo(mockTransition)
            }
        }

    @Test
    fun `Should return load failed state when load resource failed with await`() =
        mainCoroutineRule.runBlockingTest {
            every {
                requestBuilder.into(any<CustomTarget<Drawable>>())
            } answers {
                firstArg<CustomTarget<Drawable>>().onLoadFailed(mockErrorDrawable)
                mockCustomTarget
            }

            val imageResult = requestBuilder.await(mockContext)

            Truth.assertThat(imageResult).isInstanceOf(ImageResult.LoadFailed::class.java)
            if (imageResult is ImageResult.LoadFailed) {
                Truth.assertThat(imageResult.errorDrawable).isEqualTo(mockErrorDrawable)
            }
        }

    @Test
    fun `Should return load cleared state when load is cancelled and resources are freed with await`() =
        mainCoroutineRule.runBlockingTest {
            every {
                requestBuilder.into(any<CustomTarget<Drawable>>())
            } answers {
                firstArg<CustomTarget<Drawable>>().onLoadCleared(mockPlaceholderDrawable)
                mockCustomTarget
            }

            val imageResult = requestBuilder.await(mockContext)

            Truth.assertThat(imageResult).isInstanceOf(ImageResult.LoadCleared::class.java)
            if (imageResult is ImageResult.LoadCleared) {
                Truth.assertThat(imageResult.placeholder).isEqualTo(mockPlaceholderDrawable)
            }
        }
}