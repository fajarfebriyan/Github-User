package com.astro.test.fajarfebriyan.core.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.astro.test.fajarfebriyan.core.coroutines.CloseableCoroutineScope
import com.astro.test.fajarfebriyan.core.coroutines.CoroutinesDispatcherProvider
import com.astro.test.fajarfebriyan.data.network.general.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.IOException
import java.util.concurrent.Executor

abstract class BasePagedKeyedDataSource<ITEM : Any> : PageKeyedDataSource<Int, ITEM>() {

    companion object {
        const val CONNECTION_FAILED = "\uD83D\uDE28 Wooops Please try again."
    }

    internal lateinit var retryExecutor: Executor
    internal lateinit var dispatchers: CoroutinesDispatcherProvider

    internal var retry: (() -> Any)? = null
    internal val networkState = MutableLiveData<NetworkState>()
    internal val scope = CloseableCoroutineScope(Job() + Dispatchers.Main)

    override fun loadBefore(
        params: LoadParams<Int>,
        switchback: LoadCallback<Int, ITEM>
    ) {
        // ignored, since we only ever append to our initial load
    }

    /**
     * load initial
     */
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        switchback: LoadInitialCallback<Int, ITEM>
    ) {
        val currentPage = 1
        val nextPage = currentPage + 1
        makeLoadInitialRequestSync(params, switchback, currentPage, nextPage)
    }

    // triggered by a refresh, we better execute sync
    abstract fun makeLoadInitialRequestSync(
        params: LoadInitialParams<Int>,
        switchback: LoadInitialCallback<Int, ITEM>,
        currentPage: Int,
        nextPage: Int
    )

    /**
     * load after
     */
    override fun loadAfter(
        params: LoadParams<Int>,
        switchback: LoadCallback<Int, ITEM>
    ) {
        val currentPage = params.key
        val nextPage = currentPage + 1

        makeLoadAfterRequest(params, switchback, currentPage, nextPage)
    }

    // triggered when load more, we better execute async
    abstract fun makeLoadAfterRequest(
        params: LoadParams<Int>,
        switchback: LoadCallback<Int, ITEM>,
        currentPage: Int,
        nextPage: Int
    )

    internal open fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let { retry ->
            retryExecutor.execute { retry() }
        }
    }

    protected open fun handleException(e: Exception, retry: () -> Unit) {
        if (e is IOException) {
            // this is an actual network failure :( inform the user and possibly retry
            // logging probably not necessary
            networkState.postValue(NetworkState.error(CONNECTION_FAILED))
        } else {
            // conversion issue! big problems :(
            networkState.postValue(NetworkState.error(e.localizedMessage))
        }
        retry()
    }
}
