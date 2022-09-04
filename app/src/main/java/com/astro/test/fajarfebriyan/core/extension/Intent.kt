package com.astro.test.fajarfebriyan.core.extension

import android.content.Intent
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.parceled(key: String): T? {
    val extras = this.extras
    var data: T? = null
    extras?.let {
        if (it.containsKey(key)) {
            data = it.getParcelable(key)
        }
    }
    return data
}

inline fun Intent.getInt(key: String): Int {
    val extras = this.extras
    var data = 0
    extras?.let {
        if (it.containsKey(key)) {
            data = it.getInt(key, 0)
        }
    }
    return data
}
