package com.astro.test.fajarfebriyan.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object BadRequest : Failure()
    object NotFound : Failure()
    object Unauthorized : Failure()
    object RequestEntityTooLarge : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}