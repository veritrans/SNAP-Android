package com.midtrans.sdk.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.SnapCore


class SampleActivity: AppCompatActivity() {

    private val viewModel: SampleViewModel by lazy {
        ViewModelProvider(this).get(SampleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SnapCore.Builder().withContext(this.applicationContext).build()
        observeLiveData()
        viewModel.getHelloFromSnap()
    }

    private fun observeLiveData(){
        viewModel.helloLiveData.observe(
            this, Observer {
                setContent{
                    Text(text = it)
                }
            }
        )
    }

}