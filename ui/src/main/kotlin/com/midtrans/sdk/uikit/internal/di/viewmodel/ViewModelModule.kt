package com.midtrans.sdk.uikit.internal.di.viewmodel

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailViewModel
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BankTransferDetailViewModel::class)
    abstract fun bindWalletPaymentViewModel(viewModel: BankTransferDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreditCardViewModel::class)
    abstract fun bindCreditCardViewModel(viewModel: CreditCardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DirectDebitViewModel::class)
    abstract fun bindDirectDebitViewModel(viewModel: DirectDebitViewModel): ViewModel
}