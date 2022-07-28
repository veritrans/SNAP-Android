package com.midtrans.sdk.uikit.internal.view

interface BankResources {
    fun iconBordered(): iconbyBySize
    fun name(): String

}

data class iconbyBySize(
    val ic24: Int,
    val ic16: Int
)