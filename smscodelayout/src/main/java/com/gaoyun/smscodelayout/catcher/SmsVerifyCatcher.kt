package com.gaoyun.smscodelayout.catcher

import android.Manifest
import android.app.Activity
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class SmsVerifyCatcher(
    private val activity: Activity,
    private val onSmsCatchListener: OnSmsCatchListener<String>
) {
    private var fragment: Fragment? = null
    private var smsReceiver: SmsBroadcastReceiver = SmsBroadcastReceiver()
    private var phoneNumber: String? = null
    private var filter: String? = null

    init {
        smsReceiver.setListener(this.onSmsCatchListener)
    }

    constructor(
        activity: Activity,
        fragment: Fragment,
        onSmsCatchListener: OnSmsCatchListener<String>
    ) : this(activity, onSmsCatchListener) {
        this.fragment = fragment
    }

    fun onStart() {
        if (isStoragePermissionGranted(activity, fragment)) {
            registerReceiver()
        }
    }

    private fun registerReceiver() {
        smsReceiver = SmsBroadcastReceiver()
        smsReceiver.setListener(onSmsCatchListener)
        phoneNumber?.let {
            smsReceiver.setPhoneNumberFilter(phoneNumber!!)
        }
        filter?.let {
            smsReceiver.setFilter(filter!!)
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        activity.registerReceiver(smsReceiver, intentFilter)
    }

    fun setPhoneNumberFilter(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun onStop() {
        try {
            activity.unregisterReceiver(smsReceiver)
        } catch (ignore: IllegalArgumentException) {
            //receiver not registered
        }

    }

    fun setFilter(regexp: String) {
        this.filter = regexp
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 1 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                registerReceiver()
            }
            else -> {
            }
        }
    }

    companion object {

        @VisibleForTesting
        internal val PERMISSION_REQUEST_CODE = 14

        fun isStoragePermissionGranted(activity: Activity, fragment: Fragment?): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.RECEIVE_SMS
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                } else {
                    if (fragment == null) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                            PERMISSION_REQUEST_CODE
                        )
                    } else {
                        fragment.requestPermissions(
                            arrayOf(
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS
                            ), PERMISSION_REQUEST_CODE
                        )
                    }
                    return false
                }
            } else {
                return true
            }
        }
    }
}