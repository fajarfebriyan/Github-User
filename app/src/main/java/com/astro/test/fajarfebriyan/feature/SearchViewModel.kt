package com.astro.test.fajarfebriyan.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.astro.test.fajarfebriyan.core.base.BaseViewModel
import com.astro.test.fajarfebriyan.core.interactor.LocalCase
import com.astro.test.fajarfebriyan.core.util.common.LoadingState
import com.astro.test.fajarfebriyan.core.util.common.SingleLiveEvent
import com.astro.test.fajarfebriyan.core.util.common.Status
import com.astro.test.fajarfebriyan.data.model.Favorite
import com.astro.test.fajarfebriyan.data.model.SearchRequest
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.data.network.general.Listing
import com.astro.test.fajarfebriyan.data.preferences.PreferenceHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val localCase: LocalCase,
    private val searchNetworkCaseList: SearchNetworkCaseList
): BaseViewModel() {

    private val listing = MutableLiveData<Listing<User>>()
    val list = Transformations.switchMap(listing) { it.pagedList }
    val network = Transformations.switchMap(listing) { it.networkState }

    var favList = SingleLiveEvent<List<Favorite>>()
    var favListTemp: List<Favorite> = ArrayList()
    var isFailed = SingleLiveEvent<String>()

    fun getFav() {
        viewModelScope.launch {
            withContext(io) {
                try {
                    favListTemp = localCase.getFavList()
                    favList.postValue(favListTemp)
                } catch (e: Exception) {

                }
            }
        }
    }

    fun insertFav(id: Int?) {
        val fav = Favorite(id)
        viewModelScope.launch {
            withContext(io) {
                try {
                    localCase.insertFav(fav)
                    getFav()
                } catch (e: Exception) {

                }
            }
        }
    }

    fun deleteFav(id: Int?) {
        val fav = Favorite(id)
        viewModelScope.launch {
            withContext(io) {
                try {
                    localCase.deleteFav(fav)
                    getFav()
                } catch (e: Exception) {

                }
            }
        }
    }

    fun setOrder(order: String) {
        localCase.setOrder(PreferenceHelper.Preference.ORDER_KEY, order)
    }

    fun getOrder() = localCase.getOrder(PreferenceHelper.Preference.ORDER_KEY)

    fun search(text: String) {
        if (loadingState.value?.status == Status.RUNNING) {
            return
        }
        loadingState.postValue(LoadingState.LOADING)

        val searchRequest = SearchRequest(
            word = text,
            order = localCase.getOrder(PreferenceHelper.Preference.ORDER_KEY)
        )

        viewModelScope.launch {
            withContext(io) {
                searchNetworkCaseList(searchRequest) {
                    it.either(::handleFailure) { response ->
                        listing.postValue(response)
                    }
                    loadingState.postValue(LoadingState.LOADED)
                }
            }
        }
    }

    fun retry() {
        val listing = listing.value
        listing?.retry?.invoke()
    }
}