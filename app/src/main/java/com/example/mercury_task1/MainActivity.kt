package com.example.mercury_task1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope{

    private var localJob: Job = Job()
    override val coroutineContext = Dispatchers.Default + localJob

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            val intent: Intent = Intent(this, ColorListActivity::class.java)
            localJob = launch {
                delayedActivityLaunch(2000L, intent)
            }
        }
    }

    override fun finish() {
        cancel()
        super.finish()
    }

    private suspend fun delayedActivityLaunch(delayTime: Long, intent: Intent){
            delay(delayTime)
            startActivity(intent)
            finish()
        }
}
