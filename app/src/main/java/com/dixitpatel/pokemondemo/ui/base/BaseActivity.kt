package com.dixitpatel.pokemondemo.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerAppCompatActivity

/**
 *  Base Activity : all activity will extend this and pass their ViewModel object as Generic type.
 */
abstract class BaseActivity<out T : ViewModel?> : DaggerAppCompatActivity() {

    lateinit var me: BaseActivity<*>

    private var viewModel: T? = null

    protected inline fun <reified T : ViewDataBinding> binding(
            @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView(this, resId) }

    /**
     * @return view model instance
     */
    abstract fun getViewModel(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            me = this
            viewModel = if (viewModel == null) getViewModel() else viewModel
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}