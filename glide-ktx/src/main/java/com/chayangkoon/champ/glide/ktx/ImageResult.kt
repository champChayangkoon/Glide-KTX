package com.chayangkoon.champ.glide.ktx

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.transition.Transition

sealed class ImageResult<out T> {
    data class LoadStarted(val placeholder: Drawable?) : ImageResult<Nothing>()
    data class SourceReady<T>(val resource: T, val transition: Transition<in T>?) : ImageResult<T>()
    data class LoadFailed(val errorDrawable: Drawable?) : ImageResult<Nothing>()
    data class LoadCleared(val placeholder: Drawable?) : ImageResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is LoadStarted -> "LoadStarted[placeholder=$placeholder]"
            is SourceReady<T> -> "SourceReady[resource=$resource,transition=$transition]"
            is LoadFailed -> "LoadFailed[errorDrawable=$errorDrawable]"
            is LoadCleared -> "LoadCleared[placeholder=$placeholder]"
        }
    }
}