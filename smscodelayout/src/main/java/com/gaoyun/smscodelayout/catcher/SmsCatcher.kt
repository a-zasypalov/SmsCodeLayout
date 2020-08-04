package com.gaoyun.smscodelayout.catcher

import android.app.Activity
import android.content.*
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsCatcher(
    private val activity: Activity,
    private val requestCode: Int,
    private val phone: String?
) {

    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            activity.startActivityForResult(consentIntent, requestCode)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        Log.e("SmsCatcher", "Sms catch timeout")
                    }
                }
            }
        }
    }

    fun startCatchSms() {
        SmsRetriever.getClient(activity).startSmsUserConsent(phone)
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity.registerReceiver(smsVerificationReceiver, intentFilter)
    }

    fun getSmsMessage(data: Intent?): String {
        return data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE) ?: ""
    }

    fun unbindCatcher() {
        activity.unregisterReceiver(smsVerificationReceiver)
    }

}