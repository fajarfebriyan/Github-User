package com.astro.test.fajarfebriyan.data.network.general

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * Listing class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class Listing<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request initialRequest status to show to the user
    val networkState: LiveData<NetworkState>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries any failed requests.
    val retry: () -> Unit
)
