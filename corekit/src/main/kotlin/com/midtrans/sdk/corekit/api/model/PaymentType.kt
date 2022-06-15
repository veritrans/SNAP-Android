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
        MANDIRI_CLICKPAY,
        BRI_EPAY,
        TELKOMSEL_CASH,
        XL_TUNAI,
        BBM_MONEY,
        MANDIRI_ECASH,
        INDOMARET,
        INDOSAT_DOMPETKU,
        KIOSON,
        GCI,
        GOPAY,
        SHOPEEPAY,
        QRIS,
        DANAMON_ONLINE,
        INDOMARET_CSTORE,
        AKULAKU,
        ALFAMART,
        UOB_WEB,
        UOB_APP,
        UOB_EZPAY
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val CREDIT_CARD = "credit_card"
        const val PERMATA_VA = "permata_va"
        const val BCA_VA = "bca_va"
        const val BNI_VA = "bni_va"
        const val BRI_VA = "bri_va"
        const val ALL_VA = "other_va"
        const val E_CHANNEL = "echannel"
        const val KLIK_BCA = "bca_klikbca"
        const val BCA_KLIKPAY = "bca_klikpay"
        const val CIMB_CLICKS = "cimb_clicks"
        const val MANDIRI_CLICKPAY = "mandiri_clickpay"
        const val BRI_EPAY = "bri_epay"
        const val TELKOMSEL_CASH = "telkomsel_cash"
        const val XL_TUNAI = "xl_tunai"
        const val BBM_MONEY = "bbm_money"
        const val MANDIRI_ECASH = "mandiri_ecash"
        const val INDOMARET = "indomaret"
        const val INDOSAT_DOMPETKU = "indosat_dompetku"
        const val KIOSON = "kioson"
        const val GCI = "gci"
        const val GOPAY = "gopay"
        const val SHOPEEPAY = "shopeepay"
        const val QRIS = "qris"
        const val DANAMON_ONLINE = "danamon_online"
        const val INDOMARET_CSTORE = "cstore"
        const val AKULAKU = "akulaku"
        const val ALFAMART = "alfamart"
        const val UOB_WEB = "uobweb"
        const val UOB_APP = "uobapp"
        const val UOB_EZPAY = "uob_ezpay"
    }
}