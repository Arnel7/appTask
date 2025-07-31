package kappela.com.tonnom.data.entities

enum class Categorie(val libelle: String) {
    PROFESSIONNEL("Professionnel"),
    ETUDIANT("Étudiant"),
    CHOMEUR("Chômeur"),
    RETRAITE("Retraité"),
    AUTRE("Autre");
    
    companion object {
        fun fromString(value: String): Categorie {
            return when (value) {
                "Professionnel" -> PROFESSIONNEL
                "Étudiant" -> ETUDIANT
                "Chômeur" -> CHOMEUR
                "Retraité" -> RETRAITE
                "Autre" -> AUTRE
                else -> AUTRE
            }
        }
        
        fun getAllCategories(): List<String> {
            return values().map { it.libelle }
        }
    }
}