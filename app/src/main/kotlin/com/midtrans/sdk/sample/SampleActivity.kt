package com.midtrans.sdk.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
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
        viewModel.chargeBniVa()
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

    @Composable
    @Preview
    fun Greeting() {
        Column {
            Text("Insert snap token", style = TextStyle(color = Color.Red))
            TextField(value = "", onValueChange = {
                value ->
            })
        }
    }

}