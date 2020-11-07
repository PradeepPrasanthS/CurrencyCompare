package com.pradeep.currencycompare

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences(MyPREF, Context.MODE_PRIVATE)

        val baseCurrency = findViewById<TextView>(R.id.base)

        val base = intent.getStringExtra("base")
        val compare = intent.getStringExtra("compare")

        val editor = sharedPreferences.edit()
        editor.putString("base", base)
        editor.putString("compare", compare)
        editor.apply()

        baseCurrency.text = base
    }

    companion object{
        const val MyPREF = "MyPref"
    }
}