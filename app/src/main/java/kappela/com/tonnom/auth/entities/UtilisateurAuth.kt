package kappela.com.tonnom.auth.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "utilisateurs_auth",
    indices = [Index(value = ["username"], unique = true)]
)
data class UtilisateurAuth(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val motDePasse: String,
    val roleId: Int
)