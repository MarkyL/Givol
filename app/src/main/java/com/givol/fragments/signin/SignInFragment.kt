package com.givol.fragments.signin

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.givol.R
import com.givol.core.GivolFragment
import com.givol.model.User
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.MainScreen
import com.givol.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SignInFragment: GivolFragment() {

    lateinit var transferInfo: TransferInfo

    private val auth = FirebaseAuth.getInstance()
    private val fbUtil: FirebaseUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)
        configureScreen()

        actionBtn.setOnClickListener { onActionBtnClick() }
        noAccountBtn.setOnClickListener { onNoAccountBtnClick() }
        passwordTextInputEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                actionBtn.callOnClick()
                true
            }
            false
        }
    }

    private fun configureScreen() {
        when (transferInfo.flow) {
            TransferInfo.Flow.SignUP -> adjustScreenToSignUpFlow()
            else -> {
                // Do Nothing!
            }
        }
    }

    private fun adjustScreenToSignUpFlow() {
        actionBtn.text = resources.getString(R.string.sign_up)
        noAccountBtn.visibility = View.GONE
        transferInfo.flow = TransferInfo.Flow.SignUP
    }

    private fun onActionBtnClick() {
        Timber.i("onActionBtnClick")

        val userName = userNameTextInputEditText.text
        val password = passwordTextInputEditText.text

        if (validateUserNameAndPassword(userName, password)) {
            val email = convertUserNameToEmail(userName.toString())
            if (transferInfo.flow == TransferInfo.Flow.SignUP) {
                createUserWithEmailAndPassword(email, password.toString())
            } else if (transferInfo.flow == TransferInfo.Flow.SignIn) {
                signInWithEmailAndPassword(email, password.toString())
            }
        }
    }

    private fun convertUserNameToEmail(userName: String): String {
        return userName.plus(EMAIL_SUFFIX)
    }

    /**
     * Returns True if both email & password are valid.
     */
    private fun validateUserNameAndPassword(userName: Editable?, password: Editable?): Boolean {
        return when {
            userName.isNullOrEmpty() -> {
                userNameTextInputEditText.error =
                    resources.getString(R.string.sign_in_missing_user_name)
                false
            }
            password.isNullOrEmpty() -> {
                passwordTextInputEditText.error =
                    resources.getString(R.string.sign_in_missing_password)
                false
            }
            password.length < MIN_PASSWORD_LENGTH -> {
                passwordTextInputEditText.error =
                    resources.getString(R.string.sign_in_short_password, MIN_PASSWORD_LENGTH)
                false
            }
            else -> true
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        updateUI(it)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    task.exception?.let {
                        Timber.e("signInWithEmail:failure $it")
                    }
                    //errorHandler.handleError(fragment = this, subtitle = resources.getString(R.string.login_error_general))
                    errorHandler.handleFBError(this, task)
                    //updateUI(null)
                }
            }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        initUserDefaultData(it)
                        updateUI(it)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    task.exception?.let {
                        Timber.e("createUserWithEmail:failure - $it")
                    }
                    errorHandler.handleError(
                        fragment = this,
                        subtitle = resources.getString(R.string.signup_error_general)
                    )
                    //updateUI(null)
                }
            }
    }

    private fun initUserDefaultData(user: FirebaseUser) {
        Timber.i("initUserDefaultData with $user")
        val dbReference = fbUtil.getFirebaseUserNodeReference(user.uid)

        //Email
        dbReference.child(fbUtil.PARAM_EMAIL).setValue(user.email)
    }

    private fun onNoAccountBtnClick() {
        adjustScreenToSignUpFlow()
    }

    private fun updateUI(user: FirebaseUser) {
        // user has logged in.
        User.create(user.email, user.uid)
        val transferInfo = TransferInfo()
        transferInfo.uid = user.uid
        navigator.replace(MainScreen(transferInfo))
    }

    override fun showProgressView() {
        TODO("Not yet implemented")
    }

    override fun hideProgressView() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "SignInFragment"
        private const val EMAIL_SUFFIX = "@Givol.com"
        private const val MIN_PASSWORD_LENGTH = 6
    }
}