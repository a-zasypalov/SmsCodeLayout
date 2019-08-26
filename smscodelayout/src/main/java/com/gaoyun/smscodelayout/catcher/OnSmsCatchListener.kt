package com.gaoyun.smscodelayout.catcher

interface OnSmsCatchListener<T> {
    fun onCatch(message: String)
}