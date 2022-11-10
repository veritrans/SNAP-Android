package com.midtrans.sdk.sample.presentation.shop.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.R

@Composable
fun Header() {
    Row {
        Column {
            Text(text = "Prospero Sounds",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onSecondary,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Discover a new products from us",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSecondary,
                fontFamily = FontFamily.Serif
            )
        }
        Image(
            painter = painterResource(id = R.drawable.header),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(start = 32.dp)
        )
    }
}