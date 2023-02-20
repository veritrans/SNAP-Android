package com.midtrans.sdk.corekit.internal.scheduler

import io.reactivex.schedulers.Schedulers

internal class TestSdkScheduler : BaseSdkScheduler {
    override fun computation() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
    override fun newThread() = Schedulers.trampoline()
}