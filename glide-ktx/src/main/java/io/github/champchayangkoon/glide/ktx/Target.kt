package io.github.champchayangkoon.glide.ktx

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

/**
 * Create a new [CustomTarget] and to load the resource in its original size with lambda.
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
inline fun <T> createCustomTarget(
    crossinline onResourceReady: (T, Transition<in T>?) -> Unit,
    crossinline onLoadFailed: (Drawable?) -> Unit = {},
    crossinline onLoadStarted: (Drawable?) -> Unit = {},
    crossinline onLoadCleared: (Drawable?) -> Unit = {}
): CustomTarget<T> {
    return object : CustomTarget<T>() {
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
    }
}

/**
 * Create a new [CustomTarget] and to load the resource with [width] and [height]
 * as the requested size with lambda.
 *
 * @param [T] The type of resource that will be delivered to the [CustomTarget].
 *
 * @param width The requested width (> 0, or == [Target.SIZE_ORIGINAL]).
 * @param height The requested height (> 0, or == [Target.SIZE_ORIGINAL]).
 * @throws IllegalArgumentException if width/height doesn't meet (> 0, or == [Target.SIZE_ORIGINAL])
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
inline fun <T> createCustomTarget(
    width: Int,
    height: Int,
    crossinline onResourceReady: (T, Transition<in T>?) -> Unit,
    crossinline onLoadFailed: (Drawable?) -> Unit = {},
    crossinline onLoadStarted: (Drawable?) -> Unit = {},
    crossinline onLoadCleared: (Drawable?) -> Unit = {}
): CustomTarget<T> {
    return object : CustomTarget<T>(width, height) {
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
    }
}

/**
 * Create a new [CustomViewTarget] and to load the resource by size of view with lambda.
 *
 * @param [T] The specific subclass of view wrapped by this target
 * (e.g. {@link android.widget.ImageView})
 * @param [Z] The resource type this [CustomViewTarget] will receive.
 *
 * @param view The view for use determine size for resource
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
inline fun <T : View, Z> createCustomViewTarget(
    view: T,
    crossinline onResourceReady: (Z, Transition<in Z>?) -> Unit,
    crossinline onLoadFailed: (Drawable?) -> Unit = {},
    crossinline onResourceLoading: (Drawable?) -> Unit = {},
    crossinline onResourceCleared: (Drawable?) -> Unit = {}
): CustomViewTarget<T, Z> {
    return object : CustomViewTarget<T, Z>(view) {
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
    }
}