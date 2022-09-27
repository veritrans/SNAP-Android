package com.midtrans.sdk.uikit.internal.di.viewmodel

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailViewModel
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardViewModel
import com.midtrans.sdk.uikit.internal.presentation.creditcard.SavedCardViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobPaymentViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobSelectionViewModel
import com.midtrans.sdk.uikit.internal.presentation.ewallet.WalletViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BankTransferDetailViewModel::class)
    abstract fun bindBankTransferViewModel(viewModel: BankTransferDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WalletViewModel::class)
    abstract fun bindWalletPaymentViewModel(viewModel: BankTransferDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreditCardViewModel::class)
    abstract fun bindCreditCardViewModel(viewModel: CreditCardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DirectDebitViewModel::class)
    abstract fun bindDirectDebitViewModel(viewModel: DirectDebitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UobPaymentViewModel::class)
    abstract fun bindUobPaymentViewModel(viewModel: UobPaymentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UobSelectionViewModel::class)
    abstract fun bindUobSelectionViewModel(viewModel: UobSelectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedCardViewModel::class)
    abstract fun bindSavedCardViewModel(viewModel: SavedCardViewModel): ViewModel
}