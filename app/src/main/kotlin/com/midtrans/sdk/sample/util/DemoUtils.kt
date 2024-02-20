package com.midtrans.sdk.sample.util

import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.uikit.api.CardTokenRequest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DemoUtils {

    fun getFormattedTime(time: Long): String {
        // Quoted "Z" to indicate UTC, no timezone offset
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
        df.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return df.format(Date(time))
    }

    fun populateAcquiringBank(acquiringBank: String): String? {
        var bank: String? = null
        if (acquiringBank != DemoConstant.NO_ACQUIRING_BANK) {
            bank = acquiringBank
        }
        return bank
    }

    fun populateCCAuthType(isPreAuth: Boolean): String {
        val ccAuthType = if (isPreAuth) {
            CardTokenRequest.TYPE_AUTHORIZE
        } else {
            CardTokenRequest.TYPE_CAPTURE
        }
        return ccAuthType
    }

    fun populateWhitelistBins(whitelistBins: String, isBniPointOnly: Boolean = false): ArrayList<String> {
        var output = arrayListOf<String>()
        if (isBniPointOnly) {
            output.add("bni")
        } else if (whitelistBins.isNotEmpty()) {
            output = ArrayList(whitelistBins.split(", "))
        }
        return output
    }

    fun populateBlacklistBins(blacklistBins: String): ArrayList<String> {
        var output = arrayListOf<String>()
        if (blacklistBins.isNotEmpty()){
            output = ArrayList(blacklistBins.split(", "))
        }
        return output
    }

    fun populateEnabledPayment(paymentChannels: ArrayList<ListItem>, isShowAllPaymentChannels: Boolean = true): List<String>? {
        var payment: List<String>? = null
        val channels = mutableListOf<String>()
        if (!isShowAllPaymentChannels) {
            for (i in paymentChannels) {
                channels.add(i.type)
            }
            payment = channels
        }
        return payment
    }
}