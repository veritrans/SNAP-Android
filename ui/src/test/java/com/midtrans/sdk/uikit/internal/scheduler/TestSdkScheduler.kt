package com.midtrans.sdk.uikit.internal.scheduler

import com.midtrans.sdk.uikit.internal.scheduler.BaseSdkScheduler
import io.reactivex.schedulers.Schedulers

internal class TestSdkScheduler : BaseSdkScheduler {
    override fun computation() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
    override fun newThread() = Schedulers.trampoline()
}