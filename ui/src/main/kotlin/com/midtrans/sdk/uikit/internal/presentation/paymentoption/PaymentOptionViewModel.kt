package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.AKULAKU
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.ALFAMART
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.ALL_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BCA_KLIKPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BCA_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BNI_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BRI_EPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BRI_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CIMB_CLICKS
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CREDIT_CARD
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.DANAMON_ONLINE
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.E_CHANNEL
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.GOPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.INDOMARET
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.KLIK_BCA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.PERMATA_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.UOB_EZPAY
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.model.PaymentList
import com.midtrans.sdk.uikit.external.model.PaymentMethod
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.model.PaymentMethodList

class PaymentOptionViewModel : ViewModel() {

    fun initiateList(paymentList: PaymentList, isTabletDevice: Boolean): PaymentMethodList {
        val paymentMethodItems = mutableListOf<PaymentMethodItem>()

        paymentList.options.forEach {
            if (it.status != "up")
                return@forEach

            if (it.category == "bank_transfer") {
                val index = paymentMethodItems.indexOfFirst { item ->
                    item.type == it.category
                }

                if (index == -1) {
                    paymentMethodItems.add(
                        PaymentMethodItem(
                            type = it.category,
                            titleId = R.string.payment_summary_bank_transfer,
                            methods = listOf(it.type.orEmpty()),
                            icons = getBankTransferIconList(it, null)
                        )
                    )
                } else {
                    paymentMethodItems[index] = PaymentMethodItem(
                        type = it.category,
                        titleId = R.string.payment_summary_bank_transfer,
                        methods = getBankTransferMethodList(paymentMethodItems[index], it),
                        icons = getBankTransferIconList(it, paymentMethodItems[index].icons)
                    )
                }
            } else if (isSoloMethod(it)) {
                paymentMethodItems.add(
                    element = PaymentMethodItem(
                        type = it.type!!,
                        titleId = getSoloMethodTitle(it.type),
                        methods = if (it.type == UOB_EZPAY) {
                            it.mode.orEmpty()
                        } else {
                            listOf()
                        },
                        icons = getSoloMethodIcons(it.type)
                    )
                )
            } else if (it.category == "cstore") {
                paymentMethodItems.add(
                    element = PaymentMethodItem(
                        type = it.type!!,
                        titleId = getConvenienceStoreTitle(it.type),
                        icons = getConvenienceStoreIcons(it.type)
                    )
                )
            } else if (
                isShopeepayInTablet(it, isTabletDevice) ||
                isShopeepayInPhone(it, isTabletDevice)
            ) {
                paymentMethodItems.add(
                    element = PaymentMethodItem(
                        type = SHOPEEPAY,
                        titleId = R.string.payment_summary_shopeepay,
                        icons = listOf(
                            R.drawable.ic_outline_shopeepay_40,
                            R.drawable.ic_outline_qris_40
                        )
                    )
                )
            }
        }
        return PaymentMethodList(paymentMethodItems)
    }

    private fun getBankTransferMethodList(
        paymentMethodItem: PaymentMethodItem,
        paymentMethod: PaymentMethod
    ): List<String> {
        return paymentMethodItem
            .methods
            .toMutableList()
            .apply { add(paymentMethod.type.orEmpty()) }
    }

    private fun getConvenienceStoreTitle(type: String): Int {
        return if (type == INDOMARET)
            R.string.payment_summary_indomaret
        else
            R.string.payment_summary_alfamart
    }

    private fun getSoloMethodTitle(type: String): Int {
        return when (type) {
            KLIK_BCA -> R.string.payment_summary_klikbca
            BCA_KLIKPAY -> R.string.payment_summary_bcaklikpay
            CIMB_CLICKS -> R.string.payment_summary_octoclicks
            BRI_EPAY -> R.string.payment_summary_brimo
            DANAMON_ONLINE -> R.string.payment_summary_danamonob
            UOB_EZPAY -> R.string.payment_summary_uobezpay
            CREDIT_CARD -> R.string.payment_summary_cc_dc
            AKULAKU -> R.string.payment_summary_akulaku
            else -> R.string.payment_summary_gopay
        }
    }

    private fun isShopeepayInPhone(paymentMethod: PaymentMethod, isTabletDevice: Boolean): Boolean {
        return paymentMethod.type == "shopeepay" && !isTabletDevice
    }

    private fun isShopeepayInTablet(
        paymentMethod: PaymentMethod,
        isTabletDevice: Boolean
    ): Boolean {
        return paymentMethod.type == "qris" &&
            paymentMethod.acquirer == "shopeepay" &&
            isTabletDevice
    }

    private fun getConvenienceStoreIcons(type: String): List<Int> {
        return when (type) {
            INDOMARET -> listOf(R.drawable.ic_outline_indomaret_40)
            ALFAMART -> listOf(
                R.drawable.ic_outline_alfamart_40,
                R.drawable.ic_outline_alfamidi_40,
                R.drawable.ic_outline_dandan_40,
            )
            else -> listOf()
        }
    }

    private fun getSoloMethodIcons(type: String): List<Int> {
        return when (type) {
            KLIK_BCA -> listOf(R.drawable.ic_outline_klikbca_40)
            BCA_KLIKPAY -> listOf(R.drawable.ic_outline_bcaklikpay_40)
            CIMB_CLICKS -> listOf(R.drawable.ic_outline_octoclicks_40_2)
            BRI_EPAY -> listOf(R.drawable.ic_outline_brimo_40)
            DANAMON_ONLINE -> listOf(R.drawable.ic_outline_danamonob_40)
            UOB_EZPAY -> listOf(R.drawable.ic_outline_uobezpay_40)
            CREDIT_CARD -> listOf(
                R.drawable.ic_outline_visa_40,
                R.drawable.ic_outline_mastercard_40,
                R.drawable.ic_outline_jcb_40_2,
                R.drawable.ic_outline_amex_40
            )
            AKULAKU -> listOf(R.drawable.ic_outline_akulaku_40)
            GOPAY -> listOf(R.drawable.ic_outline_gopay_40_2, R.drawable.ic_outline_qris_40)
            else -> listOf()
        }
    }

    private fun getBankTransferIconList(
        paymentMethod: PaymentMethod,
        iconList: List<Int>?
    ): List<Int> {
        val latestIcons = iconList?.toMutableList() ?: mutableListOf()
        when (paymentMethod.type) {
            PERMATA_VA -> latestIcons.add(R.drawable.ic_outline_permata_40)
            BCA_VA -> latestIcons.add(R.drawable.ic_outline_bca_40)
            BNI_VA -> latestIcons.add(R.drawable.ic_outline_bni_40)
            BRI_VA -> latestIcons.add(R.drawable.ic_outline_bri_40)
            E_CHANNEL -> latestIcons.add(R.drawable.ic_outline_mandiri_40)
            ALL_VA -> latestIcons.addAll(
                listOf(
                    R.drawable.ic_outline_permata_40,
                    R.drawable.ic_outline_bca_40,
                    R.drawable.ic_outline_bni_40,
                    R.drawable.ic_outline_bri_40,
                    R.drawable.ic_outline_mandiri_40
                )
            )
        }
        return latestIcons.distinct()
    }

    private fun isSoloMethod(paymentMethod: PaymentMethod) =
        paymentMethod.type == KLIK_BCA ||
            paymentMethod.type == BCA_KLIKPAY ||
            paymentMethod.type == CIMB_CLICKS ||
            paymentMethod.type == BRI_EPAY ||
            paymentMethod.type == DANAMON_ONLINE ||
            paymentMethod.type == UOB_EZPAY ||
            paymentMethod.type == CREDIT_CARD ||
            paymentMethod.type == AKULAKU ||
            paymentMethod.type == GOPAY
}