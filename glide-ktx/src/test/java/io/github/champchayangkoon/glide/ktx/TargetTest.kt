package io.github.champchayangkoon.glide.ktx

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.google.common.truth.Truth
import io.mockk.mockk
import org.junit.Test

class TargetTest {

    @Test
    fun `Should return CustomTarget when use createCustomTarget`() {
        val customTarget = createCustomTarget<Drawable>({ resource, transition -> })
        Truth.assertThat(customTarget).isInstanceOf(CustomTarget::class.java)
    }

    @Test
    fun `Should return CustomTarget with request size when use createCustomTarget with set request size`() {
        val customTarget = createCustomTarget<Drawable>(100, 100, { resource, transition -> })
        Truth.assertThat(customTarget).isInstanceOf(CustomTarget::class.java)
    }

    @Test
    fun `Should return CustomViewTarget when use createCustomViewTarget`() {
        val imageView = mockk<ImageView>(relaxed = true)
        val customTarget =
            createCustomViewTarget<ImageView, Drawable>(imageView, { resource, transition -> })
        Truth.assertThat(customTarget).isInstanceOf(CustomViewTarget::class.java)
    }
}