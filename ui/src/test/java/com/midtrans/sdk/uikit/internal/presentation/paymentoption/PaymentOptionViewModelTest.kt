package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.model.Address
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.PaymentMethod
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BANK_TRANSFER
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CREDIT_CARD
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.GOPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.GOPAY_QRIS
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.KLIK_BCA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY_QRIS
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.beans.HasPropertyWithValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

internal class PaymentOptionViewModelTest {

    private var snapCore: SnapCore = mock()

    private var eventAnalytics: EventAnalytics = mock()

    private lateinit var viewModel: PaymentOptionViewModel

    private lateinit var closeable: AutoCloseable

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)
        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        viewModel = PaymentOptionViewModel(snapCore)
    }

    @After
    fun teardown() {
        closeable.close()
    }

    @Test
    fun getCustomerInfoWhenInputNullShouldReturnNull() {
        val result = viewModel.getCustomerInfo(null)
        assertNull(result)
    }

    @Test
    fun getCustomerInfoWhenFirstNameOnlyShouldPutFirstNameAsName() {
        val result = viewModel.getCustomerInfo(
            CustomerDetails(
                firstName = "first name"
            )
        )
        assertNotNull(result)
        assertEquals("first name", result?.name)
    }

    @Test
    fun getCustomerInfoWhenLastNameOnlyShouldPutLastNameAsName() {
        val result = viewModel.getCustomerInfo(
            CustomerDetails(
                lastName = "last name"
            )
        )
        assertNotNull(result)
        assertEquals("last name", result?.name)
    }

    @Test
    fun getCustomerInfoWhenFirstNameAndLastNameShouldPutBothAsName() {
        val result = viewModel.getCustomerInfo(
            CustomerDetails(
                firstName = "first name",
                lastName = "last name"
            )
        )
        assertNotNull(result)
        assertEquals("first name last name", result?.name)
    }

    @Test
    fun getCustomerInfoWhenShippingAddressIsProvidedShouldCreateAddressLines() {
        var result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    address = "street name",
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("street name", "city name", "postal code"),
                hasSize(3)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("city name", "postal code"),
                hasSize(2)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("postal code"),
                hasSize(1)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    postalCode = ""
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines, hasSize(0)
        )
    }

    @Test
    fun getCustomerInfoWhenBillingAddressIsProvidedShouldCreateAddressLines() {
        var result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    address = "street name",
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("street name", "city name", "postal code"),
                hasSize(3)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("city name", "postal code"),
                hasSize(2)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("postal code"),
                hasSize(1)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    postalCode = ""
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines, hasSize(0)
        )
    }

    @Test
    fun getCustomerInfoWhenNoAddressIsProvidedShouldReturnEmptyList() {
        val result = viewModel.getCustomerInfo(CustomerDetails())
        assertNotNull(result)
        assertTrue(result!!.addressLines.isEmpty())
    }

    @Test
    fun getCustomerInfoWhenPhoneNumberIsProvidedShouldReturnPhoneNumber() {
        var result = viewModel.getCustomerInfo(
            CustomerDetails(
                phone = "0888887777777"
            )
        )
        assertNotNull(result)
        assertEquals("0888887777777", result!!.phone)

        result = viewModel.getCustomerInfo(CustomerDetails())
        assertNotNull(result)
        assertEquals("", result!!.phone)
    }

    @Test
    fun initiateListWhenPaymentListIsEmptyShouldReturnEmptyList() {
        val result = viewModel.initiateList(emptyList(), true)
        assertTrue(result.paymentMethods.isEmpty())
    }

    @Test
    fun initiateListWhenDeviceIsTabletShouldReturnListForTablet() {
        val result = viewModel.initiateList(providePaymentMethodList(), true).paymentMethods
        assertTrue(result.size == 5)
        assertThat(
            result,
            hasItems(
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(BANK_TRANSFER)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(5))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(KLIK_BCA)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(1))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(CREDIT_CARD)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(4))
                ),
                allOf(
                    HasPropertyWithValue("type", equalTo(SHOPEEPAY_QRIS)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(2))
                ),
                allOf(
                    HasPropertyWithValue("type", equalTo(GOPAY_QRIS)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(2))
                )
            )
        )
    }

    @Test
    fun initiateListWhenDeviceIsPhoneShouldReturnListForPhone() {
        val result = viewModel.initiateList(providePaymentMethodList(), false).paymentMethods
        assertTrue(result.size == 5)
        assertThat(
            result,
            hasItems(
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(BANK_TRANSFER)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(5))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(KLIK_BCA)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(1))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(CREDIT_CARD)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(4))
                ),
                allOf(
                    HasPropertyWithValue("type", equalTo(SHOPEEPAY)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(1))
                ),
                allOf(
                    HasPropertyWithValue("type", equalTo(GOPAY)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(1))
                )
            )
        )
    }

    @Test
    fun trackPaymentListPageClosed() {
        viewModel.trackPaymentListPageClosed()
        verify(eventAnalytics).trackSnapPageClosed(PageName.PAYMENT_LIST_PAGE)
    }

    @Test
    fun trackOrderDetailsViewed() {
        viewModel.trackOrderDetailsViewed()
        verify(eventAnalytics).trackSnapOrderDetailsViewed(PageName.PAYMENT_LIST_PAGE, null, null, null)
    }

    @Test
    fun trackPageViewed() {
        viewModel.trackPageViewed(1)
        verify(eventAnalytics).trackSnapPageViewed(
            pageName = PageName.PAYMENT_LIST_PAGE,
            stepNumber = "1",
            paymentMethodName = null,
            transactionId = null
        )
    }

    private fun providePaymentMethodList(): List<PaymentMethod> {
        return listOf(
            PaymentMethod(
                type = BANK_TRANSFER,
                channels = listOf(PaymentType.BCA_VA, PaymentType.BNI_VA, PaymentType.PERMATA_VA, PaymentType.E_CHANNEL, PaymentType.OTHER_VA)
            ),
            PaymentMethod(
                type = KLIK_BCA,
                channels = emptyList()
            ),
            PaymentMethod(
                type = CREDIT_CARD,
                channels = emptyList()
            ),
            PaymentMethod(
                type = SHOPEEPAY_QRIS,
                channels = emptyList()
            ),
            PaymentMethod(
                type = SHOPEEPAY,
                channels = emptyList()
            ),
            PaymentMethod(
                type = GOPAY,
                channels = emptyList()
            ),
            PaymentMethod(
                type = GOPAY_QRIS,
                channels = emptyList()
            )
        )
    }

    @Test
    fun getItemInfo() {
        val itemList = listOf(
            ItemDetails("id-01", 7800.00, 2, "item1"),
            ItemDetails("id-01", 2200.00, 1, "item2"),
        )
        val itemInfo = viewModel.getItemInfo(itemList)
        assertEquals(itemInfo, ItemInfo(itemList, 10000.00))

        val itemInfo2 = viewModel.getItemInfo(null)
        assertEquals(itemInfo2, null)
    }
}