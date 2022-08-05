package com.midtrans.sdk.uikit.internal.di

import android.content.Context
import com.midtrans.sdk.uikit.internal.di.viewmodel.ViewModelFactoryModule
import com.midtrans.sdk.uikit.internal.di.viewmodel.ViewModelModule
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailActivity
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
        CoreModule::class
    ]
)
interface UiKitComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: Context): Builder
        fun build(): UiKitComponent
    }

    fun inject(activity: BankTransferDetailActivity)
}