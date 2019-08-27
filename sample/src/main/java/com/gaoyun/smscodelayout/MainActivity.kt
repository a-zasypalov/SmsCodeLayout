package com.gaoyun.smscodelayout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gaoyun.smscodelayout.catcher.SmsCatcher
import com.gaoyun.smscodelayout.view.SmsCodeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val smsRequestCode = 243
    private val smsCatcher = SmsCatcher(this, smsRequestCode, "+79648453779")

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
        smsCodeView.setCode("1234")

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

        smsCatcher.startCatchSms()

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            smsRequestCode ->
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        smsCodeView.setCode(smsCatcher.getCodeFromSms(data))
                    }
                }
        }
    }
}
