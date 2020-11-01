package com.chayangkoon.champ.glide.ktx

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class ImageViewExtTest {
    @RelaxedMockK
    private lateinit var imageView: ImageView

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
    fun `Should return ViewTarget when use loadAny extension of ImageView`() {
        val url: Any = "www.test.com/image.jpg"
        val mockViewTarget = mockk<ViewTarget<ImageView, Drawable>>()
        every {
            Glide.with(imageView)
                .load(url)
                .into(imageView)
        } returns mockViewTarget

        val actualViewTarget = imageView.loadAny(url)
        Truth.assertThat(actualViewTarget).isEqualTo(mockViewTarget)
    }

    @Test
    fun `Should return ViewTarget when use loadAny extension of ImageView with transition`() {
        val url: Any = "www.test.com/image.jpg"
        val mockViewTarget = mockk<ViewTarget<ImageView, Drawable>>()
        val mockWithCrossFade = mockk<DrawableTransitionOptions>()
        every {
            Glide.with(imageView)
                .load(url)
                .apply { transition(mockWithCrossFade) }
                .into(imageView)
        } returns mockViewTarget

        val actualViewTarget = imageView.loadAny(url, mockWithCrossFade)
        Truth.assertThat(actualViewTarget).isEqualTo(mockViewTarget)
    }

    @Test
    fun `Should return ViewTarget when use loadAny extension of ImageView with set request options without lambda`() {
        val url: Any = "www.test.com/image.jpg"
        val circleCrop = mockk<CircleCrop>()
        val mockViewTarget = mockk<ViewTarget<ImageView, Drawable>>()
        every {
            Glide.with(imageView)
                .load(url)
                .apply {
                    apply(RequestOptions().apply {
                        transform(circleCrop)
                    })
                }
                .into(imageView)
        } returns mockViewTarget

        val requestOptions = RequestOptions()
            .transform(circleCrop)

        val actualViewTarget = imageView.loadAny(url,requestOptions = requestOptions)
        Truth.assertThat(actualViewTarget).isEqualTo(mockViewTarget)
    }

    @Test
    fun `Should return ViewTarget when use loadAny extension of ImageView with set request options`() {
        val url: Any = "www.test.com/image.jpg"
        val circleCrop = mockk<CircleCrop>()
        val mockViewTarget = mockk<ViewTarget<ImageView, Drawable>>()
        every {
            Glide.with(imageView)
                .load(url)
                .apply(RequestOptions().apply {
                    transform(circleCrop)
                })
                .into(imageView)
        } returns mockViewTarget

        val actualViewTarget = imageView.loadAny(url) {
            transform(circleCrop)
        }
        Truth.assertThat(actualViewTarget).isEqualTo(mockViewTarget)
    }

    @Test
    fun `Should return ViewTarget when use loadAny extension of ImageView with set transition and request options`() {
        val url: Any = "www.test.com/image.jpg"
        val circleCrop = mockk<CircleCrop>()
        val mockWithCrossFade = mockk<DrawableTransitionOptions>()
        val mockViewTarget = mockk<ViewTarget<ImageView, Drawable>>()

        every {
            Glide.with(imageView)
                .load(url)
                .apply { transition(mockWithCrossFade) }
                .apply(RequestOptions().apply {
                    transform(circleCrop)
                })
                .into(imageView)
        } returns mockViewTarget

        val actualViewTarget = imageView.loadAny(url, mockWithCrossFade) {
            transform(circleCrop)
        }
        Truth.assertThat(actualViewTarget).isEqualTo(mockViewTarget)
    }
}