package com.midtrans.sdk.uikit.internal.scheduler

import io.reactivex.Scheduler

internal interface BaseSdkScheduler {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun ui(): Scheduler

    fun newThread(): Scheduler
}
