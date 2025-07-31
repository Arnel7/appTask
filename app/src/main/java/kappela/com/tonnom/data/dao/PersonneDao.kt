package kappela.com.tonnom.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kappela.com.tonnom.data.entities.Personne
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonneDao {

    @Query("SELECT * FROM personnes ORDER BY dateCreation DESC")
    fun getToutesPersonnes(): Flow<List<Personne>>

    @Query("SELECT * FROM personnes ORDER BY nom ASC")
    suspend fun getToutesPersonnesListe(): List<Personne>

    @Query("SELECT * FROM personnes WHERE id = :id")
    suspend fun getPersonneParId(id: Int): Personne?

    @Query("SELECT * FROM personnes WHERE nom LIKE '%' || :recherche || '%' OR prenoms LIKE '%' || :recherche || '%'")
    suspend fun rechercherPersonnes(recherche: String): List<Personne>

    @Query("SELECT * FROM personnes WHERE sexe = :sexe")
    suspend fun getPersonnesParSexe(sexe: String): List<Personne>

    @Query("SELECT * FROM personnes WHERE categorie = :categorie")
    suspend fun getPersonnesParCategorie(categorie: String): List<Personne>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterPersonne(personne: Personne): Long

    @Update
    suspend fun modifierPersonne(personne: Personne)

    @Delete
    suspend fun supprimerPersonne(personne: Personne)

    @Query("DELETE FROM personnes")
    suspend fun supprimerToutesPersonnes()

    @Query("SELECT COUNT(*) FROM personnes")
    suspend fun compterPersonnes(): Int

    @Query("SELECT COUNT(*) FROM personnes WHERE sexe = :sexe")
    suspend fun compterPersonnesParSexe(sexe: String): Int

    @Query("SELECT COUNT(*) FROM personnes WHERE categorie = :categorie")
    suspend fun compterPersonnesParCategorie(categorie: String): Int
}