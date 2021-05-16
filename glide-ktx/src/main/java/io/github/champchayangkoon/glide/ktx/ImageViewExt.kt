@file:JvmName("ImageViewExtKt")
@file:Suppress("unused", "DEPRECATION")

package io.github.champchayangkoon.glide.ktx

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ViewTarget
import java.io.File

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    bitmap: Bitmap?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(bitmap, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    byteArray: ByteArray?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(byteArray, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    drawable: Drawable?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(drawable, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    @RawRes @DrawableRes resourceId: Int?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(resourceId, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    uri: Uri?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(uri, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    string: String?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(string, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                       Any?,
 *                       TransitionOptions<*, Drawable>? = null,
 *                       RequestOptions? = null
 *                   ): ViewTarget<ImageView, Drawable> loadAnyWithOutRequestOptions}
 */
@JvmSynthetic
fun ImageView.load(
    file: File?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return loadAny(file, transitionOptions, requestOptions)
}

/**
 * Load the image from [data] and set it on this [ImageView].
 *
 * Example:
 * ```
 * imageView.load("https://www.example.com/image.jpg")
 * ```
 *
 * @param data The data to load.
 * @param transitionOptions A transition from the placeholder or thumbnail when this load completes.
 * @return The [ViewTarget] used to wrap the given [ImageView].
 */
@JvmSynthetic
fun ImageView.loadAny(
    data: Any?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions? = null
): ViewTarget<ImageView, Drawable> {
    return Glide.with(this)
        .load(data)
        .apply {
            transitionOptions?.let { transition(it) }
            requestOptions?.let { apply(it) }
        }
        .into(this)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    bitmap: Bitmap?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(bitmap, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    byteArray: ByteArray?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(byteArray, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    drawable: Drawable?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(drawable, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    @RawRes @DrawableRes resourceId: Int?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(resourceId, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    uri: Uri?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(uri, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    string: String?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(string, transitionOptions, requestOptions)
}

/** See also {@link #ImageView.loadAny(
 *                      Any?,
 *                      TransitionOptions<*, Drawable>? = null,
 *                      requestOptions: RequestOptions.() -> Unit
 *                  ): ViewTarget<ImageView, Drawable> loadAny}
 */
@JvmSynthetic
inline fun ImageView.load(
    file: File?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return loadAny(file, transitionOptions, requestOptions)
}

/**
 * Load the image from [data] and set it on this [ImageView].
 *
 * Example:
 * ```
 * imageView.load("https://www.example.com/image.jpg") {
 *     transform(CircleCrop())
 * }
 * ```
 *
 * @param data The data to load.
 * @param transitionOptions A transition from the placeholder or thumbnail when this load completes.
 * @param requestOptions An optional lambda to configure the request before it is load.
 * @return The [ViewTarget] used to wrap the given [ImageView].
 */
@JvmSynthetic
inline fun ImageView.loadAny(
    data: Any?,
    transitionOptions: TransitionOptions<*, Drawable>? = null,
    requestOptions: RequestOptions.() -> Unit
): ViewTarget<ImageView, Drawable> {
    return Glide.with(this)
        .load(data)
        .apply { transitionOptions?.let { transition(it) } }
        .apply(RequestOptions().apply(requestOptions))
        .into(this)
}

/**
 * Cancel any pending loads Glide may have for the view and free any resources that may have been
 * associated with this [ImageView].
 * See also {@link RequestManager#clear(View)}
 */
@JvmSynthetic
fun ImageView.clear() {
    Glide.with(this).clear(this)
}