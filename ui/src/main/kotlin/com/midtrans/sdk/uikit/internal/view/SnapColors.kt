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
    val backgroundFillSecondary = 0XEFF2F6
    val backgroundFillHighlight = 0XE8EBEF
    val backgroundFillBrand = 0X3A5EAB
    val backgroundFillInverse = 0X2044
    val backgroundFillLight
        get() = UiKitApi.getDefaultInstance().customColors?.backgroundFillLight ?: BACKGROUND_FILL_LIGHT

    val textPrimary = 0X2D2E34
    val textSecondary = 0X4F515C
    val textMuted = 0X8E919B
    val textInverse get() = UiKitApi.getDefaultInstance().customColors?.textInverse ?: TEXT_INVERSE
    val textBrand = 0X07ADDC
    val textDisabled = 0X9A9EAA
    val iconPrimary = 0X2D2E34
    val iconInverse = 0XFFFFFF
    val iconMuted = 0X8E919B
    val iconBrand = 0X7ADDC
    val iconDisabled = 0X9A9EAA

    val link = 0X1F44E5
    val linkHover = 0X3E62FE
    val linkActive = 0X1F44E5
    val linkVisited = 0XC643C1

    val interactiveFillBrandDefault
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillBrandDefault ?: INTERACTIVE_FILL_BRAND_DEFAULT
    val interactiveFillBrandHover = 0X41CFF8
    val interactiveFillBrandActive = 0X054FBF

    val interactiveFillDefault = 0X5070FD
    val interactiveFillHover = 0X85A2F9
    val interactiveFillActive = 0X1C3ABB
    val interactiveBorderInput
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderInput ?: INTERACTIVE_BORDER_INPUT
    val interactiveBorderDotted = 0XDCDFE6
    val interactiveDisabled = 0XF1F3F6
    val interactiveFocus = 0X054FBF
    val interactiveBorderSupport
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderSupport ?: INTERACTIVE_BORDER_SUPPORT
    val interactiveBorderAction
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveBorderAction ?: INTERACTIVE_BORDER_ACTION
    val interactiveFillInverse
        get() = UiKitApi.getDefaultInstance().customColors?.interactiveFillInverse ?: INTERACTIVE_FILL_INVERSE
    val supportDangerDefault
        get() = UiKitApi.getDefaultInstance().customColors?.supportDangerDefault ?: SUPPORT_DANGER_DEFAULT
    val supportDangerHover = 0XF3637A
    val supportDangerActive = 0XC32E52
    val supportDangerFill get() = UiKitApi.getDefaultInstance().customColors?.supportDangerFill ?: SUPPORT_DANGER_FILL
    val supportWarningDefault = 0XFFCE45
    val supportWarningHover = 0XFFDD80
    val supportWarningActive = 0XD8991F
    val supportWarningFill = 0XFFF5E3
    val supportSuccessDefault
        get() = UiKitApi.getDefaultInstance().customColors?.supportSuccessDefault ?: SUPPORT_SUCCESS_DEFAULT
    val supportSuccessHover = 0X84E1CC
    val supportSuccessActive = 0X87973
    val supportSuccessFill get() = UiKitApi.getDefaultInstance().customColors?.supportSuccessFill ?: SUPPORT_DANGER_FILL
    val supportInfoDefault get() = UiKitApi.getDefaultInstance().customColors?.supportInfoDefault ?: SUPPORT_INFO_DEFAULT
    val supportInfoHover = 0X85A2F9
    val supportInfoActive = 0X1C3ABB
    val supportInfoFill = 0XE3EDFF
    val supportNeutralFill get() = UiKitApi.getDefaultInstance().customColors?.supportNeutralFill ?: SUPPORT_NEUTRAL_FILL

    val transparent get() = UiKitApi.getDefaultInstance().customColors?.transparent ?: TRANSPARENT

    val overlayBlack = 0X32333A
    val overlayWhite get() = UiKitApi.getDefaultInstance().customColors?.overlayWhite ?: OVERLAY_WHITE

    val lineLightMuted get() = UiKitApi.getDefaultInstance().customColors?.lineLightMuted ?: LINE_LIGHT_MUTED

    fun getARGBColor(color: Int): Color {
        return Color((color.toLong() + (0xFF000000).toLong()))
    }
}