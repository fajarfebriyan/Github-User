package com.astro.test.fajarfebriyan.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class SearchRequest(
    var word: String? = null,
    var order: String? = null
) : Parcelable
