package br.com.curso.rickandmorty.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.curso.rickandmorty.utils.NotificationHelper

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            NotificationHelper.showNotification(
                applicationContext,
                "Sincronização Ativa",
                "Dados de Rick and Morty atualizados em segundo plano."
            )
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}