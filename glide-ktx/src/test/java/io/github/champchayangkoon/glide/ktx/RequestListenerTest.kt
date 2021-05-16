package io.github.champchayangkoon.glide.ktx

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.RequestListener
import com.google.common.truth.Truth
import org.junit.Test

class RequestListenerTest {
    @Test
    fun `Should return RequestListener when use createRequestListener method`() {
        val requestListener = createRequestListener<Drawable>(
            { resource, model, target, datasource, isFirstResource ->
                false
            }, { glideException, model, target, isFirstResource ->
                true
            }
        )

        Truth.assertThat(requestListener).isInstanceOf(RequestListener::class.java)
    }
}