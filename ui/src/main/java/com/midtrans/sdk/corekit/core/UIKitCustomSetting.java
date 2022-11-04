package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.uikit.external.UiKitApi;

/**
 * Created by ziahaqi on 11/4/16.
 */

public class UIKitCustomSetting {

    public void setSaveCardChecked(boolean saveCardChecked) {
        UiKitApi.Companion.getDefaultInstance().getUiKitSetting().setSaveCardChecked(saveCardChecked);
    }
}
