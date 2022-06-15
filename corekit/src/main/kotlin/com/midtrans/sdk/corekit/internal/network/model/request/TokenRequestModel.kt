package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.midtrans.sdk.corekit.api.model.*

/**
 * Created by ziahaqi on 7/19/16.
 */
class TokenRequestModel {
    @SerializedName("customer_details")
    private val costumerDetails: CustomerDetails

    @SerializedName("item_details")
    private var itemDetails: ArrayList<ItemDetails>

    @SerializedName("transaction_details")
    private var transactionDetails: SnapTransactionDetails

    @SerializedName("credit_card")
    private var creditCard: CreditCard? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("permata_va")
    var permataVa: BankTransferRequestModel? = null

    @SerializedName("bca_va")
    var bcaVa: BankTransferRequestModel? = null

    @SerializedName("bni_va")
    var bniVa: BankTransferRequestModel? = null

    @SerializedName("bri_va")
    private var briVa: BankTransferRequestModel? = null

    @SerializedName("enabled_payments")
    var enabledPayments: List<String>? = null
    var expiry: ExpiryModel? = null
    var promo: SnapPromo? = null

    // Custom field
    @SerializedName("custom_field1")
    var customField1: String? = null

    @SerializedName("custom_field2")
    var customField2: String? = null

    @SerializedName("custom_field3")
    var customField3: String? = null

    @SerializedName("gopay")
    var gopay: Gopay? = null

    @SerializedName("shopeepay")
    var shopeepay: Shopeepay? = null

    @SerializedName("uob_ezpay")
    var uobEzpay: UobEzpay? = null

    constructor(
        transactionDetails: SnapTransactionDetails, itemDetails: ArrayList<ItemDetails>,
        customerDetails: CustomerDetails
    ) {
        this.transactionDetails = transactionDetails
        this.itemDetails = itemDetails
        costumerDetails = customerDetails
    }

    constructor(
        transactionDetails: SnapTransactionDetails,
        itemDetails: ArrayList<ItemDetails>,
        customerDetails: CustomerDetails,
        creditCard: CreditCard?
    ) {
        this.transactionDetails = transactionDetails
        this.itemDetails = itemDetails
        costumerDetails = customerDetails
        this.creditCard = creditCard
    }

    fun getCreditCard(): CreditCard? {
        return creditCard
    }

    fun setCreditCard(creditCard: CreditCard?) {
        this.creditCard = creditCard
    }

    fun getCostumerDetails(): CustomerDetails {
        return costumerDetails
    }

    fun getItemDetails(): ArrayList<ItemDetails> {
        return itemDetails
    }

    fun setItemDetails(itemDetails: ArrayList<ItemDetails>) {
        this.itemDetails = itemDetails
    }

    fun getTransactionDetails(): SnapTransactionDetails {
        return transactionDetails
    }

    fun setTransactionDetails(transactionDetails: SnapTransactionDetails) {
        this.transactionDetails = transactionDetails
    }

    val string: String
        get() {
            var json = ""
            try {
                val gson = Gson()
                json = gson.toJson(this)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return json
        }

    fun setBriVa(briVa: BankTransferRequestModel?) {
        this.briVa = briVa
    }
}