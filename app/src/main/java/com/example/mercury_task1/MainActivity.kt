package com.example.mercury_task1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("Hello!")
        val intent: Intent = Intent(this, ColorListActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch{
            delayedActivityLaunch(2000L, intent)
        }
    }

    private suspend fun delayedActivityLaunch(delayTime: Long, intent: Intent){
        delay(delayTime)
        startActivity(intent)
        println("Activity launched")
        finish()
    }
}
