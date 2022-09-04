package com.astro.test.fajarfebriyan.feature

import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.astro.test.fajarfebriyan.R
import com.astro.test.fajarfebriyan.core.base.BaseActivityHasInject
import com.astro.test.fajarfebriyan.core.extension.*
import com.astro.test.fajarfebriyan.core.interactor.NetworkCase
import com.astro.test.fajarfebriyan.data.model.Favorite
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.data.network.general.NetworkState
import com.astro.test.fajarfebriyan.databinding.ActivitySearchBinding

class SearchActivity : BaseActivityHasInject<ActivitySearchBinding, SearchViewModel>(),
    SearchAdapter.OnContentItemClickListener<User> {

    private lateinit var searchAdapter: SearchAdapter
    private var isAdapterAvail = false

    override val layoutId = R.layout.activity_search

    override fun setupViewUI() {

        binding.appbarProfile.tvTitle.text = getString(R.string.app_name)
        binding.appbarProfile.tvSubTitle.text = getString(R.string.app_name)
        model.getFav()

        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (validateSearch()) {
                    model.search(binding.etSearch.text.toString())
                    hideKeyboard()
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.etSearch.doAfterTextChanged{
            Handler().postDelayed({
                if (validateSearch()) model.search(binding.etSearch.text.toString())
                else showList(isShowList = false, isShowNoData = false)
            }, 300)
        }

        if (model.getOrder() == "desc") binding.rbDesc.isChecked = true
        else binding.rbAsc.isChecked = true

        binding.rbDesc.onClick {

            model.setOrder("desc")
            model.search(binding.etSearch.text.toString())
        }

        binding.rbAsc.onClick {
            model.setOrder("asc")
            model.search(binding.etSearch.text.toString())
        }
    }

    override fun setupViewModel() {
        model = viewModel(viewModelFactory) {
            observe(loadingState,:: handleLoading)
            observe(list,:: handleList)
            observe(favList,:: handleFavList)
            observe(network,:: handleNetworkCase)
            //observe(isFailed,:: handleFailed)
        }
    }

    override fun onItemViewClicked(v: View, item: User, isLike: Boolean) {
        if (isLike)
            model.insertFav(item.id)
        else
            model.deleteFav(item.id)
    }

    fun setUpAdapter(favList: List<Favorite>) {

        searchAdapter = SearchAdapter(this, this, favList) {
            model.retry()
        }

        binding.rvListUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
        isAdapterAvail = true
    }

    private fun handleFavList(list: List<Favorite>) {
        if (isAdapterAvail)
            searchAdapter.updateFavList(list)
        else
            setUpAdapter(list)
    }

    private fun handleList(list: PagedList<User>?) {
        list?.let {
            if (it.size > 0) {
                searchAdapter.submitList(it)
                showList(isShowList = true, isShowNoData = false)
            } else {
                showList(isShowList = false, isShowNoData = true)
            }
        }
    }


    private fun showList(isShowList: Boolean, isShowNoData: Boolean) {
        if (isShowList) {
            binding.rvListUser.visibility = View.VISIBLE
            binding.rgOrder.visibility = View.VISIBLE
            binding.tvNodata.visibility = View.GONE
        } else {
            binding.rvListUser.visibility = View.GONE
            binding.rgOrder.visibility = View.GONE
            if (isShowNoData) binding.tvNodata.visibility = View.VISIBLE
            else binding.tvNodata.visibility = View.GONE
        }
    }

    private fun validateSearch(): Boolean {
        return !binding.etSearch.text.toString().isNullOrEmpty()
    }

}