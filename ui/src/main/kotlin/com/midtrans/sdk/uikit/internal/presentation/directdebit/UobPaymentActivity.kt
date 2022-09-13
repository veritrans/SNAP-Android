package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*

class UobPaymentActivity : BaseActivity() {

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val uobMode: String by lazy {
        intent.getStringExtra(EXTRA_UOB_MODE)
            ?: throw RuntimeException("Uob mode must not be empty")
    }

    private val amount: String by lazy {
        intent.getStringExtra(EXTRA_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerInfo: CustomerInfo? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_INFO) as? CustomerInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerUiKitComponent.builder()
            .applicationContext(this.applicationContext)
            .build()
            .inject(this)
    }

    @Composable
    private fun UobPaymentContent(
        uobMode: String,
        amount: String,
        orderId: String,
        customerInfo: CustomerInfo?,
        response: TransactionResponse?
    ) {
        var isCustomerDetailExpanded by remember { mutableStateOf(false) }
        var isInstructionExpanded by remember { mutableStateOf(false) }
        val title = stringResource(getTitleId(uobMode = uobMode))
        val url = getUobDeeplinkUrl(uobMode, response)

        if (url.isEmpty()) {
            Column(
                modifier = Modifier
                    .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
                    .fillMaxHeight(1f)
            ) {
                SnapAppBar(
                    title = title,
                    iconResId = R.drawable.ic_arrow_left
                ) {
                    onBackPressed()
                }
                SnapOverlayExpandingBox(
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 16.dp),
                    isExpanded = isCustomerDetailExpanded,
                    mainContent = {
                        SnapTotal(
                            amount = amount,
                            orderId = orderId,
                            canExpand = customerInfo != null,
                            remainingTime = null
                        ) {
                            isCustomerDetailExpanded = it
                        }
                    },
                    expandingContent = customerInfo?.let {
                        {
                            SnapCustomerDetail(
                                name = customerInfo.name,
                                phone = customerInfo.phone,
                                addressLines = customerInfo.addressLines
                            )
                        }
                    },
                    followingContent = {
                        Column(
                            modifier = Modifier
                                .verticalScroll(state = rememberScrollState())
                                .padding(top = 16.dp)
                                .fillMaxWidth()

                        ) {
                            SnapText(stringResource(getInstructionId(uobMode)))
                            SnapInstructionButton(
                                modifier = Modifier.padding(top = 28.dp),
                                isExpanded = isInstructionExpanded,
                                iconResId = R.drawable.ic_help,
                                title = stringResource(R.string.bca_klik_pay_how_to_pay_title),
                                onExpandClick = { isInstructionExpanded = !isInstructionExpanded },
                                expandingContent = {
                                    Column {
                                        AnimatedVisibility(visible = isInstructionExpanded) {
                                            SnapNumberedList(
                                                list = stringArrayResource(getHowToPayId(uobMode)).toList()
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
                SnapButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    enabled = true,
                    text = stringResource(id = getUobCta(uobMode)),
                    style = SnapButton.Style.PRIMARY
                ) {
                    //TODO viewmodel here
//                    viewModel.payDirectDebit(
//                        snapToken = snapToken,
//                        paymentType = paymentType,
//                        userId = userId
//                    )
                }
            }
        } else {
            //todo open deeplink
        }
    }

    private fun getUobDeeplinkUrl(
        uobMode: String,
        response: TransactionResponse?
    ): String {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> response?.uobEzpayWebUrl.orEmpty()
            PaymentType.UOB_EZPAY_APP -> response?.uobEzpayDeeplinkUrl.orEmpty()
            else -> ""
        }
    }

    @Composable
    private fun getTitleId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_method_name
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_method_name
            else -> 0
        }
    }

    @Composable
    private fun getInstructionId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_instruction
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_instruction
            else -> 0
        }
    }

    @Composable
    private fun getHowToPayId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.array.uob_web_how_to_pay
            PaymentType.UOB_EZPAY_APP -> R.array.uob_tmrw_how_to_pay
            else -> 0
        }
    }

    @Composable
    private fun getUobCta(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_cta
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_cta
            else -> 0
        }
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "directDebit.uobPayment.extra.snap_token"
        private const val EXTRA_UOB_MODE = "directDebit.uobPayment.extra.uob_mode"
        private const val EXTRA_AMOUNT = "directDebit.uobPayment.extra.amount"
        private const val EXTRA_ORDER_ID = "directDebit.uobPayment.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "directDebit.uobPayment.extra.customer_info"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            uobMode: String = "",
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?
        ): Intent {
            return Intent(activityContext, UobPaymentActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_UOB_MODE, uobMode)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
            }
        }
    }
}