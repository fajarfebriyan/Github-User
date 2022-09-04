package com.astro.test.fajarfebriyan.feature

import com.astro.test.fajarfebriyan.core.interactor.NetworkCase
import com.astro.test.fajarfebriyan.data.model.SearchRequest
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.data.network.general.Listing
import javax.inject.Inject

class SearchNetworkCaseList @Inject constructor(
    private val repository: SearchRepository) : NetworkCase<SearchRequest, Listing<User>>(){

    override fun run(params: SearchRequest) = repository.getResultSearch(params)
}