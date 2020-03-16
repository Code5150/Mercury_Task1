package com.example.mercury_task1

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CreateElementActivity : AppCompatActivity() {

    private lateinit var itemName: String
    private var itemColor: Int = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_element)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //val db: SQLiteDatabase = baseContext.
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
