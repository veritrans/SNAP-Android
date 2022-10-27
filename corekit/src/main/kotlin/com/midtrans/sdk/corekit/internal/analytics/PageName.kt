package com.midtrans.sdk.corekit.internal.analytics

import androidx.annotation.StringDef

internal class PageName {
    @StringDef(
        PAYMENT_LIST_PAGE,
        CREDIT_DEBIT_CARD_PAGE,
        GOPAY_QR_PAGE,
        GOPAY_DEEPLINK_PAGE,
        SHOPEEPAY_QR_PAGE,
        SHOPEEPAY_DEEPLINK_PAGE,
        INDOMARET_PAGE,
        ALFAMART_PAGE,
        AKULAKU_PAGE,
        KLIK_BCA_PAGE,
        BCA_KLIK_PAY_PAGE,
        OCTO_CLICKS_PAGE,
        DANAMON_ONLINE_PAGE,
        BRIMO_PAGE,
        UOB_PAGE,
        REDIRECTION_PAGE,
        SUCCESS_PAGE,
        PROCESSING_PAGE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val PAYMENT_LIST_PAGE = "Payment List Page"
        const val CREDIT_DEBIT_CARD_PAGE = "Credit/Debit Card Page"
        const val GOPAY_QR_PAGE = "Gopay QR page"
        const val GOPAY_DEEPLINK_PAGE = "Gopay Deeplink Instruction"
        const val SHOPEEPAY_QR_PAGE = "Shopeepay QR page"
        const val SHOPEEPAY_DEEPLINK_PAGE = "Shopeepay Deeplink Instruction"
        const val INDOMARET_PAGE = "Indomaret Page"
        const val ALFAMART_PAGE = "Alfamart Page"
        const val AKULAKU_PAGE = "Akulaku  Instruction"
        const val KLIK_BCA_PAGE = "KlikBCA  Instruction"
        const val BCA_KLIK_PAY_PAGE = "BCAKlikPay  Instruction"
        const val OCTO_CLICKS_PAGE = "OCTOClicks Instruction"
        const val DANAMON_ONLINE_PAGE = "Danamon Online Banking Instruction"
        const val BRIMO_PAGE = "BRImo  Instruction"
        const val UOB_PAGE = "UOB EZpay  Instruction"
        const val REDIRECTION_PAGE = "Redirection Page"
        const val SUCCESS_PAGE = "Success Page"
        const val PROCESSING_PAGE = "Processing Page"
    }
}
