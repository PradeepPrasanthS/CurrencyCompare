package com.pradeep.currencycompare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_base)

        supportActionBar?.hide()

        val currencyCode = findViewById<EditText>(R.id.currency_code)
        val next = findViewById<TextView>(R.id.next)

        val sharedPreferences = getSharedPreferences(ResultsActivity.MyPREF, Context.MODE_PRIVATE)
        val base = sharedPreferences.getString("base", "")
        val editBase = intent.getStringExtra("action")
        if (base != "" && base != null && editBase != "edit-base"){
            val intent = Intent(this, AddCompareActivity::class.java)
            startActivity(intent)
            finish()
        }

        next.setOnClickListener {
            if (currencyCode.text.toString().isEmpty()){
                currencyCode.error = getString(R.string.enter_currency)
            } else {
                val editor = sharedPreferences.edit()
                editor.putString("base", currencyCode.text.toString())
                editor.apply()
                if (editBase != "edit-base"){
                    val intent = Intent(this, AddCompareActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, ResultsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}