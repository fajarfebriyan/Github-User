package com.astro.test.fajarfebriyan.data.model

data class SearchResponse(
    var total_count: Int? = 0,
    var incomplete_results: Boolean? = false,
    var items: List<User> = ArrayList(),
    var success: Int? = 0,
    var message: String? = null
)