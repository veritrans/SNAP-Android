package com.midtrans.sdk.uikit.internal.view

import androidx.compose.ui.graphics.Color
import com.midtrans.sdk.uikit.external.UiKitApi

internal object SnapColors {
    private const val BACKGROUND_BORDER_SOLID_SECONDARY = 0XE6E9EF
    private const val BACKGROUND_BORDER_SOLID_PRIMARY = 0XDBDFE7
    private const val BACKGROUND_FILL_PRIMARY = 0XFFFFFF
    private const val BACKGROUND_FILL_SECONDARY = 0XEFF2F6
    private const val BACKGROUND_FILL_HIGHLIGHT = 0XE8EBEF
    private const val BACKGROUND_FILL_BRAND = 0X3A5EAB
    private const val BACKGROUND_FILL_INVERSE = 0X2044
    private const val BACKGROUND_FILL_LIGHT = 0XFFFBFE

    private const val TEXT_PRIMARY = 0X2D2E34
    private const val TEXT_SECONDARY = 0X4F515C
    private const val TEXT_MUTED = 0X8E919B
    private const val TEXT_INVERSE = 0XFFFFFF
    private const val TEXT_BRAND = 0X07ADDC
    private const val TEXT_DISABLED = 0X9A9EAA
    private const val ICON_PRIMARY = 0X2D2E34
    private const val ICON_INVERSE = 0XFFFFFF
    private const val ICON_MUTED = 0X8E919B
    private const val ICON_BRAND = 0X7ADDC
    private const val ICON_DISABLED = 0X9A9EAA

    private const val LINK = 0X1F44E5
    private const val LINK_HOVER = 0X3E62FE
    private const val LINK_ACTIVE = 0X1F44E5
    private const val LINK_VISITED = 0XC643C1

    private const val INTERACTIVE_FILL_BRAND_DEFAULT = 0X00AA13
    private const val INTERACTIVE_FILL_BRAND_HOVER = 0X41CFF8
    private const val INTERACTIVE_FILL_BRAND_ACTIVE = 0X054FBF

    private const val INTERACTIVE_FILL_DEFAULT = 0X5070FD
    private const val INTERACTIVE_FILL_HOVER = 0X85A2F9
    private const val INTERACTIVE_FILL_ACTIVE = 0X1C3ABB
    private const val INTERACTIVE_BORDER_INPUT = 0XDCDFE6
    private const val INTERACTIVE_BORDER_DOTTED = 0XDCDFE6
    private const val INTERACTIVE_DISABLED = 0XF1F3F6
    private const val INTERACTIVE_FOCUS = 0X054FBF
    private const val INTERACTIVE_BORDER_SUPPORT = 0XF27E89
    private const val INTERACTIVE_BORDER_ACTION = 0X4F515C
    private const val INTERACTIVE_FILL_INVERSE = 0X383942
    private const val SUPPORT_DANGER_DEFAULT = 0XF3536B
    private const val SUPPORT_DANGER_HOVER = 0XF3637A
    private const val SUPPORT_DANGER_ACTIVE = 0XC32E52
    private const val SUPPORT_DANGER_FILL = 0XFEE1E7
    private const val SUPPORT_WARNING_DEFAULT = 0XFFCE45
    private const val SUPPORT_WARNING_HOVER = 0XFFDD80
    private const val SUPPORT_WARNING_ACTIVE = 0XD8991F
    private const val SUPPORT_WARNING_FILL = 0XFFF5E3
    private const val SUPPORT_SUCCESS_DEFAULT = 0X00D5B5
    private const val SUPPORT_SUCCESS_HOVER = 0X84E1CC
    private const val SUPPORT_SUCCESS_ACTIVE = 0X87973
    private const val SUPPORT_SUCCESS_FILL = 0XE8FBF7
    private const val SUPPORT_INFO_DEFAULT = 0X6FD6
    private const val SUPPORT_INFO_HOVER = 0X85A2F9
    private const val SUPPORT_INFO_ACTIVE = 0X1C3ABB
    private const val SUPPORT_INFO_FILL = 0XE3EDFF
    private const val SUPPORT_NEUTRAL_FILL = 0XEFF2F6

    private const val TRANSPARENT = 0

    private const val OVERLAY_BLACK = 0X32333A
    private const val OVERLAY_WHITE = 0XFFFFFF

    private const val LINE_LIGHT_MUTED = 0XE8E8E8

    val backgroundBorderSolidSecondary
        get() = UiKitApi.getDefaultInstance().customColors?.backgroundBorderSolidSecondary
            ?: BACKGROUND_BORDER_SOLID_SECONDARY
    val backgroundBorderSolidPrimary
        get() = UiKitApi.getDefaultInstance().customColors?.backgroundBorderSolidPrimary
            ?: BACKGROUND_BORDER_SOLID_PRIMARY
    val backgroundFillPrimary
        get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillPrimary ?: BACKGROUND_FILL_PRIMARY
    val backgroundFillSecondary get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillSecondary ?: BACKGROUND_FILL_SECONDARY
    val backgroundFillHighlight get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillHighlight ?: BACKGROUND_FILL_HIGHLIGHT
    val backgroundFillBrand get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillBrand ?: BACKGROUND_FILL_BRAND
    val backgroundFillInverse get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillInverse ?: BACKGROUND_FILL_INVERSE
    val backgroundFillLight
        get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillLight ?: BACKGROUND_FILL_LIGHT

    val textPrimary get() = UiKitApi.getDefaultInstance().customColors?.textPrimary ?: TEXT_PRIMARY
    val textSecondary get() = UiKitApi.getDefaultInstance().customColors?.textSecondary ?: TEXT_SECONDARY
    val textMuted get() = UiKitApi.getDefaultInstance().customColors?.textMuted ?: TEXT_MUTED
    val textInverse get() = UiKitApi.getDefaultInstance().customColors?.textInverse ?: TEXT_INVERSE
    val textBrand get() = UiKitApi.getDefaultInstance().customColors?.textBrand ?: TEXT_BRAND
    val textDisabled get() = UiKitApi.getDefaultInstance().customColors?.textDisabled ?: TEXT_DISABLED
    val iconPrimary get() = UiKitApi.getDefaultInstance().customColors?.iconPrimary ?: ICON_PRIMARY
    val iconInverse get() = UiKitApi.getDefaultInstance().customColors?.iconInverse ?: ICON_INVERSE
    val iconMuted get() = UiKitApi.getDefaultInstance().customColors?.iconMuted ?: ICON_MUTED
    val iconBrand get() = UiKitApi.getDefaultInstance().customColors?.iconBrand ?: ICON_BRAND
    val iconDisabled get() = UiKitApi.getDefaultInstance().customColors?.iconDisabled ?: ICON_DISABLED

    val link get() = UiKitApi.getDefaultInstance().customColors?.link ?: LINK
    val linkHover get() = UiKitApi.getDefaultInstance().customColors?.linkHover ?: LINK_HOVER
    val linkActive get() = UiKitApi.getDefaultInstance().customColors?.linkActive ?: LINK_ACTIVE
    val linkVisited get() = UiKitApi.getDefaultInstance().customColors?.linkVisited ?: LINK_VISITED

    val interactiveFillBrandDefault
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillBrandDefault ?: INTERACTIVE_FILL_BRAND_DEFAULT
    val interactiveFillBrandHover get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillBrandHover ?: INTERACTIVE_FILL_BRAND_HOVER
    val interactiveFillBrandActive get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillBrandActive ?: INTERACTIVE_FILL_BRAND_ACTIVE

    val interactiveFillDefault get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillDefault ?: INTERACTIVE_FILL_DEFAULT
    val interactiveFillHover get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillHover ?: INTERACTIVE_FILL_HOVER
    val interactiveFillActive get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillActive ?: INTERACTIVE_FILL_ACTIVE
    val interactiveBorderInput
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderInput ?: INTERACTIVE_BORDER_INPUT
    val interactiveBorderDotted get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderDotted ?: INTERACTIVE_BORDER_DOTTED
    val interactiveDisabled get() = UiKitApi.getDefaultInstance().customColors?.interactiveDisabled ?: INTERACTIVE_DISABLED
    val interactiveFocus get() = UiKitApi.getDefaultInstance().customColors?.interactiveFocus ?: INTERACTIVE_FOCUS
    val interactiveBorderSupport
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderSupport ?: INTERACTIVE_BORDER_SUPPORT
    val interactiveBorderAction
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderAction ?: INTERACTIVE_BORDER_ACTION
    val interactiveFillInverse
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillInverse ?: INTERACTIVE_FILL_INVERSE
    val supportDangerDefault
        get() = UiKitApi.getDefaultInstance().customColors?.supportDangerDefault ?: SUPPORT_DANGER_DEFAULT
    val supportDangerHover get() = UiKitApi.getDefaultInstance().customColors?.supportDangerHover ?: SUPPORT_DANGER_HOVER
    val supportDangerActive get() = UiKitApi.getDefaultInstance().customColors?.supportDangerActive ?: SUPPORT_DANGER_ACTIVE
    val supportDangerFill get() = UiKitApi.getDefaultInstance().customColors?.supportDangerFill ?: SUPPORT_DANGER_FILL
    val supportWarningDefault get() = UiKitApi.getDefaultInstance().customColors?.supportWarningDefault ?: SUPPORT_WARNING_DEFAULT
    val supportWarningHover get() = UiKitApi.getDefaultInstance().customColors?.supportWarningHover ?: SUPPORT_WARNING_HOVER
    val supportWarningActive get() = UiKitApi.getDefaultInstance().customColors?.supportWarningActive ?: SUPPORT_WARNING_ACTIVE
    val supportWarningFill get() = UiKitApi.getDefaultInstance().customColors?.supportWarningFill ?: SUPPORT_WARNING_FILL
    val supportSuccessDefault
        get() = UiKitApi.getDefaultInstance().customColors?.supportSuccessDefault ?: SUPPORT_SUCCESS_DEFAULT
    val supportSuccessHover get() = UiKitApi.getDefaultInstance().customColors?.supportSuccessHover ?: SUPPORT_SUCCESS_HOVER
    val supportSuccessActive get() = UiKitApi.getDefaultInstance().customColors?.supportSuccessActive ?: SUPPORT_SUCCESS_ACTIVE
    val supportSuccessFill get() = UiKitApi.getDefaultInstance().customColors?.supportSuccessFill ?: SUPPORT_DANGER_FILL
    val supportInfoDefault get() = UiKitApi.getDefaultInstance().customColors?.supportInfoDefault ?: SUPPORT_INFO_DEFAULT
    val supportInfoHover get() = UiKitApi.getDefaultInstance().customColors?.supportInfoHover ?: SUPPORT_INFO_HOVER
    val supportInfoActive get() = UiKitApi.getDefaultInstance().customColors?.supportInfoActive ?: SUPPORT_INFO_ACTIVE
    val supportInfoFill get() = UiKitApi.getDefaultInstance().customColors?.supportInfoFill ?: SUPPORT_INFO_FILL
    val supportNeutralFill get() = UiKitApi.getDefaultInstance().customColors?.supportNeutralFill ?: SUPPORT_NEUTRAL_FILL

    val transparent get() = UiKitApi.getDefaultInstance().customColors?.transparent ?: TRANSPARENT

    val overlayBlack get() = UiKitApi.getDefaultInstance().customColors?.overlayBlack ?: OVERLAY_BLACK
    val overlayWhite get() = UiKitApi.getDefaultInstance().customColors?.overlayWhite ?: OVERLAY_WHITE

    val lineLightMuted get() = UiKitApi.getDefaultInstance().customColors?.lineLightMuted ?: LINE_LIGHT_MUTED

    fun getARGBColor(color: Int): Color {
        return Color((color.toLong() + (0xFF000000).toLong()))
    }
}