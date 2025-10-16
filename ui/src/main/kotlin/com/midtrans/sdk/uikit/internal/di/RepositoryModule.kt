package com.midtrans.sdk.uikit.internal.di

import com.midtrans.sdk.uikit.internal.data.repository.ImageRepositoryImpl
import com.midtrans.sdk.uikit.internal.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindImageRepository(
        imageRepositoryImpl: ImageRepositoryImpl
    ): ImageRepository
}