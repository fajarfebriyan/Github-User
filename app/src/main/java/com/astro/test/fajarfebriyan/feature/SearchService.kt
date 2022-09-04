package com.astro.test.fajarfebriyan.feature

import com.astro.test.fajarfebriyan.data.model.SearchResponse
import com.astro.test.fajarfebriyan.data.network.client.RestClient
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchService @Inject constructor(private val restClient: RestClient) : SearchApi {
    private val api: SearchApi
        get() = restClient.getDefaultRetrofit().create(SearchApi::class.java)

    override fun getSearch(word: String?, page: Int?, per_page: Int?, sort: String?, order: String?) = api.getSearch(word, page, per_page, sort, order)
}