package com.midtrans.sdk.corekit.api.model

import androidx.annotation.StringDef

class PaymentType {
    @StringDef(
        CREDIT_CARD,
        PERMATA_VA,
        BCA_VA,
        BNI_VA,
        BRI_VA,
        ALL_VA,
        E_CHANNEL,
        KLIK_BCA,
        BCA_KLIKPAY,
        CIMB_CLICKS,
        BRI_EPAY,
        INDOMARET,
        GOPAY,
        SHOPEEPAY,
        QRIS,
        DANAMON_ONLINE,
        AKULAKU,
        ALFAMART,
        UOB_WEB,
        UOB_APP,
        UOB_EZPAY,
        SHOPEEPAY_QRIS
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val CREDIT_CARD = "credit_card"

        //BANK TRANSFER
        const val PERMATA_VA = "permata_va"
        const val BCA_VA = "bca_va"
        const val BNI_VA = "bni_va"
        const val BRI_VA = "bri_va"
        const val ALL_VA = "other_va"
        const val E_CHANNEL = "echannel"

        //DIRECT DEBIT
        const val KLIK_BCA = "bca_klikbca"
        const val BCA_KLIKPAY = "bca_klikpay"
        const val CIMB_CLICKS = "cimb_clicks"
        const val BRI_EPAY = "bri_epay"
        const val DANAMON_ONLINE = "danamon_online"

        //E-WALLET
        const val SHOPEEPAY_QRIS = "shopeepay_qris"
        const val QRIS = "qris"

        //PAY LATER
        const val AKULAKU = "akulaku"

        //CONVENIENCE STORE
        const val INDOMARET = "indomaret"
        const val ALFAMART = "alfamart"

        const val GOPAY = "gopay"
        const val SHOPEEPAY = "shopeepay"
        const val UOB_WEB = "uobweb"
        const val UOB_APP = "uobapp"
        const val UOB_EZPAY = "uob_ezpay"
    }
}