package io.github.champchayangkoon.glide.ktx

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

/**
 * create a new [RequestListener] with lambda for use with [Glide].
 * [Glide] recommend best way is create a single instance of an exception handler
 * per type of request (usually activity/fragment) rather than
 * pass one in per request to avoid some redundant object allocation.
 *
 * @param [R] The type of resource being loaded.
 *
 * @param onResourceReady The onResourceReady will be invoked when a load completes successfully,
 * immediately before {@link Target#onResourceReady([Object], [Transition])}.
 * return true if prevent {@link Target#onLoadFailed(Drawable)} from being called on the Target
 * or false to allow {@link Target#onLoadFailed(Drawable)} to be called on Target.
 *
 * @param onLoadFailed The onLoadFailed will be invoked when an exception occurs during a load,
 * immediately before {@link Target#onLoadFailed([Drawable])}.
 * return true if prevent {@link Target#onResourceReady([Object], [Transition])}from being called on the Target
 * or false to allow {@link Target#onResourceReady([Object], [Transition])} to be called on Target.
 *
 * @return The [RequestListener].
 */
inline fun <R> createRequestListener(
    crossinline onResourceReady: (R, Any?, Target<R>?, DataSource?, Boolean) -> Boolean,
    crossinline onLoadFailed: (GlideException?, Any?, Target<R>?, Boolean) -> Boolean
): RequestListener<R> {
    return object : RequestListener<R> {
        override fun onResourceReady(
            resource: R,
            model: Any?,
            target: Target<R>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return onResourceReady.invoke(resource, model, target, dataSource, isFirstResource)
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<R>?,
            isFirstResource: Boolean
        ): Boolean {
            return onLoadFailed.invoke(e, model, target, isFirstResource)
        }
    }
}