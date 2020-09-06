@file:JvmName("FutureTargetExtKt")
@file:Suppress("BlockingMethodInNonBlockingContext")

package com.chayangkoon.champ.glide.ktx

import com.bumptech.glide.request.FutureTarget
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * Waits if necessary for the computation to complete, and then
 * retrieves its result on coroutine.
 * @see FutureTarget.get()
 *
 * Example :
 * ```
 * val futureTarget = Glide.with(activity)
 *     .asBitmap()
 *     .load("https://www.example.com/image.jpg")
 *     .submit()
 *
 * coroutineScope.launch(exceptionHandler) {
 *     val bitmap = futureTarget.getOnCoroutine()
 * }
 * ```
 *
 * @param [R] The type of resource this FutureTarget will retrieve.
 * @param dispatcher A coroutine context for execute on coroutine, [Dispatchers.IO] by default.
 * @return [R] The compute result by type of resource.
 */
suspend fun <R> FutureTarget<R>.getOnCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): R = withContext(dispatcher) {
    get()
}


/**
 * Waits if necessary for at most the given time for the computation
 * to complete, and then retrieves its result, if available on coroutine.
 * See also {@link FutureTarget#get(long,TimeUnit) }
 *
 * Example :
 * ```
 * val futureTarget = Glide.with(activity)
 *     .asBitmap()
 *     .load("https://www.example.com/image.jpg")
 *     .submit()
 *
 * coroutineScope.launch(exceptionHandler) {
 *     val bitmap = futureTarget.getOnCoroutine(3000, TimeUnit.MILLISECONDS)
 * }
 * ```
 *
 * @param [R] The type of resource this FutureTarget will retrieve.
 * @param timeout the maximum time to wait.
 * @param unit the time unit of the timeout argument.
 * @param dispatcher A coroutine context for execute on coroutine, [Dispatchers.IO] by default.
 * @return [R] The compute result by type of resource.
 */
suspend fun <R> FutureTarget<R>.getOnCoroutine(
    timeout: Long,
    unit: TimeUnit,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): R = withContext(dispatcher) {
    get(timeout, unit)
}


