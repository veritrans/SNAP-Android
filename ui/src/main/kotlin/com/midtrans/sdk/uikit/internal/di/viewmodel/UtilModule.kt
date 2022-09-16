package com.midtrans.sdk.uikit.internal.di.viewmodel

import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import dagger.Module
import dagger.Provides

@Module
internal class UtilModule {
    @Provides
    fun provideDateTimeUtil() = DateTimeUtil

    @Provides
    fun provideSnapCreditCardUtil() = SnapCreditCardUtil

    @Provides
    fun provideErrorCard() = ErrorCard
}