package kappela.com.tonnom.data.entities

enum class Sexe(val libelle: String) {
    MASCULIN("Masculin"),
    FEMININ("Féminin");
    
    companion object {
        fun fromString(value: String): Sexe {
            return when (value) {
                "Masculin" -> MASCULIN
                "Féminin" -> FEMININ
                else -> MASCULIN
            }
        }
        
        fun getAllSexes(): List<String> {
            return values().map { it.libelle }
        }
    }
}