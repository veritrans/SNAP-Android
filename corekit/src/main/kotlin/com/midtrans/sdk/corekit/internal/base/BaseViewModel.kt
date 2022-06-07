package com.midtrans.sdk.corekit.internal.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal open class BaseViewModel : ViewModel() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun dispose() {
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }

    protected fun observe(execute: () -> Disposable) {
        compositeDisposable.add(execute())
    }
}