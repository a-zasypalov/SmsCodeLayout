package com.gaoyun.smscodelayout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gaoyun.smscodelayout.catcher.SmsCatcher
import com.gaoyun.smscodelayout.interfaces.SmsCodeCompleteWatcher
import com.gaoyun.smscodelayout.interfaces.SmsCodeLengthWatcher
import com.gaoyun.smscodelayout.interfaces.SmsCodeTimeEmitter
import com.gaoyun.smscodelayout.view.SmsCodeView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val smsRequestCode = 243 //or any other free request code
    private val smsCatcher = SmsCatcher(this, smsRequestCode, "YOUR PHONE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smsCodeView.setActionText(resources.getString(R.string.resend_sms))
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

        smsCodeView.addCodeCompleteWatcher(object:
            SmsCodeCompleteWatcher {
            override fun codeCompleteChanged(complete: Boolean) {
                if(complete) {
                    Toast.makeText(this@MainActivity, "Code has 4 numbers", Toast.LENGTH_SHORT).show()
                }
            }
        })

        smsCodeView.addCodeLengthWatcher(object:
            SmsCodeLengthWatcher {
            override fun codeLengthChanged(length: Int) {
                Toast.makeText(this@MainActivity, "Code length is $length", Toast.LENGTH_SHORT).show()
            }
        })

        smsCodeView.setOnActionClickListener(View.OnClickListener {
            smsCodeView.addTimerToRepeatAction(70, TimeUnit.SECONDS, object: SmsCodeTimeEmitter{
                override fun onTick(min: Int, sec: Int) {
                    smsCodeView.setActionText(resources.getString(R.string.resend_sms_timer, min, sec))
                }

                override fun onTimerStop() {
                    smsCodeView.setActionText(resources.getString(R.string.resend_sms))
                }
            })
        })

        smsCatcher.startCatchSms()

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            smsRequestCode ->
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        val message = smsCatcher.getSmsMessage(data)
                        smsCodeView.setCode(message)
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        smsCodeView.clearTimerToRepeatAction()
    }
}
