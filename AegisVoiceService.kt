package com.aegis.mobile.voice

import android.app.*
import android.content.Intent
import android.os.IBinder
import com.aegis.mobile.R

class AegisVoiceService : Service() {
    private val channelId = "aegis_voice"
    override fun onCreate() {
        super.onCreate()
        getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(channelId,"AEGIS Voice",NotificationManager.IMPORTANCE_LOW)
        )
        val n = Notification.Builder(this,channelId)
            .setContentTitle("AEGIS Voice")
            .setContentText("Voice mode is active")
            .setSmallIcon(R.drawable.ic_aegis_voice).build()
        startForeground(1001,n)
    }
    override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int {
        // Connect SpeechRecognizer/Whisper and a wake-word engine here.
        return START_STICKY
    }
    override fun onBind(intent:Intent?):IBinder?=null
}
