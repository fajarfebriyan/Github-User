package com.astro.test.fajarfebriyan.core.functional

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.astro.test.fajarfebriyan.core.exception.Failure
import com.astro.test.fajarfebriyan.core.extension.await
import com.astro.test.fajarfebriyan.core.functional.Either.Left
import com.astro.test.fajarfebriyan.core.functional.Either.Right
import com.astro.test.fajarfebriyan.data.model.ErrorResponse
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 * FP Convention dictates that [Left] is used for "failure"
 * and [Right] is used for "success".
 *
 * @see Left
 * @see Right
 */
sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Left -> Left(a)
        is Right -> fn(b)
    }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))

fun <T, R> requestSync(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    return try {
        val response = call.execute()
        Timber.d("requestSync response %s", response.body())
        when (response.isSuccessful) {
            true -> Right(transform(response.body() ?: default))
            false -> {
                Timber.d("requestSync not successful %s", response.message())
                when {
                    response.code() == 400 -> Left(Failure.BadRequest)
                    response.code() == 401 -> Left(Failure.Unauthorized)
                    response.code() == 404 -> Left(Failure.NotFound)
                    response.code() == 413 -> Left(Failure.RequestEntityTooLarge)
                    else -> Left(Failure.ServerError)
                }
            }
        }
    } catch (exception: Throwable) {
        Timber.d("requestSync catch %s", exception.message)
        Left(Failure.ServerError)
    }
}

private suspend fun <T, R> requestAsync(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    return try {
        val response = call.await()
        Right(transform(response ?: default))
    } catch (exception: Throwable) {
        Left(Failure.ServerError)
    }
}