package com.midtrans.sdk.uikit.internal.util

import android.os.Build
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Handles window insets for Android 15+ edge-to-edge display
 */
internal object SnapWindowInsetsUtil {

    @Composable
    fun Modifier.snapStatusBarsPadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this.statusBarsPadding()
        }
        return this
    }

    @Composable
    fun Modifier.snapSafeBottomPadding(): Modifier {
        if (Build.VERSION.SDK_INT >= 35) {
            return this
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        }
        return this.padding(bottom = 16.dp)
    }
}

@Composable
internal fun Modifier.snapStatusBarsPadding(): Modifier = 
    with(SnapWindowInsetsUtil) { this@snapStatusBarsPadding.snapStatusBarsPadding() }

@Composable
internal fun Modifier.snapSafeBottomPadding(): Modifier = 
    with(SnapWindowInsetsUtil) { this@snapSafeBottomPadding.snapSafeBottomPadding() }