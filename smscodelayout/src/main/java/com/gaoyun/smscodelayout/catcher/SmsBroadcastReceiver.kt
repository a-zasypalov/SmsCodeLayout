package com.gaoyun.smscodelayout.catcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log


class SmsBroadcastReceiver : BroadcastReceiver() {

    private var listener: OnSmsCatchListener<String>? = null
    private var phoneFilter: String? = null
    private var filter: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        try {
            if (bundle != null) {
                val messages = bundle.get("pdus") as Array<*>?
                for (i in messages!!.indices) {
                    val currentMessage = getIncomingMessage(messages[i]!!, bundle)
                    val phoneNumber = currentMessage.displayOriginatingAddress

                    if (!phoneFilter.isNullOrEmpty() && phoneNumber != phoneFilter) {
                        return
                    }
                    val message = currentMessage.displayMessageBody
                    if (!filter.isNullOrEmpty() && !message.matches(filter!!.toRegex())) {
                        return
                    }

                        listener?.onCatch(message)
                }
            }

        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }

    }

    private fun getIncomingMessage(aObject: Any, bundle: Bundle): SmsMessage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            SmsMessage.createFromPdu(aObject as ByteArray, format)
        } else {
            SmsMessage.createFromPdu(aObject as ByteArray)
        }
    }

    /**
     * Set phone number filter
     *
     * @param listener SmsCatchListener
     */
    fun setListener(listener: OnSmsCatchListener<String>) {
        this.listener = listener
    }

    /**
     * Set phone number filter
     *
     * @param phoneFilter phone number
     */
    fun setPhoneNumberFilter(phoneFilter: String) {
        this.phoneFilter = phoneFilter
    }

    /**
     * set message filter with regexp
     *
     * @param regularExpression regexp
     */
    fun setFilter(regularExpression: String) {
        this.filter = regularExpression
    }

}