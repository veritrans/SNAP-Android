package com.midtrans.sdk.sample.presentation.shop.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.midtrans.sdk.sample.model.Product
import com.midtrans.sdk.sample.model.getData

@Composable
fun ProductListPage(onClick: (Product) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy((-30).dp),
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 24.dp),
        content = {
            item {
                Header()
            }
            items(items = getData(), key = { it.name }) {
                ProductsCard(it) {
                    onClick.invoke(it)
                }
            }
        })
}

@Composable
fun ProductsCard(products: Product, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val color = Color(products.color)
        Card(
            Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .height(120.dp),
            backgroundColor = color.copy(alpha = 0.2f),
            elevation = 0.dp,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                bottomEnd = 16.dp,
                topEnd = 5.dp,
                bottomStart = 5.dp
            ),
        ) {
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                color.copy(alpha = 0.5f),
                                color.copy(alpha = 0.2f),
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 32.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = products.name,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Rp.${(products.price).toString().dropLast(2)}",
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = products.image),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(110.dp)
                .padding(end = 32.dp)
                .align(Alignment.BottomEnd)
        )
    }
}