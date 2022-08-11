package com.midtrans.sdk.uikit.internal.di.viewmodel

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailViewModel
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BankTransferDetailViewModel::class)
    abstract fun bindWalletPaymentViewModel(viewModel: BankTransferDetailViewModel): ViewModel
}