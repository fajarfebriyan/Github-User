package com.astro.test.fajarfebriyan.feature.paging

import com.astro.test.fajarfebriyan.core.coroutines.CoroutinesDispatcherProvider
import com.astro.test.fajarfebriyan.core.extension.await
import com.astro.test.fajarfebriyan.core.util.common.Statics
import com.astro.test.fajarfebriyan.core.util.paging.BasePagedKeyedDataSource
import com.astro.test.fajarfebriyan.data.model.SearchRequest
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.data.network.general.NetworkState
import com.astro.test.fajarfebriyan.feature.SearchService
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor

class SearchPagedKeyedDataSource(
    retryExecutor: Executor,
    dispatchers: CoroutinesDispatcherProvider,
    private val service: SearchService,
    private val request: SearchRequest
) : BasePagedKeyedDataSource<User>() {

    init {
        super.retryExecutor = retryExecutor
        super.dispatchers = dispatchers
    }

    override fun makeLoadInitialRequestSync(
        params: LoadInitialParams<Int>,
        switchback: LoadInitialCallback<Int, User>,
        currentPage: Int,
        nextPage: Int
    ) {
        networkState.postValue(NetworkState.LOADING)

        try {
            val request = service.getSearch(request.word, currentPage, 15, Statics.Extras.JOINED, request.order)
            val response = request.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.items != null) {
                    //val items = body.data ?: emptyList()
                    val items = arrayListOf<User>()
                    items.addAll(body.items)
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    switchback.onResult(items, null, nextPage)
                } else {
                    retry = { loadInitial(params, switchback) }
                    networkState.postValue(NetworkState.error(response.message()))
                }
            } else {
                retry = { loadInitial(params, switchback) }
                networkState.postValue(NetworkState.error(response.message()))
                Timber.d("requestSync not successful %s", response.message())
            }
        } catch (e: Exception) {
            handleException(e) { retry = { loadInitial(params, switchback) } }
        }
    }

    override fun makeLoadAfterRequest(
        params: LoadParams<Int>,
        switchback: LoadCallback<Int, User>,
        currentPage: Int,
        nextPage: Int
    ) {
        networkState.postValue(NetworkState.LOADING)

        scope.launch {
            withContext(dispatchers.io) {
                try {
                    val request = service.getSearch(request.word, currentPage, 15, Statics.Extras.JOINED, request.order)
                    val response = request.await()

                    if (response.items != null) {
                        val items = response.items
                        retry = null
                        networkState.postValue(NetworkState.LOADED)
                        switchback.onResult(items, nextPage)
                    } else {
                        retry = { loadAfter(params, switchback) }
                        networkState.postValue(NetworkState.error(response.message))
                    }
                } catch (e: Exception) {
                    handleException(e) { retry = { loadAfter(params, switchback) } }
                }
            }
        }
    }
}
