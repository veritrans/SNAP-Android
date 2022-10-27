package com.midtrans.sdk.corekit.api.model

import androidx.annotation.StringDef

class PaymentType {
    @StringDef(
        CREDIT_CARD,
        BANK_TRANSFER,
        PERMATA_VA,
        BCA_VA,
        BNI_VA,
        BRI_VA,
        OTHER_VA,
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
        CSTORE,
        ALFAMART,
        UOB_EZPAY_WEB,
        UOB_EZPAY_APP,
        UOB_EZPAY,
        SHOPEEPAY_QRIS,
        GOPAY_QRIS
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val CREDIT_CARD = "credit_card"

        //BANK TRANSFER
        const val BANK_TRANSFER = "bank_transfer"
        const val PERMATA_VA = "permata_va"
        const val BCA_VA = "bca_va"
        const val BNI_VA = "bni_va"
        const val BRI_VA = "bri_va"
        const val OTHER_VA = "other_va"
        const val E_CHANNEL = "echannel"

        //DIRECT DEBIT
        const val KLIK_BCA = "bca_klikbca"
        const val BCA_KLIKPAY = "bca_klikpay"
        const val CIMB_CLICKS = "cimb_clicks"
        const val BRI_EPAY = "bri_epay"
        const val DANAMON_ONLINE = "danamon_online"
        const val UOB_EZPAY = "uob_ezpay"

        //DIRECT DEBIT - UOB MODE //TODO move this to uob mode independent class
        const val UOB_EZPAY_WEB = "uob_ezpay_web"
        const val UOB_EZPAY_APP = "uob_ezpay_deeplink"

        //E-WALLET
        const val SHOPEEPAY_QRIS = "shopeepay_qris"
        const val GOPAY_QRIS = "gopay_qris"
        const val QRIS = "qris"
        const val GOPAY = "gopay"
        const val SHOPEEPAY = "shopeepay"

        //PAY LATER
        const val AKULAKU = "akulaku"

        //CONVENIENCE STORE
        const val CSTORE = "cstore"
        const val INDOMARET = "indomaret"
        const val ALFAMART = "alfamart"

    }
}