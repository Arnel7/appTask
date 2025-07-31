package kappela.com.tonnom.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kappela.com.tonnom.database.entities.Utilisateur
import kotlinx.coroutines.flow.Flow

@Dao
interface UtilisateurDao {

    // ✅ MÉTHODES CORRIGÉES

    @Query("SELECT * FROM utilisateurs ORDER BY nom ASC")
    fun getTousUtilisateurs(): Flow<List<Utilisateur>>

    @Query("SELECT * FROM utilisateurs")
    suspend fun getTousUtilisateursListe(): List<Utilisateur>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterUtilisateur(utilisateur: Utilisateur)

    @Update
    suspend fun modifierUtilisateur(utilisateur: Utilisateur)

    @Delete
    suspend fun supprimerUtilisateur(utilisateur: Utilisateur)

    @Query("SELECT * FROM utilisateurs WHERE id = :id")
    suspend fun getUtilisateurParId(id: Int): Utilisateur?

    // ✅ RECHERCHE CORRIGÉE
    @Query("SELECT * FROM utilisateurs WHERE nom LIKE '%' || :recherche || '%'")
    suspend fun rechercherUtilisateurs(recherche: String): List<Utilisateur>

    // Alternative avec opérateur || (SQLite)
    @Query("SELECT * FROM utilisateurs WHERE nom LIKE '%' || :recherche || '%'")
    suspend fun rechercherUtilisateursConcat(recherche: String): List<Utilisateur>

    // Recherche multiple champs
    @Query("""
        SELECT * FROM utilisateurs 
        WHERE nom LIKE '%' || :recherche || '%' 
        OR email LIKE '%' || :recherche || '%'
    """)
    suspend fun rechercherDansTousLesChamps(recherche: String): List<Utilisateur>

    @Query("SELECT COUNT(*) FROM utilisateurs")
    suspend fun compterUtilisateurs(): Int

    @Query("DELETE FROM utilisateurs")
    suspend fun supprimerTousLesUtilisateurs()
}
