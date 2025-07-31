package kappela.com.tonnom.auth.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kappela.com.tonnom.auth.entities.UtilisateurAuth
import kotlinx.coroutines.flow.Flow

@Dao
interface UtilisateurAuthDao {

    @Query("SELECT * FROM utilisateurs_auth")
    fun getTousUtilisateurs(): Flow<List<UtilisateurAuth>>

    @Query("SELECT * FROM utilisateurs_auth WHERE username = :username AND motDePasse = :motDePasse")
    suspend fun connexion(username: String, motDePasse: String): UtilisateurAuth?

    @Query("SELECT * FROM utilisateurs_auth WHERE username = :username")
    suspend fun getUtilisateurParUsername(username: String): UtilisateurAuth?

    @Query("SELECT * FROM utilisateurs_auth WHERE roleId = :roleId")
    suspend fun getUtilisateursParRole(roleId: Int): List<UtilisateurAuth>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterUtilisateur(utilisateur: UtilisateurAuth)

    @Update
    suspend fun modifierUtilisateur(utilisateur: UtilisateurAuth)

    @Delete
    suspend fun supprimerUtilisateur(utilisateur: UtilisateurAuth)

    @Query("DELETE FROM utilisateurs_auth WHERE roleId = :roleId")
    suspend fun supprimerUtilisateursParRole(roleId: Int)

    @Query("SELECT COUNT(*) FROM utilisateurs_auth")
    suspend fun compterUtilisateurs(): Int
}