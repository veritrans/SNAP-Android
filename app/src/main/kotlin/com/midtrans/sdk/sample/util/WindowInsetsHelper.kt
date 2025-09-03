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
        return if (Build.VERSION.SDK_INT >= 35) {
            this.navigationBarsPadding()
        } else {
            this
        }
    }
    
    @Composable
    fun Modifier.safeSystemBarsPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.systemBarsPadding()
        } else {
            this
        }
    }
    
    @Composable
    fun Modifier.safeStatusBarsPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.statusBarsPadding()
        } else {
            this
        }
    }

    @Composable
    fun Modifier.safeBottomPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        } else {
            this.padding(bottom = 16.dp)
        }
    }
    
    @Composable
    fun Modifier.safeImePadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.imePadding()
        } else {
            this
        }
    }
}

@Composable
internal fun Modifier.safeStatusBarsPadding(): Modifier = 
    with(WindowInsetsHelper) { this@safeStatusBarsPadding.safeStatusBarsPadding() }

@Composable
internal fun Modifier.safeBottomPadding(): Modifier = 
    with(WindowInsetsHelper) { this@safeBottomPadding.safeBottomPadding() }