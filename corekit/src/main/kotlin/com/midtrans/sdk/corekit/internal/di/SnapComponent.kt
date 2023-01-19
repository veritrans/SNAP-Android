package com.midtrans.sdk.corekit.internal.di

import android.content.Context
import com.midtrans.sdk.corekit.SnapCore
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        UsecaseModule::class,
        RestClientModule::class,
        RepositoryModule::class,
        AnalyticsModule::class
    ]
)
interface SnapComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: Context): Builder
        @BindsInstance
        fun merchantUrl(@Named("merchant_url") url: String): Builder
        @BindsInstance
        fun merchantClientKey(@Named("merchant_client_key") clientKey: String): Builder
        @BindsInstance
        fun enableLog(@Named("enable_log") enableLog: Boolean): Builder
        fun build(): SnapComponent
    }

    fun inject(snapCore: SnapCore)

}
