package br.com.curso.rickandmorty.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.curso.rickandmorty.data.local.dao.RickDao
import br.com.curso.rickandmorty.data.local.entity.CharacterEntity
import br.com.curso.rickandmorty.data.local.entity.EpisodeEntity
import br.com.curso.rickandmorty.data.local.entity.LocationEntity
import br.com.curso.rickandmorty.data.local.entity.PortalLocationEntity
import br.com.curso.rickandmorty.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class,
        EpisodeEntity::class,
        UserEntity::class,
        PortalLocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rickDao(): RickDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rick_and_morty_db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            getDatabase(context).rickDao().insertUser(
                                UserEntity(username = "admin", passwordHash = "admin123")
                            )
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}