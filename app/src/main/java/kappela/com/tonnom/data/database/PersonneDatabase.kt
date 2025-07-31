package kappela.com.tonnom.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kappela.com.tonnom.data.dao.PersonneDao
import kappela.com.tonnom.data.entities.Personne

@Database(
    entities = [Personne::class],
    version = 1,
    exportSchema = false
)
abstract class PersonneDatabase : RoomDatabase() {

    abstract fun personneDao(): PersonneDao

    companion object {
        @Volatile
        private var INSTANCE: PersonneDatabase? = null

        fun getDatabase(context: Context): PersonneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonneDatabase::class.java,
                    "personnes_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                
                // Force la création immédiate de la base de données
                instance.openHelper.writableDatabase
                
                android.util.Log.d("PersonneDB", "=== Base de données PERSONNES créée ===")
                
                INSTANCE = instance
                instance
            }
        }
    }
}