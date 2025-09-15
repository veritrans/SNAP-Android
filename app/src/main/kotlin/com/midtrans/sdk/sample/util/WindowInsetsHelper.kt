package com.midtrans.sdk.sample.util

import android.os.Build
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal object WindowInsetsHelper {
    
    @Composable
    fun Modifier.safeNavigationBarsPadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this.navigationBarsPadding()
        }
        return this
    }
    
    @Composable
    fun Modifier.safeSystemBarsPadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this.systemBarsPadding()
        }
        return this
    }
    
    @Composable
    fun Modifier.safeStatusBarsPadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this.statusBarsPadding()
        }
        return this
    }

    @Composable
    fun Modifier.safeBottomPadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        }
        return this.padding(bottom = 16.dp)
    }
    
    @Composable
    fun Modifier.safeImePadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this.imePadding()
        }
        return this
    }
}

@Composable
internal fun Modifier.safeStatusBarsPadding(): Modifier = 
    with(WindowInsetsHelper) { this@safeStatusBarsPadding.safeStatusBarsPadding() }

@Composable
internal fun Modifier.safeBottomPadding(): Modifier = 
    with(WindowInsetsHelper) { this@safeBottomPadding.safeBottomPadding() }