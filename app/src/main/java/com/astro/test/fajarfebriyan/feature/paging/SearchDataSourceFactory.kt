package com.astro.test.fajarfebriyan.feature.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.astro.test.fajarfebriyan.core.coroutines.CoroutinesDispatcherProvider
import com.astro.test.fajarfebriyan.data.model.SearchRequest
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.feature.SearchService
import java.util.concurrent.Executor

class SearchDataSourceFactory(
    private val retryExecutor: Executor,
    private val dispatchers: CoroutinesDispatcherProvider,
    private val service: SearchService,
    private val request: SearchRequest
) : DataSource.Factory<Int, User>() {

    internal val source = MutableLiveData<SearchPagedKeyedDataSource>()

    override fun create(): DataSource<Int, User> {
        val source = SearchPagedKeyedDataSource(
            retryExecutor,
            dispatchers,
            service,
            request
        )
        this.source.postValue(source)
        return source
    }
}
