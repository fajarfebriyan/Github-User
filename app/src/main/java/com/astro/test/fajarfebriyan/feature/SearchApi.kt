package com.astro.test.fajarfebriyan.feature

import com.astro.test.fajarfebriyan.data.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchApi {
    @GET("/search/users")
    fun getSearch(
        @Query("q") word: String?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?,
        @Query("sort") sort: String?,
        @Query("order") order: String?
    ): Call<SearchResponse>
}