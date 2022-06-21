package com.midtrans.sdk.corekit.internal.di

import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.usecase.BankTransferUsecase
import com.midtrans.sdk.corekit.internal.usecase.DirectDebitUsecase
import dagger.Module
import dagger.Provides

@Module
internal class UsecaseModule {
    @Provides
    fun provideBankTransferUsecase(snapRepository: SnapRepository): BankTransferUsecase {
        return BankTransferUsecase(snapRepository)
    }

    @Provides
    fun provideDirectDebitUsecase(snapRepository: SnapRepository): DirectDebitUsecase {
        return DirectDebitUsecase(snapRepository)
    }
}