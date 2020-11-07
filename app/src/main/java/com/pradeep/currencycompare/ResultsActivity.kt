package com.pradeep.currencycompare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class ResultsActivity : AppCompatActivity() {

    private lateinit var base: String
    private lateinit var compare: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        supportActionBar?.hide()

        val baseCurrencyText = findViewById<TextView>(R.id.base_currency_text)
        val result = findViewById<TextView>(R.id.result)
        val refresh = findViewById<TextView>(R.id.refresh)
        val editBase = findViewById<TextView>(R.id.edit_base)
        val editTarget = findViewById<TextView>(R.id.edit_target)

        val sharedPreferences = getSharedPreferences(ResultsActivity.MyPREF, Context.MODE_PRIVATE)
        base = sharedPreferences?.getString("base", "") ?: ""
        compare = sharedPreferences?.getString("compare", "") ?: ""

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

    }

    private fun loadData(result: TextView) {
        val queue = Volley.newRequestQueue(this)

        val stringRequest = JsonObjectRequest(Request.Method.GET, "$URL$base", null,
            { response ->
                if (response != null) {
                    val rates = response.getJSONObject("rates")
                    val value = rates.getString(compare)
                    result.text = getString(R.string.result, compare, value)
                }
            },
            { error ->
                Log.i(TAG, error.toString())
            })

        queue.add(stringRequest)
    }

    companion object {
        const val MyPREF = "MyPref"
        const val URL = "https://api.exchangeratesapi.io/latest?base="
        const val TAG = "ResultsActivity"
    }
}