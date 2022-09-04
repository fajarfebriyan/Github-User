package com.astro.test.fajarfebriyan.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class User(
    var login: String? = null,
    var id: Int? = 0,
    var avatar_url: String? = null,
    var isLiked: Boolean = false
): Parcelable
