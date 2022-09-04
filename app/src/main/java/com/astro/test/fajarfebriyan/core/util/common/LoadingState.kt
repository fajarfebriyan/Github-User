package com.astro.test.fajarfebriyan.core.util.common

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    EOF
}

data class LoadingState constructor(val status: Status) {
    companion object {
        val LOADED = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.RUNNING)
        val FAILED = LoadingState(Status.FAILED)
        val EOF = LoadingState(Status.EOF)
    }
}