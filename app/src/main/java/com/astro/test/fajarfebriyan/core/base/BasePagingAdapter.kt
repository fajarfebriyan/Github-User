package com.astro.test.fajarfebriyan.core.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.fajarfebriyan.R
import com.astro.test.fajarfebriyan.core.util.pagging.LoadMoreViewHolder
import com.astro.test.fajarfebriyan.data.network.general.NetworkState

abstract class BasePagingAdapter <ITEM : Any, VH : RecyclerView.ViewHolder, VDB : ViewDataBinding>(
    comparator: DiffUtil.ItemCallback<ITEM>,
    private val context: Context,
    private val retrySwitchback: () -> Unit
): PagedListAdapter<ITEM, RecyclerView.ViewHolder>(comparator) {

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract fun getViewHolder(binding: VDB, viewType: Int): VH
    protected abstract fun onBindViewHolder(item: ITEM, holder: RecyclerView.ViewHolder, position: Int)

    private var layoutNetworkState = R.layout.layout_network_state
    private var networkState: NetworkState? = null
    private lateinit var items: ArrayList<ITEM>

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int) = if (hasExtraRow() && position == itemCount - 1) layoutNetworkState else layoutId

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType){
        layoutNetworkState -> LoadMoreViewHolder.create(parent, retrySwitchback)
        layoutId -> {
            val inflater = LayoutInflater.from(context)
            val binding: VDB = DataBindingUtil.inflate(inflater, layoutId, parent, false)
            getViewHolder(binding, viewType)
        }
        else -> throw IllegalArgumentException("unknown view type $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (getItemViewType(position)){
        layoutNetworkState -> (holder as LoadMoreViewHolder).bind(networkState)
        layoutId -> onBindViewHolder(getItem(position)!!, holder, position)
        else -> throw IllegalArgumentException("unknown view type $position")
    }

    internal fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    internal fun isPositionFooter(position: Int): Boolean = (hasExtraRow() && position == itemCount - 1)
}