package com.givol.dialogs

import android.os.Bundle
import android.view.View

class GivolDialog(
    var title: String? = null,
    var subtitle: String? = null,
    var iconDrawable: Int = NO_ICON,
    var positiveButtonText: Int = NO_TEXT,
    var negativeButtonText: Int = NO_TEXT,
    var subActionText: Int = NO_TEXT,
    override val callback: Callback = innerCallback) : AbstractDialog(callback), AbstractDialog.Callback {

    init {
        val bundle = Bundle()
        bundle.putString(TITLE_ARGUMENT, title)
        bundle.putString(SUBTITLE_ARGUMENT, subtitle)
        bundle.putInt(POSITIVE_BUTTON_TEXT_RES, positiveButtonText)
        bundle.putInt(NEGATIVE_BUTTON_TEXT_RES, negativeButtonText)
        bundle.putInt(ICON_ID_RES, iconDrawable)
        bundle.putInt(SUB_ACTION_ARGUMENT, subActionText)

        arguments = bundle
    }

    override fun getIconRes(): Int {
        return NO_ICON
    }

    override fun getTitleRes(): Int {
        return NO_TEXT
    }

    override fun getTitleStr(): String {
        return NO_TEXT_STR
    }

    override fun getSubTitleRes(): Int {
        return NO_TEXT
    }

    override fun getPositiveRes(): Int {
        return NO_TEXT
    }

    override fun getNegativeRes(): Int {
        return NO_TEXT
    }

    override fun getSubActionRes(): Int {
        return NO_TEXT
    }

    override fun getSubAction(): View.OnClickListener {
        return View.OnClickListener { dismiss() }
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(false)
    }

    override fun onDialogPositiveAction(requestCode: Int) {
        // STUB!
    }

    override fun onDialogNegativeAction(requestCode: Int) {
        // STUB!
    }

    fun setSubTitle(subtitle: String) {

        arguments?.putString(SUBTITLE_ARGUMENT, subtitle)
    }

    companion object {
        const val TAG = "GivolDialog"
    }
}