package kappela.com.tonnom.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personnes")
data class Personne(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String,
    val prenoms: String,
    val sexe: String, // "Masculin" ou "Féminin"
    val categorie: String, // "Professionnel", "Étudiant", etc.
    val dateCreation: Long = System.currentTimeMillis()
)