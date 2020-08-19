package com.givol.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.givol.R
import kotlinx.android.synthetic.main.givol_dialog.*
import timber.log.Timber

abstract class AbstractDialog(open val callback: Callback) : DialogFragment() {

    //    lateinit var callback: Callback
    private var shown = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.givol_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            adjustTitle(it)
            adjustSubtitle(it)
            adjustNegativeAction(it)
            adjustPositiveAction(it)
            adjustDialogIcon(it)
            adjustSubAction(it)
        }

        dialogPositive.setOnClickListener { onClickPositive() }
        dialogNegative.setOnClickListener { onClickNegative() }
        dialogSubAction.setOnClickListener { onClickSubAction() }
    }

    private fun onClickPositive() {
        dismiss()
        callback.onDialogPositiveAction(targetRequestCode)
    }

    private fun onClickNegative() {
        dismiss()
        callback.onDialogNegativeAction(targetRequestCode)
    }

    private fun onClickSubAction() {
        dismiss()
        callback.onDialogPositiveAction(targetRequestCode)
    }

    private fun adjustTitle(args: Bundle) {
        if (args.containsKey(TITLE_ARGUMENT)) {
            dialogTitle.text = args.getString(TITLE_ARGUMENT)
        } else {
            when {
                getTitleRes() != NO_TEXT -> dialogTitle.setText(getTitleRes())
                getTitleStr() != NO_TEXT_STR -> dialogTitle.text = getTitleStr()
                else -> dialogTitle.visibility = View.GONE
            }
        }
    }

    private fun adjustSubtitle(args: Bundle) {
        if (arguments != null && args.containsKey(SUBTITLE_ARGUMENT)) {
            dialogSubtitle.text = args.getString(SUBTITLE_ARGUMENT)
        } else {
            if (getSubTitleRes() != NO_TEXT) {
                dialogSubtitle.setText(getSubTitleRes())
            } else {
                dialogSubtitle.visibility = View.GONE
            }
        }
    }

    private fun adjustNegativeAction(args: Bundle) {
        if (args.containsKey(NEGATIVE_BUTTON_TEXT_RES)) {
            val negativeRes = args.getInt(NEGATIVE_BUTTON_TEXT_RES)
            if (negativeRes != NO_TEXT) {
                dialogNegative.text = getString(negativeRes)
            } else {
                dialogNegative.visibility = View.GONE
                devider.visibility = View.GONE
            }
        } else {
            if (getNegativeRes() != NO_TEXT) {
                dialogNegative.setText(getNegativeRes())
            } else {
                dialogNegative.visibility = View.GONE
                devider.visibility = View.GONE
            }
        }
    }

    private fun adjustPositiveAction(args: Bundle) {
        if (args.containsKey(POSITIVE_BUTTON_TEXT_RES)) {
            val positiveRes = args.getInt(POSITIVE_BUTTON_TEXT_RES)
            if (positiveRes != NO_TEXT) {
                dialogPositive.text = getString(positiveRes)
            } else {
                dialogPositive.visibility = View.GONE
                devider.visibility = View.GONE
            }
        } else {
            if (getPositiveRes() != NO_TEXT) {
                dialogPositive.setText(getPositiveRes())
            } else {
                dialogPositive.visibility = View.GONE
                devider.visibility = View.GONE
            }
        }
    }

    private fun adjustDialogIcon(args: Bundle) {
        if (arguments != null && args.containsKey(ICON_ID_RES)) {
            val iconRes = args.getInt(ICON_ID_RES)
            if (iconRes != NO_ICON) {
                dialogIcon.setImageResource(iconRes)
            } else {
                dialogIcon.visibility = View.GONE
            }
        } else {
            if (getIconRes() == NO_ICON) {
                dialogIcon.visibility = View.GONE
            } else {
                dialogIcon.setImageResource(getIconRes())
            }
        }
    }

    private fun adjustSubAction(args: Bundle) {
        if (arguments != null && args.containsKey(SUB_ACTION_ARGUMENT)) {
            val subActionTextRes = args.getInt(SUB_ACTION_ARGUMENT)
            if (subActionTextRes != NO_TEXT) {
                dialogSubAction.text = getString(subActionTextRes)
                dialogSubAction.setOnClickListener(getSubAction())
            } else {
                dialogSubAction.visibility = View.GONE
            }
        } else {
            if (getSubActionRes() != NO_TEXT) {
                dialogSubAction.setText(getSubActionRes())
                dialogSubAction.setOnClickListener(getSubAction())
            } else {
                dialogSubAction.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val windowParams = it.attributes
            windowParams.dimAmount = DIM_AMOUNT
            it.attributes = windowParams
        }
    }

    protected abstract fun getIconRes(): Int

    protected abstract fun getTitleRes(): Int

    protected abstract fun getTitleStr(): String

    protected abstract fun getSubTitleRes(): Int

    protected abstract fun getPositiveRes(): Int

    protected abstract fun getNegativeRes(): Int

    protected abstract fun getSubActionRes(): Int

    protected abstract fun getSubAction(): View.OnClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!shown) {
            shown = true
            super.show(manager, tag)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        shown = false
    }

    companion object {
        private val DIM_AMOUNT = 0.75f
        const val NO_TEXT = -1
        const val NO_TEXT_STR = ""
        const val NO_ICON = -1

        const val TITLE_ARGUMENT = "title"
        const val SUBTITLE_ARGUMENT = "subtitle"
        const val SUB_ACTION_ARGUMENT = "sub_action"
        const val POSITIVE_BUTTON_TEXT_RES = "positiveButtonTextRes"
        const val NEGATIVE_BUTTON_TEXT_RES = "negativeButtonTextRes"
        const val ICON_ID_RES = "iconId"

        val innerCallback = object : Callback {
            override fun onDialogPositiveAction(requestCode: Int) {
                Timber.i("Mark - onDialogPositiveAction")
            }

            override fun onDialogNegativeAction(requestCode: Int) {
                Timber.i("Mark - onDionDialogNegativeActionalogPositiveAction")
            }

        }
    }

    interface Callback {
        fun onDialogPositiveAction(requestCode: Int)

        fun onDialogNegativeAction(requestCode: Int)
    }

}