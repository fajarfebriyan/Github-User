package com.astro.test.fajarfebriyan.core.util.pagging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.fajarfebriyan.R
import com.astro.test.fajarfebriyan.data.network.general.NetworkState
import com.astro.test.fajarfebriyan.data.network.general.Status

class LoadMoreViewHolder(view: View, private val retryCallback: () -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retry = view.findViewById<Button>(R.id.retry_button)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)
    private var isFirstRetryShow = true

    init {
        retry.setOnClickListener {
            retryCallback()
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): LoadMoreViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_network_state, parent, false)
            return LoadMoreViewHolder(view, retryCallback)
        }

        fun toVisbility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun bind(networkState: NetworkState?) {
        if (networkState?.status == Status.FAILED) {
            if (isFirstRetryShow) {
                isFirstRetryShow = false
                retryCallback()
            }
        }
        progressBar.visibility = toVisbility(networkState?.status == Status.RUNNING)
        retry.visibility = toVisbility(networkState?.status == Status.FAILED)
        errorMsg.visibility = toVisbility(networkState?.msg != null)
        errorMsg.text = networkState?.msg
    }
}
