package kappela.com.tonnom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kappela.com.tonnom.database.dao.ProduitDao
import kappela.com.tonnom.database.dao.UtilisateurDao
import kappela.com.tonnom.database.entities.Produit
import kappela.com.tonnom.database.entities.Utilisateur
import kappela.com.tonnom.auth.dao.UtilisateurAuthDao
import kappela.com.tonnom.auth.dao.RoleDao
import kappela.com.tonnom.auth.entities.UtilisateurAuth
import kappela.com.tonnom.auth.entities.Role


@Database(
    entities = [Utilisateur::class, Produit::class, UtilisateurAuth::class, Role::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun utilisateurDao(): UtilisateurDao
    abstract fun produitDao(): ProduitDao
    abstract fun utilisateurAuthDao(): UtilisateurAuthDao
    abstract fun roleDao(): RoleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tonnom_database"
                )
                .fallbackToDestructiveMigration() // Recrée la DB si problème de version
                .build()
                
                // Force la création immédiate de la base de données
                instance.openHelper.writableDatabase
                
                // Initialiser les rôles par défaut
                Thread {
                    try {
                        android.util.Log.d("TonNOM_DB", "Initialisation de la base de données...")
                        val roleDao = instance.roleDao()
                        val userDao = instance.utilisateurAuthDao()
                        
                        val roles = listOf(
                            Role(1, Role.SUPER_ADMIN),
                            Role(2, Role.USER_SAMPLE)
                        )
                        
                        // Utilisation de runBlocking pour les opérations suspend
                        kotlinx.coroutines.runBlocking {
                            roleDao.ajouterRoles(roles)
                            android.util.Log.d("TonNOM_DB", "Rôles ajoutés: ${roles.size}")
                            
                            // Créer un admin par défaut s'il n'existe pas
                            val adminExists = userDao.getUtilisateurParUsername("admin")
                            if (adminExists == null) {
                                userDao.ajouterUtilisateur(
                                    UtilisateurAuth(username = "admin", motDePasse = "admin123", roleId = 1)
                                )
                                android.util.Log.d("TonNOM_DB", "Admin créé avec succès")
                            } else {
                                android.util.Log.d("TonNOM_DB", "Admin existe déjà")
                            }
                            
                            // Vérifier le nombre total d'utilisateurs
                            val totalUsers = userDao.compterUtilisateurs()
                            android.util.Log.d("TonNOM_DB", "Nombre total d'utilisateurs: $totalUsers")
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("TonNOM_DB", "Erreur lors de l'initialisation: ${e.message}", e)
                    }
                }.start()
                
                INSTANCE = instance
                instance
            }
        }
    }
}