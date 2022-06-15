package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * contains information about payment charge api call like,  status message, status code,
 * transaction id, transaction status etc.
 *
 *
 * Created by chetan on 30/10/15.
 */
data class TransactionResponse(
    /**
     * status_code : 200 status_message : Success, Credit Card 3D Secure transaction is successful
     * transaction_id : 49ab48d4-93e1-4b52-a706-2f7a746b99d0 saved_token_id :
     * 48111119d4a368-602b-4352-a1ac-23bad256741d masked_card : 481111-1114 order_id : 109380dv0
     * gross_amount : 10000.00 payment_type : credit_card transaction_time : 2015-10-30 19:57:33
     * transaction_status : capture fraud_status : accept saved_token_id_expired_at : 2025-10-30
     * 19:57:36 approval_code : 1446209855742 secure_token : true bank : bni eci : 05
     */
    @SerializedName("status_code")
    var statusCode: String? = null,

    @SerializedName("status_message")
    var statusMessage: String? = null,

    @SerializedName("transaction_id")
    var transactionId: String? = null,

    @SerializedName("saved_token_id")
    var savedTokenId: String? = null,

    @SerializedName("masked_card")
    var maskedCard: String? = null,

    @SerializedName("three_ds_version")
    var threeDsVersion: String? = null,

    @SerializedName("order_id")
    var orderId: String? = null,

    @SerializedName("gross_amount")
    var grossAmount: String? = null,

    @SerializedName("payment_type")
    var paymentType: String? = null,

    @SerializedName("transaction_time")
    var transactionTime: String? = null,

    @SerializedName("transaction_status")
    var transactionStatus: String? = null,

    @SerializedName("fraud_status")
    var fraudStatus: String? = null,

    @SerializedName("saved_token_id_expired_at")
    var savedTokenIdExpiredAt: String? = null,

    @SerializedName("approval_code")
    var approvalCode: String? = null,

    @SerializedName("secure_token")
    var isSecureToken: Boolean = false,

    @SerializedName("permata_va_number")
    var permataVANumber: String? = null,

    @SerializedName("permata_expiration")
    var permataExpiration: String? = null,

    @SerializedName("va_numbers")
    var accountNumbers: List<VaNumber>? = null,

    @SerializedName("bca_klikbca_expire_time")
    var bcaKlikBcaExpiration: String? = null,

    @SerializedName("bca_va_number")
    var bcaVaNumber: String? = null,

    @SerializedName("bca_expiration")
    var bcaExpiration: String? = null,

    @SerializedName("bni_va_number")
    var bniVaNumber: String? = null,

    @SerializedName("bni_expiration")
    var bniExpiration: String? = null,

    @SerializedName("bri_va_number")
    var briVaNumber: String? = null,

    @SerializedName("bri_expiration")
    var briExpiration: String? = null,

    @SerializedName("billpayment_expiration")
    var mandiriBillExpiration: String? = null,

    @SerializedName("xl_tunai_order_id")
    var xlTunaiOrderId: String? = null,

    @SerializedName("xl_tunai_merchant_id")
    var xlTunaiMerchantId: String? = null,

    @SerializedName("xl_expiration")
    var xlTunaiExpiration: String? = null,

    @SerializedName("installment_term")
    var installmentTerm: String? = null,

    @SerializedName("gopay_expiration")
    var gopayExpiration: String? = null,

    @SerializedName("alfamart_expire_time")
    var alfamartExpireTime: String? = null,

    @SerializedName("gopay_expiration_raw")
    var gopayExpirationRaw: String? = null,

    @SerializedName("indomaret_expire_time")
    var indomaretExpireTime: String? = null,

    @SerializedName("redirect_url")
    var redirectUrl: String? = null,

    @SerializedName("uob_ezpay_web_url")
    var uobWebUrl: String? = null,

    @SerializedName("uob_ezpay_deeplink_url")
    var uobDeeplinkUrl: String? = null,

    @SerializedName("pdf_url")
    var pdfUrl: String? = null,
    var bank: String? = null,
    var eci: String? = null,
    //for mandiri bill pay
    /**
     * bill number/code
     */
    @SerializedName("bill_key")
    var paymentCode: String? = null,

    /**
     * company or biller code.
     */
    @SerializedName("biller_code")
    var companyCode: String? = null,

    /**
     * payment code for Indomaret
     */
    @SerializedName("payment_code")
    var paymentCodeResponse: String? = null,

    @SerializedName("finish_redirect_url")
    var finishRedirectUrl: String? = null,

    @SerializedName("kioson_expire_time")
    var kiosonExpireTime: String? = null,

    @SerializedName("validation_messages")
    var validationMessages: ArrayList<String>? = null,

    @SerializedName("point_balance")
    var pointBalance: Double = 0.0,

    @SerializedName("point_balance_amount")
    var pointBalanceAmount: String? = null,

    @SerializedName("point_redeem_amount")
    var pointRedeemAmount: Double = 0.0,

    //for gopay
    @SerializedName("qr_code_url")
    var qrCodeUrl: String? = null,

    @SerializedName("deeplink_url")
    var deeplinkUrl: String? = null,

    //currently for shopeepay, could be used for other QRIS payment except gopay (gopay still using qr_code_url)
    @SerializedName("qris_url")
    var qrisUrl: String? = null,
    var currency: String? = null

) : Serializable