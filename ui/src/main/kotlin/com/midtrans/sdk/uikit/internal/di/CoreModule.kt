package com.midtrans.sdk.uikit.internal.di

import com.midtrans.sdk.corekit.SnapCore
import dagger.Module
import dagger.Provides

@Module
internal class CoreModule {
    @Provides
    fun provideSnapCore(): SnapCore {
        return SnapCore.getInstance()!!
    }
}