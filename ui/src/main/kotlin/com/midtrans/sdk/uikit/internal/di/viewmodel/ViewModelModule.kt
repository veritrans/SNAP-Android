package com.midtrans.sdk.uikit.internal.di.viewmodel

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailViewModel
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferListViewModel
import com.midtrans.sdk.uikit.internal.presentation.conveniencestore.ConvenienceStoreViewModel
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobPaymentViewModel
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobSelectionViewModel
import com.midtrans.sdk.uikit.internal.presentation.ewallet.DeepLinkViewModel
import com.midtrans.sdk.uikit.internal.presentation.ewallet.WalletViewModel
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentViewModel
import com.midtrans.sdk.uikit.internal.presentation.paylater.PayLaterViewModel
import com.midtrans.sdk.uikit.internal.presentation.paymentoption.PaymentOptionViewModel
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.SuccessScreenViewModel
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
    @ViewModelKey(PayLaterViewModel::class)
    abstract fun bindPayLaterViewModel(viewModel: PayLaterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConvenienceStoreViewModel::class)
    abstract fun bindConvenienceStoreViewModel(viewModel: ConvenienceStoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SuccessScreenViewModel::class)
    abstract fun bindSuccessScreenViewModel(viewModel: SuccessScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeepLinkViewModel::class)
    abstract fun bindDeepLinkViewModel(viewModel: DeepLinkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoadingPaymentViewModel::class)
    abstract fun bindLoadingPaymentViewModel(viewModel: LoadingPaymentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PaymentOptionViewModel::class)
    abstract fun bindPaymentOptionViewModel(viewModel: PaymentOptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BankTransferListViewModel::class)
    abstract fun bindBankTransferListViewModel(viewModel: BankTransferListViewModel): ViewModel
}