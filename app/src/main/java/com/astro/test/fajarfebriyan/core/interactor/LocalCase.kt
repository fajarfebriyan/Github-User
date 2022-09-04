package com.astro.test.fajarfebriyan.core.interactor

import com.astro.test.fajarfebriyan.data.database.DatabaseHelper
import com.astro.test.fajarfebriyan.data.model.Favorite
import com.astro.test.fajarfebriyan.data.preferences.PreferenceHelper
import javax.inject.Inject

class LocalCase @Inject constructor(
    private val preference: PreferenceHelper.Preference,
    private val databaseHelperImpl: DatabaseHelper.DatabaseHelperImpl) : PreferenceHelper,
    DatabaseHelper {
    override suspend fun getFavList(): List<Favorite> {
        return databaseHelperImpl.getFavList()
    }

    override suspend fun insertFav(fav: Favorite) {
        databaseHelperImpl.insertFav(fav)
    }

    override suspend fun deleteFav(fav: Favorite) {
        databaseHelperImpl.deleteFav(fav)
    }

    override fun setOrder(key: String, value: String) {
        preference.setOrder(key, value)
    }

    override fun getOrder(key: String): String {
        return preference.getOrder(key)
    }
}