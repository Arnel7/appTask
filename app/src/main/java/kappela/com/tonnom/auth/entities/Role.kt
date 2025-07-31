package kappela.com.tonnom.auth.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class Role(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String
) {
    companion object {
        const val SUPER_ADMIN = "super_admin"
        const val USER_SAMPLE = "user_sample"
    }
}