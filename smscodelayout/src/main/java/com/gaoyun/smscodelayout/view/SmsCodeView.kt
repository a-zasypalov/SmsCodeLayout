package com.gaoyun.smscodelayout.view

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.gaoyun.smscodelayout.R
import kotlinx.android.synthetic.main.view_sms_code.view.*


class SmsCodeView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr)  {

    companion object{
        const val NORMAL_STYLE = 0
        const val BOLD_STYLE = 1
        const val ITALIC_STYLE = 2
        const val BOLD_ITALIC_STYLE = 3
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sms_code, this, true)

        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it,
                R.styleable.SmsCodeView, 0, 0)

            setTitleText(styledAttributes.getString(R.styleable.SmsCodeView_titleText))
            setTitleTextColor(styledAttributes.getColor(
                R.styleable.SmsCodeView_titleTextColor, ContextCompat.getColor(context,
                    R.color.textColorPrimary
                )))
            setTitleTextSize(styledAttributes.getDimension(R.styleable.SmsCodeView_titleTextSize, 14f))
            setTitleTextStyle(styledAttributes.getInt(
                R.styleable.SmsCodeView_smsTitleTextStyle,
                NORMAL_STYLE
            ))

            setActionText(styledAttributes.getString(R.styleable.SmsCodeView_actionText))
            setActionTextColor(styledAttributes.getColor(
                R.styleable.SmsCodeView_actionTextColor, ContextCompat.getColor(context,
                    R.color.colorBlue
                )))
            setActionTextSize(styledAttributes.getDimension(R.styleable.SmsCodeView_actionTextSize, 14f))
            setActionTextStyle(styledAttributes.getInt(
                R.styleable.SmsCodeView_smsActionTextStyle,
                NORMAL_STYLE
            ))

            setMechanic()

            styledAttributes.recycle()
        }
    }

    fun getCode(): String{
        return "${txtNumber1.text}${txtNumber2.text}${txtNumber3.text}${txtNumber4.text}"
    }

    fun setCode(code: String){
        when {
            code.length < 4 -> Toast.makeText(context, "Code is too short!", Toast.LENGTH_SHORT).show()
            code.length > 4 -> {
                Toast.makeText(context, "Code is too long!", Toast.LENGTH_SHORT).show()
                setCodeFromString(code)
            }
            else -> setCodeFromString(code)
        }
    }

    fun setOnActionDoneClickListener(action: () -> Unit){
        txtNumber4.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                action()
                true
            } else {
                false
            }
        }
    }

    fun setTitleText(value: String?){
        value?.let {
            lblTitle.text = value
            lblTitle.visibility = View.VISIBLE
        }
    }

    fun setTitleTextColor(@ColorInt value: Int){
        lblTitle.setTextColor(value)
    }

    fun setTitleTextSize(value: Float){
        lblTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
    }

    fun setTitleTextStyle(value: Int){
         lblTitle.typeface = when(value){
             NORMAL_STYLE -> Typeface.DEFAULT
             BOLD_STYLE -> Typeface.DEFAULT_BOLD
             ITALIC_STYLE -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
             else -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
         }
    }

    fun setActionText(value: String?){
        value?.let {
            btnAction.text = value
            btnAction.visibility = View.VISIBLE
        }
    }

    fun setActionTextColor(@ColorInt value: Int){
        btnAction.setTextColor(value)
    }

    fun setActionTextSize(value: Float){
        btnAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
    }

    fun setActionTextStyle(value: Int){
        btnAction.typeface = when(value){
            NORMAL_STYLE -> Typeface.DEFAULT
            BOLD_STYLE -> Typeface.DEFAULT_BOLD
            ITALIC_STYLE -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            else -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
        }
    }

    fun setOnActionClickListener(onClickListener: OnClickListener){
        btnAction.setOnClickListener(onClickListener)
    }

    fun hideSoftKeyboardOnLastNumberInput(hide: Boolean, activity: Activity){
        if(hide){
            txtNumber4.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    if(p0?.length!! > 0) hideSoftKeyboard(activity)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }
    }

    private fun setCodeFromString(code: String){
        txtNumber1.setText(code[0].toString())
        txtNumber2.setText(code[1].toString())
        txtNumber3.setText(code[2].toString())
        txtNumber4.setText(code[3].toString())
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val focusedView = activity.currentFocus
        if (focusedView != null) {
            val windowToken = focusedView.windowToken
            if (windowToken != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(windowToken, 0)
            }
        }
    }

    private fun setMechanic(){
        txtNumber1.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) txtNumber2.requestFocus()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        txtNumber2.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    txtNumber3.requestFocus()
                } else {
                    txtNumber1.requestFocus()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        txtNumber3.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0) {
                    txtNumber4.requestFocus()
                } else {
                    txtNumber2.requestFocus()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        txtNumber4.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! == 0) txtNumber3.requestFocus()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

    }

}