package com.astro.test.fajarfebriyan.core.interactor

import com.astro.test.fajarfebriyan.core.exception.Failure
import com.astro.test.fajarfebriyan.core.functional.Either

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [NetworkCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class NetworkCase<in Params, out Type> where Type : Any {

    abstract fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) =
        onResult(run(params))

    class None
}