package com.pradeep.currencycompare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddCompareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_compare)

        supportActionBar?.hide()

        val baseCurrency = findViewById<TextView>(R.id.base)
        val currencyToCompare = findViewById<EditText>(R.id.currency_to_compare)
        val next = findViewById<TextView>(R.id.next)

        val sharedPreferences = getSharedPreferences(ResultsActivity.MyPREF, Context.MODE_PRIVATE)
        val base = sharedPreferences.getString("base", "")
        val compare = sharedPreferences.getString("compare", "")
        val editTarget = intent.getStringExtra("action")
        if (compare != "" && compare != null && editTarget != "edit-target") {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
            finish()
        }

        baseCurrency.text = base

        next.setOnClickListener {
            if (currencyToCompare.text.toString().isEmpty()) {
                currencyToCompare.error = getString(R.string.enter_currency)
            } else {
                val editor = sharedPreferences.edit()
                editor.putString("compare", currencyToCompare.text.toString())
                editor.apply()
                val intent = Intent(this, ResultsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}