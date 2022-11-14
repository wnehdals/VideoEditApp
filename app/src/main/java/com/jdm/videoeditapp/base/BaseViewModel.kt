package com.jdm.videoeditapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdm.videoeditapp.model.SourceMedia
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel: ViewModel() {
    open var ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
        _toast.value = throwable.message
        _loading.value = false
    }
    open val _toast = MutableLiveData<String>()
    val toast: LiveData<String> get() = _toast

    open val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading



}