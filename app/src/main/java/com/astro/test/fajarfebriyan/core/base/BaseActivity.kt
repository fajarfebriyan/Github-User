package com.astro.test.fajarfebriyan.core.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.astro.test.fajarfebriyan.R
import com.astro.test.fajarfebriyan.core.exception.Failure
import com.astro.test.fajarfebriyan.core.interactor.LocalCase
import com.astro.test.fajarfebriyan.core.util.common.Loading
import com.astro.test.fajarfebriyan.core.util.common.LoadingState
import com.astro.test.fajarfebriyan.core.util.common.Status
import com.astro.test.fajarfebriyan.data.network.general.NetworkState
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

abstract class BaseActivity<VDB: ViewDataBinding, VM: ViewModel>() : AppCompatActivity(){

    private var progressDialog: ProgressDialog? = null

    @get:LayoutRes
    protected abstract val layoutId: Int
    protected val binding: VDB by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<VDB>(this, layoutId)
    }

    @Inject
    lateinit var preferenceLocalCase: LocalCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOnCreate(savedInstanceState)
    }

    open fun setupOnCreate(savedInstanceState: Bundle?) {
        onPreCreateView(savedInstanceState)
        collectExtras(intent.extras)

        setupLifecycleOwner()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        setupViewUI()

        onPostCreateView(savedInstanceState)
    }

    abstract fun setupViewUI()

    internal fun setupLifecycleOwner() {
        binding.lifecycleOwner = this
    }

    protected open fun onPreCreateView(savedInstanceState: Bundle?) {}
    protected open fun collectExtras(bundle: Bundle?) {}
    protected open fun onPostCreateView(savedInstanceState: Bundle?) {}

    private fun showProgress() = progressStatus(View.VISIBLE)
    private fun hideProgress() = progressStatus(View.GONE)
    private fun progressStatus(viewStatus: Int) {
        when (viewStatus) {
            View.VISIBLE -> {
                hideProgress()
                progressDialog = Loading.showDialog(this)
            }
            View.GONE -> progressDialog?.let { if (it.isShowing) it.cancel() }
        }
    }

    open fun handleLoading(it: LoadingState?) {
        if (it?.status == Status.RUNNING) showProgress() else hideProgress()
    }

    internal fun showToast(@StringRes message: Int) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    internal fun showToast(string: String) {
        Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
    }

    internal fun showSnackbar(@StringRes message: Int) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    internal fun showSnackbarWithAction(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { action.invoke() }
        snackBar.show()
    }

    protected open fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.BadRequest, Failure.NotFound -> renderFailure(R.string.something_went_wrong)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            else -> {renderFailure(R.string.something_went_wrong)}
        }
    }

    protected open fun handleNetworkCase(state: NetworkState) {
        if (state.status != com.astro.test.fajarfebriyan.data.network.general.Status.SUCCESS) {
            state.msg?.let {
                if (it.isNotEmpty())
                    showToast(it)
                else
                    renderFailure(R.string.failure_server_error)
            }
        }
    }

    protected open fun renderFailure(@StringRes message: Int) {
        showToast(message)
    }

    internal fun showToast(@StringRes message: Int, isSuccess: Boolean) {
        //Helper.showResultToast(this, message, isSuccess)
    }

    internal open fun changePage(
        cls: Class<out Activity?>,
        bundle: Bundle?,
        vararg flags: Int?
    ) {
        val intent = Intent(this, cls)
        if (bundle != null) intent.putExtras(bundle)
        for (flag in flags) {
            if (flag != null) intent.addFlags(flag)
        }
        startActivity(intent)
    }
}