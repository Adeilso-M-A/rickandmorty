package br.com.curso.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.curso.rickandmorty.data.local.database.AppDatabase
import br.com.curso.rickandmorty.data.remote.RickApi
import br.com.curso.rickandmorty.data.repository.RickRepository
import br.com.curso.rickandmorty.service.SyncWorker
import br.com.curso.rickandmorty.ui.RickAndMortyAppNav
import br.com.curso.rickandmorty.utils.NotificationHelper
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        val database = AppDatabase.getDatabase(this)
        val api = RickApi.create()
        val repository = RickRepository(api, database.rickDao())

        setupPeriodicSyncWork()

        setContent {
            RickAndMortyAppNav(repository = repository)
        }
    }

    private fun setupPeriodicSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RickAndMortySyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}