package com.midtrans.sdk.uikit.internal.di

import android.content.Context
import com.midtrans.sdk.uikit.internal.di.viewmodel.UtilModule
import com.midtrans.sdk.uikit.internal.di.viewmodel.ViewModelFactoryModule
import com.midtrans.sdk.uikit.internal.di.viewmodel.ViewModelModule
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailActivity
import com.midtrans.sdk.uikit.internal.presentation.conveniencestore.ConvenienceStoreActivity
import com.midtrans.sdk.uikit.internal.presentation.ewallet.WalletActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.SavedCardActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobPaymentActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobSelectionActivity
import com.midtrans.sdk.uikit.internal.presentation.ewallet.DeepLinkActivity
import com.midtrans.sdk.uikit.internal.presentation.paylater.PayLaterActivity
import com.midtrans.sdk.uikit.internal.presentation.paymentoption.PaymentOptionActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        CoreModule::class,
        UtilModule::class
    ]
)
internal interface UiKitComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: Context): Builder
        fun build(): UiKitComponent
    }

    fun inject(activity: BankTransferDetailActivity)
    fun inject(activity: WalletActivity)
    fun inject(activity: DeepLinkActivity)

    fun inject(activity: CreditCardActivity)
    fun inject(activity: DirectDebitActivity)
    fun inject(activity: SavedCardActivity)
    fun inject(activity: UobPaymentActivity)
    fun inject(activity: UobSelectionActivity)
    fun inject(activity: PayLaterActivity)
    fun inject(activity: ConvenienceStoreActivity)
    fun inject(activity: PaymentOptionActivity)
}