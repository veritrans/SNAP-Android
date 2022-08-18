package com.midtrans.sdk.uikit.internal.di

import android.content.Context
import com.midtrans.sdk.uikit.internal.di.viewmodel.UtilModule
import com.midtrans.sdk.uikit.internal.di.viewmodel.ViewModelFactoryModule
import com.midtrans.sdk.uikit.internal.di.viewmodel.ViewModelModule
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitActivity
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
    fun inject(activity: CreditCardActivity)
    fun inject(activity: DirectDebitActivity)
}