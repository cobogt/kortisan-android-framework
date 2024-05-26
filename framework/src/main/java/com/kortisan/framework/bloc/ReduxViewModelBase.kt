package com.kortisan.framework.bloc

import android.app.Application
import androidx.lifecycle.ViewModel

abstract class ReduxViewModelBase private constructor(): ViewModel() {
    private lateinit var _application: Application
    private lateinit var _vmActionDispatcher: ViewModelActionDispatcher

    constructor(
        application: Application,
        vmActionDispatcher: ViewModelActionDispatcher
    ): this() {
        _application = application
        _vmActionDispatcher = vmActionDispatcher
    }

    @Suppress("UNCHECKED_CAST")
    fun< T: Application> getApplication(): T = _application as T
    fun getDispatcher() = _vmActionDispatcher
}