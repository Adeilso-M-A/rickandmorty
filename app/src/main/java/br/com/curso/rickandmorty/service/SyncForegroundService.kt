package br.com.curso.rickandmorty.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import br.com.curso.rickandmorty.R
import br.com.curso.rickandmorty.utils.NotificationHelper

class SyncForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setContentTitle("Sincronizando...")
            .setContentText("Buscando dados no serviço de primeiro plano")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(101, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}