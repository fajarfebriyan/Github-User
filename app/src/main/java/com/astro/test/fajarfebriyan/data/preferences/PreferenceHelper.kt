package com.astro.test.fajarfebriyan.data.preferences

import com.orhanobut.hawk.Hawk

interface PreferenceHelper {

    fun setOrder(key: String, value: String)

    fun getOrder(key: String): String

    class Preference : PreferenceHelper {

        companion object {
            const val DESC = "desc"
            const val ASC = "asc"
            const val ORDER_KEY = "ORDER_KEY"
        }

        override fun setOrder(key: String, value: String) {
            Hawk.put(key, value)
        }

        override fun getOrder(key: String): String {
            return Hawk.get(key, ASC)
        }
    }
}