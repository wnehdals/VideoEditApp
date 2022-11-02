package com.jdm.videoeditapp.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.view.BaseAppBar
import com.jdm.videoeditapp.view.ProgressDialog

abstract class BaseActivity<T : ViewDataBinding>: AppCompatActivity() {
    @get:LayoutRes
    abstract val layoutId: Int
    private lateinit var _binding: T
    val binding: T
        get() = _binding

    private var progressDialog: ProgressDialog? = null
    private var baseAppBar: BaseAppBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        initState()
    }
    open fun initState() {
        subscribe()
        initView()
        initEvent()
    }
    abstract fun subscribe()
    abstract fun initEvent()
    abstract fun initView()

    fun showProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = ProgressDialog(this, getString(R.string.loading))
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
    fun setBaseAppBar(title: String = "") {
        if (supportActionBar == null) {
            throw IllegalStateException("can not found supportActionBar")
        }
        baseAppBar = BaseAppBar(this, supportActionBar)
        baseAppBar?.setUpActionBar()
        setAppBarTitle(title)
    }
    fun setAppBarTitle(title: String) {
        baseAppBar?.setUpActionBar()
        if(!title.isNullOrEmpty()) {
            baseAppBar?.setTitle(title)
        }
    }
    fun setAppBarColor(color: String) {
        baseAppBar?.setBackgroundColor(color)
    }
    fun appBarLeftButtonClicked(callback: (View) -> Unit) {
        baseAppBar?.leftButtonClickListener = callback
    }
    fun appBarRightButtonClicked(callback: (View) -> Unit) {
        baseAppBar?.rightButtonClickListener = callback
    }
    fun setBackKey() {
        baseAppBar?.setLeftButtonDrawable(R.drawable.ic_left_arrow)
        appBarLeftButtonClicked {
            onBackPressed()
        }
    }
}