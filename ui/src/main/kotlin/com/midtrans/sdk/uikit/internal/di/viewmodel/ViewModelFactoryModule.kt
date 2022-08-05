package com.midtrans.sdk.uikit.internal.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.uikit.internal.di.viewmodel.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}