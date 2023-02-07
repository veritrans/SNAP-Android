package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.model.Product

class OrderReviewLegacyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Text(text = "Order Review Legacy Activity")
        }
    }

    companion object {
        private const val EXTRA_PRODUCT = "orderReviewLegacy.extra.product"
        private const val EXTRA_INPUT_INSTALLMENT = "orderReviewLegacy.extra.installment"
        private const val EXTRA_INPUT_ISREQUIRED = "orderReviewLegacy.extra.isRequired"
        private const val EXTRA_INPUT_ACQUIRINGBANK = "orderReviewLegacy.extra.acquiringBank"
        private const val EXTRA_INPUT_EXPIRY = "orderReviewLegacy.extra.expiry"
        private const val EXTRA_INPUT_CCAUTHENTICATIONTYPE = "orderReviewLegacy.extra.ccAuthenticationType"
        private const val EXTRA_INPUT_WHITELISTBINS = "orderReviewLegacy.extra.whitelistBins"
        private const val EXTRA_INPUT_BLACKLISTBINS = "orderReviewLegacy.extra.blacklistBins"
        private const val EXTRA_INPUT_BCAVA = "orderReviewLegacy.extra.bcaVa"
        private const val EXTRA_INPUT_BNIVA = "orderReviewLegacy.extra.bniVa"
        private const val EXTRA_INPUT_PERMATAVA = "orderReviewLegacy.extra.permataVa"
        private const val EXTRA_INPUT_ISSAVEDCARD = "orderReviewLegacy.extra.isSavedCard"
        private const val EXTRA_INPUT_ISPREAUTH = "orderReviewLegacy.extra.isPreAuth"
        private const val EXTRA_INPUT_ISBNIPOINTS = "orderReviewLegacy.extra.isBniPoints"
        private const val EXTRA_INPUT_ISSHOWALLPAYMENT = "orderReviewLegacy.extra.isShowAllPayment"
        private const val EXTRA_INPUT_PAYMENTCHANNELS = "orderReviewLegacy.extra.paymentChannels"
        private const val EXTRA_INPUT_COLOR = "orderReviewLegacy.extra.inputColor"

        fun getOrderReviewLegacyActivityIntent(
            activityContext: Context,
            product: Product,
            installmentBank: String,
            isRequiredInstallment: Boolean,
            acquiringBank: String,
            customExpiry: String,
            authenticationType: String,
            isSavedCard: Boolean,
            isPreAuth: Boolean,
            isBniPointsOnly: Boolean,
            isShowAllPaymentChannels: Boolean,
            paymentChannels: ArrayList<ListItem>,
            whitelistBins: String,
            blacklistBins: String,
            bcaVa: String,
            bniVa: String,
            permataVa: String,
            color: String
        ): Intent {
            return Intent(activityContext, OrderReviewLegacyActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
                putExtra(EXTRA_INPUT_INSTALLMENT, installmentBank)
                putExtra(EXTRA_INPUT_ISREQUIRED, isRequiredInstallment)
                putExtra(EXTRA_INPUT_ACQUIRINGBANK, acquiringBank)
                putExtra(EXTRA_INPUT_EXPIRY, customExpiry)
                putExtra(EXTRA_INPUT_CCAUTHENTICATIONTYPE, authenticationType)
                putExtra(EXTRA_INPUT_ISSAVEDCARD, isSavedCard)
                putExtra(EXTRA_INPUT_ISPREAUTH, isPreAuth)
                putExtra(EXTRA_INPUT_ISBNIPOINTS, isBniPointsOnly)
                putExtra(EXTRA_INPUT_ISSHOWALLPAYMENT, isShowAllPaymentChannels)
                putExtra(EXTRA_INPUT_PAYMENTCHANNELS, paymentChannels)
                putExtra(EXTRA_INPUT_WHITELISTBINS, whitelistBins)
                putExtra(EXTRA_INPUT_BLACKLISTBINS, blacklistBins)
                putExtra(EXTRA_INPUT_BCAVA, bcaVa)
                putExtra(EXTRA_INPUT_BNIVA, bniVa)
                putExtra(EXTRA_INPUT_PERMATAVA, permataVa)
                putExtra(EXTRA_INPUT_COLOR, color)
            }
        }
    }
}