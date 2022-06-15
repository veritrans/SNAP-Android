package com.midtrans.sdk.corekit.api.model

import android.util.Log
import androidx.annotation.StringDef
import com.midtrans.sdk.corekit.internal.util.notEmptyOrNull
import kotlin.collections.ArrayList


/**
 * It contains information about transaction like orderId, amount,
 * itemDetails
 *
 *
 * Created by shivam on 11/5/15.
 */

/**
 * @param orderId  order id of transaction.
 * must unique.
 * Using this id later u can check status of
 * transaction.
 * @param amount   amount to charge.
 * @param currency currency
 * @param paymentMethod payment method using which user wants to perform transaction. use payment methods from [ ]
*/
data class TransactionRequest(
    var orderId: String? = null,
    var amount: Double = 0.0,
    @Currency.Def var currency: String = Currency.IDR,
    var paymentMethod: Int = PAYMENT_METHOD_NOT_SELECTED //  payment method using which user wants to perform transaction. use payment methods from [ ]
) {

    /**
     * It helps to identify whether to execute transaction in secure manner or not.
     */
    var isSecureCard = true
        private set
    var cardClickType: String? = null
        private set
    var isPromoEnabled = false
    var promoCodes: List<String>? = null

    /**
     * It contains an extra information that you want to display on bill.
     */
    var billInfoModel: BillInfoModel? = null

    /**
     * list of purchased items.
     */
    var itemDetails: ArrayList<ItemDetails> = ArrayList<ItemDetails>()

    /**
     * List of billing addresses.
     */
    private var mBillingAddressArrayList: ArrayList<BillingAddress> = ArrayList<BillingAddress>()

    /**
     * List of shipping addresses.
     */
    private var mShippingAddressArrayList: ArrayList<ShippingAddress> = ArrayList<ShippingAddress>()

    /**
     * contains details about customer
     */
    private var mCustomerDetails: CustomerDetails? = null

    /**
     * contains user app deeplink for merchant app
     */
    var gopay: Gopay? = null
    var shopeepay: Shopeepay? = null
    var uobEzpay: UobEzpay? = null

    /**
     * helps to identify whether to use ui or not.
     */
    protected var isUiEnabled = true
        private set
    var creditCard: CreditCard? = null
    private val customObject: Map<String, String>? = null
    var expiry: ExpiryModel? = null
    var customField1: String? = null
    var customField2: String? = null
    var customField3: String? = null
    var permataVa: BankTransferRequestModel? = null
    var bcaVa: BankTransferRequestModel? = null
    var bniVa: BankTransferRequestModel? = null
    var briVa: BankTransferRequestModel? = null
    var enabledPayments: List<String>? = null




    var customerDetails: CustomerDetails?
        get() = mCustomerDetails
        set(customerDetails) {
            mCustomerDetails = sanitizeCustomerDetails(customerDetails)
        }

    private fun sanitizeCustomerDetails(customerDetails: CustomerDetails?): CustomerDetails? {
        customerDetails?.apply {
            firstName = firstName.notEmptyOrNull()
            lastName = lastName.notEmptyOrNull()
            email = email.notEmptyOrNull()
            phone = phone.notEmptyOrNull()
            sanitizeBillingAddress(billingAddress)
            sanitizeShippingAddress(shippingAddress)
        }
        return customerDetails
    }

    private fun sanitizeBillingAddress(billingAddress: BillingAddress?) {
        billingAddress?.apply {
            address = address.notEmptyOrNull()
            firstName = firstName.notEmptyOrNull()
            lastName = lastName.notEmptyOrNull()
            city = city.notEmptyOrNull()
            postalCode = postalCode.notEmptyOrNull()
            phone = phone.notEmptyOrNull()
            countryCode = countryCode.notEmptyOrNull()
        }

    }

    private fun sanitizeShippingAddress(shippingAddress: ShippingAddress?) {
        shippingAddress?.apply {
            address = address.notEmptyOrNull()
            firstName = firstName.notEmptyOrNull()
            lastName = lastName.notEmptyOrNull()
            city = city.notEmptyOrNull()
            postalCode = postalCode.notEmptyOrNull()
            phone = phone.notEmptyOrNull()
            countryCode = countryCode.notEmptyOrNull()

        }
    }

    var billingAddressArrayList: ArrayList<BillingAddress>
        get() = mBillingAddressArrayList
        set(billingAddressArrayList) {
            mBillingAddressArrayList = billingAddressArrayList
        }
    var shippingAddressArrayList: ArrayList<ShippingAddress>
        get() = mShippingAddressArrayList
        set(shippingAddressArrayList) {
            mShippingAddressArrayList = shippingAddressArrayList
        }

    /**
     * It will help to enable/disable default ui provided by sdk. By default it is true, set it to
     * false to use your own ui to show transaction.
     *
     * @param enableUi is UI mode enabled
     */
    protected fun enableUi(enableUi: Boolean) {
        isUiEnabled = enableUi
    }

    /**
     * It is used in case of payment using credit card.
     * this method being deprecated since v1.9.x, please don't implement this method
     *
     * @param clickType    use click type from Constants.
     * @param isSecureCard is secure
     */
    @Deprecated("")
    fun setCardPaymentInfo(clickType: String, isSecureCard: Boolean) {
        Log.i("clicktype:$clickType,isSecured:$isSecureCard", "")
        cardClickType = clickType
        this.isSecureCard = isSecureCard
    }

    companion object {
        private val PAYMENT_METHOD_NOT_SELECTED = -1
    }
}