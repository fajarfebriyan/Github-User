package com.astro.test.fajarfebriyan.feature

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.fajarfebriyan.R
import com.astro.test.fajarfebriyan.core.base.BasePagingAdapter
import com.astro.test.fajarfebriyan.core.extension.onClick
import com.astro.test.fajarfebriyan.data.model.Favorite
import com.astro.test.fajarfebriyan.data.model.User
import com.astro.test.fajarfebriyan.databinding.ItemSearchUserBinding
import com.bumptech.glide.Glide

class SearchAdapter (
    private val listener: OnContentItemClickListener<User>,
    private val context: Context,
    private var favList: List<Favorite>,
    retrySwitchback: () -> Unit
) : BasePagingAdapter<User, SearchAdapter.ViewHolder, ItemSearchUserBinding>(comparator, context, retrySwitchback) {

    companion object {
        val comparator = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    interface OnContentItemClickListener<ITEM> {
        fun onItemViewClicked(v: View, item: ITEM, isLike: Boolean)
    }

    override val layoutId = R.layout.item_search_user
    override fun getViewHolder(
        binding: ItemSearchUserBinding,
        viewType: Int
    ) = ViewHolder(binding)

    override fun onBindViewHolder(
        item: User,
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as ViewHolder).bind(context, item, listener, favList)
    }

    fun updateFavList(favs: List<Favorite>) {
        favList = favs
    }

    class ViewHolder internal constructor(private val binding: ItemSearchUserBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(
            context: Context,
            item: User,
            listener: OnContentItemClickListener<User>,
            favList: List<Favorite>
        ) {

            val mutableList: MutableList<Favorite> = mutableListOf()
            mutableList.addAll(favList)

            Glide.with(context)
                .load(item.avatar_url)
                .placeholder(R.drawable.ic_empty_photo)
                .into(binding.civProfile)

            binding.tvName.text = item.login

            for (fav in mutableList) {
                if (fav.id == item.id) {
                    item.isLiked = true
                }
            }

            if (item.isLiked) {
                binding.loveRed.visibility = View.VISIBLE
                binding.loveWhite.visibility = View.GONE
            } else {
                binding.loveWhite.visibility = View.VISIBLE
                binding.loveRed.visibility = View.GONE
            }

            binding.loveWhite.onClick {
                item.isLiked = true
                binding.loveRed.visibility = View.VISIBLE
                binding.loveWhite.visibility = View.GONE
                listener.onItemViewClicked(it, item, true)

                val fav = Favorite(item.id)
                mutableList.add(fav)
            }

            binding.loveRed.onClick {
                item.isLiked = false
                binding.loveWhite.visibility = View.VISIBLE
                binding.loveRed.visibility = View.GONE
                listener.onItemViewClicked(it, item, false)

                var index = -1
                for (fav in mutableList) {
                    if (fav.id == item.id) {
                        index = mutableList.indexOf(fav)
                        return@onClick
                    }
                }
                if (index != -1) mutableList.removeAt(index)
            }

        }
    }
}