package com.midtrans.sdk.uikit.internal.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class SdkScheduler : BaseSdkScheduler {
    override fun io() = Schedulers.io()

    override fun computation() = Schedulers.computation()

    override fun ui() = AndroidSchedulers.mainThread()

    override fun newThread() = Schedulers.newThread()
}