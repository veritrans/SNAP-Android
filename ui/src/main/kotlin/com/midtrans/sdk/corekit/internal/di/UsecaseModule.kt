package com.midtrans.sdk.corekit.internal.di

import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.data.repository.CoreApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.MerchantApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.scheduler.SdkScheduler
import com.midtrans.sdk.corekit.internal.usecase.PaymentUsecase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class UsecaseModule {
    @Provides
    fun providePaymentUsecase(
        snapRepository: SnapRepository,
        coreApiRepository: CoreApiRepository,
        merchantApiRepository: MerchantApiRepository,
        @Named("merchant_client_key") clientKey: String,
        eventAnalytics: EventAnalytics
    ): PaymentUsecase {
        return PaymentUsecase(
            SdkScheduler(),
            snapRepository,
            coreApiRepository,
            merchantApiRepository,
            clientKey,
            eventAnalytics
        )
    }
}