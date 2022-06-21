package com.midtrans.sdk.corekit.internal.di

import android.content.Context
import com.midtrans.sdk.corekit.SnapCore
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        BankTransferModule::class,
        RestClientModule::class,
        RepositoryModule::class
    ]
)
interface SnapComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: Context): Builder
        fun build(): SnapComponent
    }

    fun inject(snapCore: SnapCore)

}
