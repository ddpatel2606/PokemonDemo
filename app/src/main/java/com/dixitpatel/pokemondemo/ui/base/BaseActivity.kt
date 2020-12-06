package com.dixitpatel.pokemondemo.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity<T : ViewModel?> : DaggerAppCompatActivity() {

    interface OnKeyboardVisibilityListener {
        fun onVisibilityChanged(visible: Boolean)
    }

    var me: BaseActivity<*>? = null
    private var viewModel: T? = null

    /**
     * @return view model instance
     */
    abstract fun getViewModel(): T

    fun startActivity(viewStart: View?, transactionName: String?, intent: Intent?)
    {
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewStart!!, transactionName!!)
        ActivityCompat.startActivity(this, intent!!, options.toBundle())
    }

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