package com.astro.test.fajarfebriyan.feature

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.astro.test.fajarfebriyan.core.coroutines.CoroutinesDispatcherProvider
import com.astro.test.fajarfebriyan.core.exception.Failure
import com.astro.test.fajarfebriyan.core.functional.Either
import com.astro.test.fajarfebriyan.core.util.common.NetworkHandler
import com.astro.test.fajarfebriyan.data.model.SearchRequest
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.data.network.general.Listing
import com.astro.test.fajarfebriyan.feature.paging.SearchDataSourceFactory
import java.util.concurrent.Executors
import javax.inject.Inject

interface SearchRepository {

    fun getResultSearch(request: SearchRequest): Either<Failure, Listing<User>>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val service: SearchService,
        private val dispatchers: CoroutinesDispatcherProvider
    ) : SearchRepository {
        companion object {
            private const val PAGE_SIZE: Int = 15
        }

        private val networkExecutor = Executors.newFixedThreadPool(5)
        override fun getResultSearch(
            request: SearchRequest
        ): Either<Failure, Listing<User>> {

            val factory = SearchDataSourceFactory(
                networkExecutor,
                dispatchers,
                service,
                request
            )

            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(PAGE_SIZE * 2)
                .setPageSize(PAGE_SIZE)
                .build()

            val livePagedList = LivePagedListBuilder(factory, config)
                .setFetchExecutor(networkExecutor)
                .build()

            return when (networkHandler.isConnected) {
                false, null -> Either.Left(Failure.NetworkConnection)
                true -> Either.Right(
                    Listing(
                        pagedList = livePagedList,
                        networkState = Transformations.switchMap(factory.source) { it.networkState },
                        retry = { factory.source.value?.retryAllFailed() },
                        refresh = { factory.source.value?.invalidate() })
                )
            }
        }
    }
}