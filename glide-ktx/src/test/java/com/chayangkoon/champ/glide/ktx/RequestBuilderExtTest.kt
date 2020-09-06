package com.chayangkoon.champ.glide.ktx

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.chayangkoon.champ.glide.ktx.utils.MainCoroutineRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

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
    fun `Should return RequestListener when use makeRequestListener method`() {
        val requestListener = makeRequestListener<Drawable>(
            { resource, model, target, datasource, isFirstResource ->
                false
            }, { glideException, model, target, isFirstResource ->
                true
            }
        )

        Truth.assertThat(requestListener).isInstanceOf(RequestListener::class.java)
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