# SmsCodeLayout
Simple and customizable view for input a 4-digit code from SMS

<img src="https://github.com/gaoyundexinmen/SmsCodeLayout/raw/master/screenshot.png">

Download
--------
[ ![Download](https://api.bintray.com/packages/gaoyundexinmen/SmsCodeLayout/SmsCodeLayout/images/download.svg) ](https://bintray.com/gaoyundexinmen/SmsCodeLayout/SmsCodeLayout/_latestVersion)

Grab via Gradle:
```groovy
implementation 'com.gaoyun.smscodelayout:smscodelayout:0.5.0'
```

Basic usage
--------
Just include SmsCodeView in your layout:
```xml
<com.gaoyun.smscodelayout.SmsCodeView
        android:id="@+id/smsCodeView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:actionText="Action text"
        app:actionTextColor="@color/colorAccent"
        app:actionTextSize="16sp"
        app:buttonStyle="@style/AppTheme"
        app:numberBackground="@drawable/rounded_card_grey"
        app:smsActionTextStyle="italic"
        app:smsTitleTextStyle="bold"
        app:titleText="Title"
        app:titleTextColor="@color/colorBlue"
        app:titleTextSize="24sp" />
```

You can customize attributes in Activity/Fragment/etc too:
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //...

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
    }
}
```

You can add SmsCodeWatchers to observe code events:
```kotlin
smsCodeView.addCodeCompleteWatcher(object: SmsCodeCompleteWatcher{
    override fun codeCompleteChanged(complete: Boolean) {
        if(complete) {
            Toast.makeText(this@MainActivity, "Code has 4 numbers", Toast.LENGTH_SHORT).show()
        }
    }
})

smsCodeView.addCodeLengthWatcher(object: SmsCodeLengthWatcher{
    override fun codeLengthChanged(length: Int) {
        Toast.makeText(this@MainActivity, "Code length is $length", Toast.LENGTH_SHORT).show()
    }
})
```
Resend Sms timer
---------
You can add timer before user will can call resend sms (or any other action).
```kotlin
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
```
!IMPORTANT
Dont forget clear timer in onDestroy() method or before transition to the next screen:
```kotlin
override fun onDestroy() {
    super.onDestroy()
    smsCodeView.clearTimerToRepeatAction()
}
```
Result:
<img src="https://github.com/gaoyundexinmen/SmsCodeLayout/raw/master/screenshot1.jpg">

SmsCatcher
-----------
Init SmsCatcher if you want activate SMS catching:
```kotlin
val smsRequestCode = 243 //or any other free request code
val smsCatcher = SmsCatcher(this, smsRequestCode, "YOUR PHONE")
```

And start SMS catching:
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //..
    smsCatcher.startCatchSms()
}
```

Catch event by request code in onActivityResult callback:
```kotlin
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
```

Don't forget to unbind receiver:
```kotlin
override fun onDestroy() {
        super.onDestroy()
        smsCatcher.unbindCatcher()
        smsCodeView.clearTimerToRepeatAction()
    }
```

License
--------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
