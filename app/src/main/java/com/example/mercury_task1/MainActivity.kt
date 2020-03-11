package com.example.mercury_task1

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

val DELAY_TIME: String = "DELAY_TIME"

class MainActivity : AppCompatActivity(), CoroutineScope {

    private var localJob: Job = Job()
    override val coroutineContext = Dispatchers.Default + localJob
    private var delayTime: Long = 2000L
    private var currentDate: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            delayTime = savedInstanceState.getLong(DELAY_TIME)
        }
        currentDate = SystemClock.elapsedRealtime()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delayTime -= SystemClock.elapsedRealtime() - currentDate
        outState.putLong(DELAY_TIME, delayTime)
    }

    override fun onStart() {
        val intent: Intent = Intent(this, ColorListActivity::class.java)
        localJob = launch {
            delayedActivityLaunch(intent)
        }
        super.onStart()
    }

    override fun onStop() {
        localJob.cancel()
        super.onStop()
    }

    private suspend fun delayedActivityLaunch(intent: Intent) {
        if (delayTime > 0) {
            delay(delayTime)
        }
        startActivity(intent)
        finish()
    }
}
