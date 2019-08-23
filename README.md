# SmsCodeLayout
Simple and customizable view for input a 4-digit code from SMS

<img src="https://github.com/gaoyundexinmen/SmsCodeLayout/raw/master/screenshot.png">

Download
--------
[ ![Download](https://api.bintray.com/packages/gaoyundexinmen/SmsCodeLayout/SmsCodeLayout/images/download.svg?version=0.1.3) ](https://bintray.com/gaoyundexinmen/SmsCodeLayout/SmsCodeLayout/0.1.3/link)

Grab via Gradle:
```groovy
implementation 'com.gaoyun.smscodelayout:smscodelayout:0.1.1'
```

SmsCodeLayout uses the Material library, so you should include it too:
```groovy
implementation 'com.google.android.material:material:1.0.0'
```

Using
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

License
--------

    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
