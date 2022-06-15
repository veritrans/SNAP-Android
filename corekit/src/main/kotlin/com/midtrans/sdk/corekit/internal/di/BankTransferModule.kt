package com.midtrans.sdk.corekit.internal.di

import com.midtrans.sdk.corekit.internal.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.usecase.BankTransferUsecase
import dagger.Module
import dagger.Provides

@Module
internal class BankTransferModule {
    @Provides
    fun provideBankTransferUsecase(snapRepository: SnapRepository): BankTransferUsecase {
        return BankTransferUsecase(snapRepository)
    }
}