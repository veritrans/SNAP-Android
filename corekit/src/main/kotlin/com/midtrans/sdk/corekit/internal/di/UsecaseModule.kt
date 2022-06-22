package com.midtrans.sdk.corekit.internal.di

import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.scheduler.SdkScheduler
import com.midtrans.sdk.corekit.internal.usecase.PaymentUsecase
import dagger.Module
import dagger.Provides

@Module
internal class UsecaseModule {
    @Provides
    fun providePaymentUsecase(snapRepository: SnapRepository): PaymentUsecase {
        return PaymentUsecase(SdkScheduler(), snapRepository)
    }
}