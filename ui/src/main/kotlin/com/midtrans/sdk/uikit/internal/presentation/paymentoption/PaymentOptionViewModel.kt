package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.api.model.Address
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.PaymentMethod
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.AKULAKU
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.ALFAMART
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.OTHER_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BANK_TRANSFER
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BCA_KLIKPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BCA_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BNI_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BRI_EPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BRI_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CIMB_CLICKS
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CREDIT_CARD
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CSTORE
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.DANAMON_ONLINE
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.E_CHANNEL
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.GOPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.INDOMARET
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.KLIK_BCA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.PERMATA_VA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY_QRIS
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.UOB_EZPAY
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.model.PaymentMethodList

class PaymentOptionViewModel : ViewModel() {

    fun initiateList(
        paymentList: List<PaymentMethod>,
        isTabletDevice: Boolean
    ): PaymentMethodList {
        val paymentMethodItems = mutableListOf<PaymentMethodItem>()

        paymentList.forEach {
            if (isValidMethod(it.type, isTabletDevice)) {
                paymentMethodItems.add(
                    element = PaymentMethodItem(
                        type = it.type,
                        titleId = getTitle(it.type, isTabletDevice),
                        methods = it.channels,
                        icons = getIcons(it.type, it.channels, isTabletDevice)
                    )
                )
            }
        }
        return PaymentMethodList(paymentMethodItems)
    }

    private fun isValidMethod(type: String, isTabletDevice: Boolean): Boolean {
        return type == CSTORE
                || type == BANK_TRANSFER
                || type == KLIK_BCA
                || type == BCA_KLIKPAY
                || type == CIMB_CLICKS
                || type == BRI_EPAY
                || type == DANAMON_ONLINE
                || type == UOB_EZPAY
                || type == CREDIT_CARD
                || type == AKULAKU
                || type == GOPAY
                || (type == SHOPEEPAY && !isTabletDevice)
                || (type == SHOPEEPAY_QRIS && isTabletDevice)
    }

    private fun getIcons(type: String, channels: List<String>, isTabletDevice: Boolean): List<Int> {
        return when (type) {
            BANK_TRANSFER -> getBankTransferIconList(channels)
            KLIK_BCA -> listOf(R.drawable.ic_outline_klikbca_40)
            BCA_KLIKPAY -> listOf(R.drawable.ic_outline_bcaklikpay_40)
            CIMB_CLICKS -> listOf(R.drawable.ic_outline_octoclicks_40_2)
            BRI_EPAY -> listOf(R.drawable.ic_outline_brimo_40)
            DANAMON_ONLINE -> listOf(R.drawable.ic_outline_danamonob_40)
            UOB_EZPAY -> listOf(R.drawable.ic_outline_uobezpay_40)
            CREDIT_CARD -> listOf(
                R.drawable.ic_outline_visa_40,
                R.drawable.ic_outline_mastercard_40,
                R.drawable.ic_outline_jcb_40,
                R.drawable.ic_outline_amex_40
            )
            AKULAKU -> listOf(R.drawable.ic_outline_akulaku_40)
            GOPAY -> if (isTabletDevice)
                listOf(
                    R.drawable.ic_outline_gopaylogo_40,
                    R.drawable.ic_outline_qris_40
                )
            else
                listOf(R.drawable.ic_outline_gopaylogo_40)
            SHOPEEPAY -> { listOf(R.drawable.ic_outline_shopeepaylogo_40) }
            SHOPEEPAY_QRIS -> {
                listOf(
                    R.drawable.ic_outline_shopeepaylogo_40,
                    R.drawable.ic_outline_qris_40
                )
            }
            INDOMARET -> listOf(R.drawable.ic_outline_indomaret_40)
            ALFAMART -> listOf(
                R.drawable.ic_outline_alfamart_40,
                R.drawable.ic_outline_alfamidi_40,
                R.drawable.ic_outline_dandan_40,
            )
            else -> listOf()
        }
    }

    private fun getTitle(type: String, isTabletDevice: Boolean): Int {
        return when (type) {
            BANK_TRANSFER -> R.string.payment_summary_bank_transfer
            KLIK_BCA -> R.string.payment_summary_klikbca
            BCA_KLIKPAY -> R.string.payment_summary_bcaklikpay
            CIMB_CLICKS -> R.string.payment_summary_octoclicks
            BRI_EPAY -> R.string.payment_summary_brimo
            DANAMON_ONLINE -> R.string.payment_summary_danamonob
            UOB_EZPAY -> R.string.payment_summary_uobezpay
            CREDIT_CARD -> R.string.payment_summary_cc_dc
            AKULAKU -> R.string.payment_summary_akulaku
            INDOMARET -> R.string.payment_summary_indomaret
            ALFAMART -> R.string.payment_summary_alfamart
            SHOPEEPAY -> R.string.payment_title_shopeepay
            SHOPEEPAY_QRIS -> R.string.payment_summary_shopeepay
            else -> if(isTabletDevice)R.string.payment_summary_gopay else R.string.payment_title_gopay
        }
    }

    private fun getBankTransferIconList(channels: List<String>): List<Int> {
        val icons = mutableListOf<Int>()
        channels.forEach { channel ->
            when (channel) {
                PERMATA_VA -> icons.add(R.drawable.ic_outline_permata_40)
                BCA_VA -> icons.add(R.drawable.ic_outline_bca_40)
                BNI_VA -> icons.add(R.drawable.ic_outline_bni_40)
                BRI_VA -> icons.add(R.drawable.ic_outline_bri_40)
                E_CHANNEL -> icons.add(R.drawable.ic_outline_mandiri_40)
                OTHER_VA -> icons.add(R.drawable.ic_bank_other_bordered_40)
            }
        }

        return icons
    }

    fun getCustomerInfo(customerDetails: CustomerDetails?): CustomerInfo? {
        if (customerDetails == null)
            return null

        val nameList = mutableListOf<String>()
        customerDetails.firstName?.run { nameList.add(this) }
        customerDetails.lastName?.run { nameList.add(this) }
        val name = nameList.joinToString(separator = " ")

        val phone = customerDetails.phone.orEmpty()

        val addressLines =
            if (
                isAddressProvided(customerDetails.shippingAddress)
                || isAddressProvided(customerDetails.billingAddress)
            ) {
                if (isAddressProvided(customerDetails.shippingAddress)) {
                    createAddressLines(customerDetails.shippingAddress!!)
                } else {
                    createAddressLines(customerDetails.billingAddress!!)
                }
            } else {
                emptyList()
            }

        return CustomerInfo(name = name, phone = phone, addressLines = addressLines)
    }

    private fun isAddressProvided(address: Address?): Boolean {
        return address != null && (!address.address.isNullOrBlank() || !address.city.isNullOrBlank() || !address.postalCode.isNullOrBlank())
    }

    private fun createAddressLines(address: Address): MutableList<String> {
        val addresses = mutableListOf<String>()
        address.address?.run { addresses.add(this) }
        address.city?.run { addresses.add(this) }
        address.postalCode?.run { addresses.add(this) }
        return addresses
    }
}