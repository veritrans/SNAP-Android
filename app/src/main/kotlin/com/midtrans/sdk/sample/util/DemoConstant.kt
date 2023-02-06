package com.midtrans.sdk.sample.util

import com.midtrans.sdk.uikit.api.model.Authentication

object DemoConstant {
    //Dropdown Title
    const val ACQUIRING_BANK = "Acquiring Bank"
    const val COLOR_THEME = "Color Theme"
    const val INSTALLMENT = "Installment"
    const val IS_INSTALLMENT_REQUIRED = "Is Installment Required"
    const val CUSTOM_EXPIRY = "Custom Expiry"
    const val CREDIT_CARD_AUTHENTICATION = "Credit Card Authentication"
    const val SAVED_CARD = "Saved Card"
    const val PRE_AUTH = "Pre-Auth"
    const val BNI_POINT_ONLY = "Bni Point Only"
    const val SHOW_ALL_PAYMENT_CHANNELS = "Show All Payment Channels"

    //Input Field Title
    const val CUSTOM_BCA_VA = "Custom BCA VA"
    const val CUSTOM_BNI_VA = "Custom BNI VA"
    const val CUSTOM_PERMATA_VA = "Custom Permata VA"

    //Color Theme
    const val COLOR_DEFAULT = "Default"
    const val COLOR_RED = "Red"
    const val COLOR_GREEN = "Green"
    const val COLOR_BLUE = "Blue"

    //Installment
    const val NO_INSTALLMENT = "No Installment"
    const val MANDIRI = "Mandiri"
    const val CIMB = "CIMB"
    const val BCA = "BCA"
    const val BNI = "BNI"
    const val MAYBANK = "MayBank"
    const val BRI = "BRI"
    const val OFFLINE = "Offline"

    //Is Required
    const val OPTIONAL = "Optional"
    const val REQUIRED = "Required"

    //Custom Expiry
    const val NONE = "None"
    const val ONE_MINUTE = "1 Minute"
    const val FIVE_MINUTE = "5 Minute"
    const val ONE_HOUR = "1 Hour"

    //Acquiring Bank
    const val NO_ACQUIRING_BANK = "No Acquiring Bank"
    const val MEGA = "Mega"

    //Authentication
    const val AUTH_NONE = Authentication.AUTH_NONE
    const val AUTH_3DS = Authentication.AUTH_3DS

    //Boolean List
    const val DISABLED = "Disabled"
    const val ENABLED = "Enabled"

    // Payment Channels
    const val SHOW_ALL = "Show All"
    const val SHOW_SELECTED_ONLY = "Show Selected Only"
}