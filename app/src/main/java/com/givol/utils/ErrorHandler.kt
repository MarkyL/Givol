package com.givol.utils

import androidx.fragment.app.Fragment
import com.givol.R
import com.givol.dialogs.GivolDialog
import com.givol.navigation.ActivityNavigator
import com.google.gson.Gson
import org.koin.core.KoinComponent
import retrofit2.HttpException
import timber.log.Timber

class ErrorHandler(val navigator: ActivityNavigator) : KoinComponent {

    fun handleError(fragment: Fragment, throwable: Throwable) {
        Timber.i("handleError - $throwable")
        showErrorDialog(fragment)
    }

    private fun showErrorDialog(fragment: Fragment, errorMessage: String = StringUtils.EMPTY_STRING) {
        val dialog = GivolDialog(
            title = fragment.resources.getString(R.string.error_general),
            subtitle = if (errorMessage.isEmpty()) { fragment.resources.getString(R.string.error_general_subtitle) } else { errorMessage },
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = R.string.dialog_positive_ok
        )

        dialog.show(fragment.parentFragmentManager, GivolDialog.TAG)
    }
}