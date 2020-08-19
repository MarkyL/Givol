package com.givol.widgets

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.givol.R

class GivolTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        setTextAppearance(R.style.GivolTextViewStyle)
    }

    fun setAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
    }

    fun disabeAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE
        )
    }
}
