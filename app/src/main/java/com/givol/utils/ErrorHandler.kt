package com.givol.utils

import androidx.fragment.app.Fragment
import com.givol.R
import com.givol.dialogs.GivolDialog
import com.givol.navigation.ActivityNavigator
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import org.koin.core.KoinComponent
import timber.log.Timber


class ErrorHandler(val navigator: ActivityNavigator) : KoinComponent {

    fun handleError(fragment: Fragment, throwable: Throwable) {
        Timber.i("handleError - $throwable")
        showErrorDialog(fragment)
    }

    fun handleError(
        fragment: Fragment,
        title: String = fragment.resources.getString(R.string.error_general),
        subtitle: String = fragment.resources.getString(R.string.error_general_subtitle),
        positiveBtnText: Int = R.string.dialog_positive_ok) {

        GivolDialog(
            title = title, subtitle = subtitle,
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = positiveBtnText
        ).show(fragment.parentFragmentManager, GivolDialog.TAG)

    }

    private fun showErrorDialog(
        fragment: Fragment,
        errorMessage: String = StringUtils.EMPTY_STRING
    ) {
        val dialog = GivolDialog(
            title = fragment.resources.getString(R.string.error_general),
            subtitle = if (errorMessage.isEmpty()) {
                fragment.resources.getString(R.string.error_general_subtitle)
            } else {
                errorMessage
            },
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = R.string.dialog_positive_ok
        )

        dialog.show(fragment.parentFragmentManager, GivolDialog.TAG)
    }

    fun handleFBError(fragment: Fragment, task: Task<AuthResult>) {
        val dialog = GivolDialog(title = fragment.resources.getString(R.string.error_fb_general),
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = DEFAULT_POSITIVE)

        try {
            throw task.exception!!
        } catch (e: FirebaseAuthWeakPasswordException) {
            dialog.setSubTitle(fragment.resources.getString(R.string.error_fb_weak_password))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            dialog.setSubTitle(fragment.resources.getString(R.string.error_fb_invalid_credentials))
        } catch (e: FirebaseAuthInvalidUserException) {
            dialog.setSubTitle(fragment.resources.getString(R.string.error_fb_invalid_user))
        } catch (e: FirebaseAuthUserCollisionException) {
            dialog.setSubTitle(fragment.resources.getString(R.string.error_fb_user_collision))
        } catch (e: Exception) {
            dialog.setSubTitle(fragment.resources.getString(R.string.error_fb_general_sub))
        }

        dialog.show(fragment.parentFragmentManager, GivolDialog.TAG)
    }

    companion object {
        private const val DEFAULT_TITLE = R.string.error_general
        private const val DEFAULT_SUB_TITLE = R.string.error_general_subtitle
        private const val DEFAULT_POSITIVE = R.string.dialog_positive_ok
    }
}