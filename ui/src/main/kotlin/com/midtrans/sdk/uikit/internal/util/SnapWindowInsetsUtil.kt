package com.midtrans.sdk.uikit.internal.util

import android.os.Build
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Handles window insets for Android 15+ edge-to-edge display
 */
internal object SnapWindowInsetsUtil {
    
    @Composable
    fun Modifier.snapNavigationBarsPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.navigationBarsPadding()
        } else {
            this
        }
    }
    
    @Composable
    fun Modifier.snapSystemBarsPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.systemBarsPadding()
        } else {
            this
        }
    }
    
    @Composable
    fun Modifier.snapStatusBarsPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.statusBarsPadding()
        } else {
            this
        }
    }
    
    /**
     * Adds bottom padding for buttons and CTAs
     */
    @Composable
    fun Modifier.snapSafeBottomPadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        } else {
            this.padding(bottom = 16.dp)
        }
    }
    
    @Composable
    fun Modifier.snapImePadding(): Modifier {
        return if (Build.VERSION.SDK_INT >= 35) {
            this.imePadding()
        } else {
            this
        }
    }
}

// Extension functions for SDK's internal UI components
@Composable
internal fun Modifier.snapStatusBarsPadding(): Modifier = 
    with(SnapWindowInsetsUtil) { this@snapStatusBarsPadding.snapStatusBarsPadding() }

@Composable
internal fun Modifier.snapSafeBottomPadding(): Modifier = 
    with(SnapWindowInsetsUtil) { this@snapSafeBottomPadding.snapSafeBottomPadding() }