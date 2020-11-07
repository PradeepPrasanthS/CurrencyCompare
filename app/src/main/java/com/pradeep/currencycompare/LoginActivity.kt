package com.pradeep.currencycompare

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)

        findViewById<View>(R.id.sign_in_button).setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        for (i in 0 until signInButton.childCount) {
            val v: View = signInButton.getChildAt(i)
            if (v is TextView) {
                v.textSize = 16f
                v.setTypeface(null, Typeface.NORMAL)
                v.text = getString(R.string.common_signin_button_text_long)
                v.isSingleLine = true
                v.setPadding(15, 15, 15, 15)
                return
            }
        }

    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            updateUI(account)
        } catch (e: ApiException) {
            Log.w(
                TAG,
                "signInResult:failed code=" + e.statusCode
            )
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            val intent = Intent(this, AddBaseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}