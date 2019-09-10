package com.gaoyun.smscodelayout.interfaces

interface SmsCodeTimeEmitter {
    fun onTick(min: Int, sec: Int)
    fun onTimerStop()
}