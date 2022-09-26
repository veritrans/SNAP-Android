package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import javax.inject.Inject

internal class UobSelectionViewModel @Inject constructor(
    private val dateTimeUtil: DateTimeUtil
): ViewModel() {

    private var expiredTime = dateTimeUtil.plusDateBy(dateTimeUtil.getCurrentMillis(), 1) //TODO temporary is 24H, later get value from request snap if set

    fun getExpiredTime() = expiredTime

    fun getExpiredHour() = dateTimeUtil.getExpiredHour(expiredTime)

}