package com.midtrans.sdk.uikit.internal.di.viewmodel

import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import dagger.Module
import dagger.Provides

@Module
internal class UtilModule {
    @Provides
    fun provideDateTimeUtil() = DateTimeUtil
}