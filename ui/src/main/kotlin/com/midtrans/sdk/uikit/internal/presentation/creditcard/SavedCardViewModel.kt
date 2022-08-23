package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.DeleteSavedCardResponse
import javax.inject.Inject

class SavedCardViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel()  {

    fun deleteSavedCard(snapToken: String, maskedCard: String){
        snapCore.deleteSavedCard(
            snapToken = snapToken,
            maskedCard = maskedCard,
            callback = object : Callback<DeleteSavedCardResponse> {
                override fun onSuccess(result: DeleteSavedCardResponse) {
                    Log.e("Delete Card Success", "Delete Card Success")
                }
                override fun onError(error: SnapError) {
                    Log.e("Delete Card Error", "Delete Card Error")
                }
            }
        )
    }
}