package com.gaoyun.smscodelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gaoyun.smscodelayout.catcher.OnSmsCatchListener
import com.gaoyun.smscodelayout.catcher.SmsVerifyCatcher
import com.gaoyun.smscodelayout.view.SmsCodeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val smsVerifyCatcher = SmsVerifyCatcher(this, object: OnSmsCatchListener<String> {
        override fun onCatch(message: String) {
            if(message.isNotEmpty()) {
                smsCodeView.setCode(message)
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smsCodeView.setActionText("Action")
        smsCodeView.setTitleText("Title")

        smsCodeView.setTitleTextSize(24f)
        smsCodeView.setActionTextSize(16f)

        smsCodeView.setTitleTextColor(resources.getColor(R.color.colorPrimary))
        smsCodeView.setActionTextColor(resources.getColor(R.color.colorAccent))

        smsCodeView.setTitleTextStyle(SmsCodeView.BOLD_STYLE)
        smsCodeView.setActionTextStyle(SmsCodeView.BOLD_ITALIC_STYLE)

        smsCodeView.getCode() //returns string like "1234"
        smsCodeView.setCode("")

        //if true then when user type last number keyboard will hide
        smsCodeView.hideSoftKeyboardOnLastNumberInput(hide = true, activity = this)

        //action when user click "action" button on the bottom of view
        smsCodeView.setOnActionClickListener(View.OnClickListener {
            //do something
        })

        //action when user click done button on system keyboard
        smsCodeView.setOnActionDoneClickListener {
            //do something
        }

        smsVerifyCatcher.setPhoneNumberFilter("SMS NUMBER")
        smsVerifyCatcher.setFilter("<regexp>")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        smsVerifyCatcher.onStart()
    }

    override fun onStop() {
        super.onStop()
        smsVerifyCatcher.onStop()
    }
}
