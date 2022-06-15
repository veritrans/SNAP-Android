package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * It contains an extra information that you want to display on bill. add your custom field and it's
 * value in string format.
 *
 * Created by shivam on 11/3/15.
 */
data class BillInfoModel(
    @field:SerializedName("bill_info1") var billInfo1: String, @field:SerializedName(
        "bill_info2"
    ) var billInfo2: String,


    @SerializedName("bill_info3")
    var billInfo3: String? = null,

    @SerializedName("bill_info4")
    var billInfo4: String? = null,

    @SerializedName("bill_info5")
    var billInfo5: String? = null,

    @SerializedName("bill_info6")
    var billInfo6: String? = null,

    @SerializedName("bill_info7")
    var billInfo7: String? = null,

    @SerializedName("bill_info8")
    var billInfo8: String? = null

)