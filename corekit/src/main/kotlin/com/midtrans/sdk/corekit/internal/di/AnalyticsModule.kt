package com.midtrans.sdk.corekit.internal.di

import android.content.Context
import com.midtrans.sdk.corekit.BuildConfig
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.MixpanelTracker
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AnalyticsModule {
    @Provides
    @Singleton
    fun provideMixpanelTracker(
        context: Context
    ): MixpanelTracker {
        return MixpanelTracker(MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_TOKEN))
    }

    @Provides
    @Singleton
    fun provideEventAnalytics(
        mixpanelTracker: MixpanelTracker
    ): EventAnalytics {
        return EventAnalytics(mixpanelTracker)
    }
}