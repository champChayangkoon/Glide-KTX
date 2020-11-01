@file:JvmName("RequestBuilderExtKt")

package com.chayangkoon.champ.glide.ktx

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Create and sets a new [RequestListener] to monitor the resource load with lambda.
 * Should Use when request only one in class if greater than one should use
 * [createRequestListener] for make [RequestListener] instead
 * and set to all request.
 *
 * [Glide] recommend best way is create a single instance of an exception handler
 * per type of request (usually activity/fragment) rather than
 * pass one in per request to avoid some redundant object allocation.
 *
 * <p>Subsequent calls to this method will replace previously set listeners. To set multiple
 * listeners, use [addListener] instead.
 *
 * @param [T] The type of resource being loaded.
 *
 * @param onResourceReady The onResourceReady will be invoked when a load completes successfully,
 * immediately before {@link Target#onResourceReady([Object], [Transition])}.
 *
 * @param onLoadFailed The onLoadFailed will be invoked when an exception occurs during a load,
 * immediately before {@link Target#onLoadFailed([Drawable])}.
 *
 * @return This [RequestBuilder].
 */
inline fun <T> RequestBuilder<T>.listener(
    crossinline onResourceReady: (T, Any?, Target<T>?, DataSource?, Boolean) -> Boolean,
    crossinline onLoadFailed: (GlideException?, Any?, Target<T>?, Boolean) -> Boolean
): RequestBuilder<T> {
    return listener(object : RequestListener<T> {
        override fun onResourceReady(
            resource: T,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return onResourceReady.invoke(resource, model, target, dataSource, isFirstResource)
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            return onLoadFailed.invoke(e, model, target, isFirstResource)
        }
    })
}

/**
 * Create and adds a new [RequestListener] with lambda. If called multiple times,
 * all passed [RequestListener] will be called in order.
 *
 * @param [T] The type of resource being loaded.
 *
 * @param onResourceReady The onResourceReady will be invoked when a load completes successfully,
 * immediately before {@link Target#onResourceReady([Object], [Transition])}.
 *
 * @param onLoadFailed The onLoadFailed will be invoked when an exception occurs during a load,
 * immediately before {@link Target#onLoadFailed([Drawable])}.
 *
 * @return This [RequestBuilder].
 */
inline fun <T> RequestBuilder<T>.addListener(
    crossinline onResourceReady: (T, Any?, Target<T>?, DataSource?, Boolean) -> Boolean,
    crossinline onLoadFailed: (GlideException?, Any?, Target<T>?, Boolean) -> Boolean
): RequestBuilder<T> {
    return addListener(object : RequestListener<T> {
        override fun onResourceReady(
            resource: T,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return onResourceReady.invoke(resource, model, target, dataSource, isFirstResource)
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            return onLoadFailed.invoke(e, model, target, isFirstResource)
        }
    })
}

/**
 * Create a new [CustomTarget] and to load the resource in its original size
 * into the CustomTarget with lambda.
 *
 * Example :
 * ```
 * Glide.with(activity)
 * .load("https://www.example.com/image.jpg")
 * .intoCustomTarget({ resource, transition ->
 *     // handle resource
 * }, { drawable ->
 *     // handle error drawable
 * })
 * ```
 *
 * @param [T] The type of resource that will be delivered to the [CustomTarget].
 *
 * @param onResourceReady The onResourceReady will be invoked when the resource load has finished.
 * See also {@link Target#onResourceReady([Object], [Transition])}
 *
 * @param onLoadFailed The onLoadFailed will be invoked when a load fails.
 * See also {@link Target#onLoadFailed([Drawable])}
 *
 * @param onLoadStarted The onLoadStarted will be invoked when a load is started.
 * See also {@link Target#onLoadStarted([Drawable])}
 *
 * @param onLoadCleared The onLoadCleared will be invoked when a load is cancelled
 * and its resources are freed. See also {@link Target#onLoadCleared([Drawable])}
 *
 * @return The [CustomTarget]
 */
inline fun <T> RequestBuilder<T>.intoCustomTarget(
    crossinline onResourceReady: (T, Transition<in T>?) -> Unit,
    crossinline onLoadFailed: (Drawable?) -> Unit = {},
    crossinline onLoadStarted: (Drawable?) -> Unit = {},
    crossinline onLoadCleared: (Drawable?) -> Unit = {}
): CustomTarget<T> {
    return into(object : CustomTarget<T>() {
        override fun onLoadStarted(placeholder: Drawable?) {
            onLoadStarted.invoke(placeholder)
        }

        override fun onResourceReady(resource: T, transition: Transition<in T>?) {
            onResourceReady.invoke(resource, transition)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            onLoadFailed.invoke(errorDrawable)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            onLoadCleared.invoke(placeholder)
        }
    })
}

/**
 * Create a new [CustomTarget] and to load the resource with [width] and [height]
 * as the requested size into the CustomTarget with lambda.
 *
 * Example :
 * ```
 * Glide.with(activity)
 * .load("https://www.example.com/image.jpg")
 * .intoCustomTarget(100, 100, { resource, transition ->
 *     // handle resource
 * }, { drawable ->
 *     // handle error drawable
 * })
 * ```
 *
 * @param [T] The type of resource that will be delivered to the [CustomTarget].
 * @param width The requested width (> 0, or == [Target.SIZE_ORIGINAL]).
 * @param height The requested height (> 0, or == [Target.SIZE_ORIGINAL]).
 *
 * @param onResourceReady The onResourceReady will be invoked when the resource load has finished.
 * See also {@link Target#onResourceReady([Object], [Transition])}
 *
 * @param onLoadFailed The onLoadFailed will be invoked when a load fails.
 * See also {@link Target#onLoadFailed([Drawable])}
 *
 * @param onLoadStarted The onLoadStarted will be invoked when a load is started.
 * See also {@link Target#onLoadStarted([Drawable])}
 *
 * @param onLoadCleared The onLoadCleared will be invoked when a load is cancelled
 * and its resources are freed. See also {@link Target#onLoadCleared([Drawable])}
 *
 * @return The [CustomTarget]
 */
inline fun <T> RequestBuilder<T>.intoCustomTarget(
    width: Int,
    height: Int,
    crossinline onResourceReady: (T, Transition<in T>?) -> Unit,
    crossinline onLoadFailed: (Drawable?) -> Unit = {},
    crossinline onLoadStarted: (Drawable?) -> Unit = {},
    crossinline onLoadCleared: (Drawable?) -> Unit = {}
): CustomTarget<T> {
    return into(object : CustomTarget<T>(width, height) {
        override fun onLoadStarted(placeholder: Drawable?) {
            onLoadStarted.invoke(placeholder)
        }

        override fun onResourceReady(resource: T, transition: Transition<in T>?) {
            onResourceReady.invoke(resource, transition)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            onLoadFailed.invoke(errorDrawable)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            onLoadCleared.invoke(placeholder)
        }
    })
}

/**
 * Create a new [CustomViewTarget] and to load the resource in its original size
 * into the CustomViewTarget with lambda.
 *
 * Example :
 * ```
 * Glide.with(imageView)
 * .load("https://www.example.com/image.jpg")
 * .intoCustomViewTarget(imageView, { resource, transition ->
 *     // handle resource
 * }, {
 *    // handle error drawable
 * })
 * ```
 *
 * @param [T] The specific subclass of view wrapped by this target
 * (e.g. {@link android.widget.ImageView})
 * @param [Z] The resource type this [CustomViewTarget] will receive.
 *
 * @param onResourceReady The onResourceReady will be invoked when the resource load has finished.
 * See also {@link Target#onResourceReady([Object], [Transition])}
 *
 * @param onLoadFailed The onLoadFailed will be invoked when a load fails.
 * See also {@link Target#onLoadFailed([Drawable])}
 *
 * @param onResourceLoading The onResourceLoading will be invoked when a resource load is started.
 * See also {@link CustomViewTarget#onLoadStarted([Drawable])}
 *
 * @param onResourceCleared The onResourceCleared will be invoked when the resource is no longer
 * valid and must be freed See also {@link CustomViewTarget#onResourceCleared([Drawable])}
 *
 * @return The [CustomViewTarget]
 */
inline fun <T : View, Z> RequestBuilder<Z>.intoCustomViewTarget(
    view: T,
    crossinline onResourceReady: (Z, Transition<in Z>?) -> Unit,
    crossinline onLoadFailed: (Drawable?) -> Unit = {},
    crossinline onResourceLoading: (Drawable?) -> Unit = {},
    crossinline onResourceCleared: (Drawable?) -> Unit = {}
): CustomViewTarget<T, Z> {
    return into(object : CustomViewTarget<T, Z>(view) {
        override fun onResourceLoading(placeholder: Drawable?) {
            onResourceLoading.invoke(placeholder)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            onLoadFailed.invoke(errorDrawable)
        }

        override fun onResourceReady(resource: Z, transition: Transition<in Z>?) {
            onResourceReady.invoke(resource, transition)
        }

        override fun onResourceCleared(placeholder: Drawable?) {
            onResourceCleared.invoke(placeholder)
        }
    })
}

/**
 * Awaits resource is ready without blocking a thread.
 * This suspending function is cancellable. @see [suspendCancellableCoroutine]
 *
 * Example :
 * ```
 * coroutineScope.launch {
 *     val imageResult = Glide.with(activity)
 *         .load("https://www.example.com/image.jpg")
 *         .await(context)
 *     // handle result of image
 * }
 * ```
 *
 * @param context A context use in [Glide.with] to clear [CustomTarget].
 * @param width The requested width (> 0, or == [Target.SIZE_ORIGINAL])
 * and original width by default.
 * @param height The requested height (> 0, or == [Target.SIZE_ORIGINAL])
 * and original height by default.
 * @return [ImageResult] The ImageResult is result from load resource into [CustomTarget]
 */
suspend fun <T> RequestBuilder<T>.await(
    context: Context,
    width: Int = Target.SIZE_ORIGINAL,
    height: Int = Target.SIZE_ORIGINAL
) = suspendCancellableCoroutine<ImageResult<T>> {
    val customTarget: CustomTarget<T> = into(object : CustomTarget<T>(width, height) {
        override fun onResourceReady(resource: T, transition: Transition<in T>?) {
            it.resume(ImageResult.SourceReady(resource, transition))
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            it.resume(ImageResult.LoadFailed(errorDrawable))
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            it.resume(ImageResult.LoadCleared(placeholder))
        }
    })

    it.invokeOnCancellation {
        Glide.with(context).clear(customTarget)
    }
}

/**
 * Creates a flow that produces values from the result from load resource with [CustomTarget].
 * @see [callbackFlow]
 *
 * Example :
 * ```
 * val imageResultFlow = Glide.with(activity)
 *     .load("https://www.example.com/image.jpg")
 *     .intoFlow(context)
 *
 * coroutineScope.launch {
 *     imageResultFlow.collect {
 *         // handle result of image
 *     }
 * }
 * ```
 *
 * @param context A context use in [Glide.with] to clear [CustomTarget].
 * @param width The requested width (> 0, or == [Target.SIZE_ORIGINAL])
 * and original width by default.
 * @param height The requested height (> 0, or == [Target.SIZE_ORIGINAL])
 * and original height by default.
 * @return Flow<ImageResult> The [ImageResult] is result from load resource into [CustomTarget]
 */
@ExperimentalCoroutinesApi
fun <T> RequestBuilder<T>.intoFlow(
    context: Context,
    width: Int = Target.SIZE_ORIGINAL,
    height: Int = Target.SIZE_ORIGINAL
): Flow<ImageResult<T>> = callbackFlow {
    val customTarget: CustomTarget<T> = into(object : CustomTarget<T>(width, height) {
        override fun onLoadStarted(placeholder: Drawable?) {
            offer(ImageResult.LoadStarted(placeholder))
        }

        override fun onResourceReady(resource: T, transition: Transition<in T>?) {
            offer(ImageResult.SourceReady(resource, transition))
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            offer(ImageResult.LoadFailed(errorDrawable))
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            offer(ImageResult.LoadCleared(placeholder))
        }
    })

    awaitClose {
        Glide.with(context).clear(customTarget)
    }
}