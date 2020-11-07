package com.pradeep.currencycompare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.util.*


class ResultsActivity : AppCompatActivity() {

    private lateinit var base: String
    private lateinit var compare: String
    private lateinit var loader: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        supportActionBar?.hide()

        val baseCurrencyText = findViewById<TextView>(R.id.base_currency_text)
        val result = findViewById<TextView>(R.id.result)
        val refresh = findViewById<TextView>(R.id.refresh)
        val editBase = findViewById<TextView>(R.id.edit_base)
        val editTarget = findViewById<TextView>(R.id.edit_target)
        val logout = findViewById<TextView>(R.id.logout)
        loader = findViewById(R.id.loader)

        val sharedPreferences = getSharedPreferences(MyPREF, Context.MODE_PRIVATE)
        base = sharedPreferences.getString("base", "")?.toUpperCase(Locale.ROOT) ?: ""
        compare = sharedPreferences.getString("compare", "")?.toUpperCase(Locale.ROOT) ?: ""

        baseCurrencyText.text = getString(R.string.base_currency_value, base, base)

        editBase.setOnClickListener {
            val intent = Intent(this, AddBaseActivity::class.java)
            intent.putExtra("action", "edit-base")
            startActivity(intent)
            finish()
        }

        editTarget.setOnClickListener {
            val intent = Intent(this, AddCompareActivity::class.java)
            intent.putExtra("action", "edit-target")
            startActivity(intent)
            finish()
        }

        refresh.setOnClickListener {
            loadData(result)
        }

        loadData(result)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        logout.setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this) {
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

    }

    private fun loadData(result: TextView) {
        loader.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)

        val stringRequest = JsonObjectRequest(Request.Method.GET, "$URL$base", null,
            { response ->
                if (response != null) {
                    loader.visibility = View.GONE
                    val rates = response.getJSONObject("rates")
                    val value = rates.getString(compare)
                    result.text = getString(R.string.result, compare, value)
                    Log.i(TAG, "Success Response")
                }
            },
            { error ->
                loader.visibility = View.GONE
                Log.e(TAG, error.toString())
            })

        queue.add(stringRequest)
    }

    companion object {
        const val MyPREF = "MyPref"
        const val URL = "https://api.exchangeratesapi.io/latest?base="
        const val TAG = "ResultsActivity"
    }
}