package kappela.com.tonnom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kappela.com.tonnom.auth.screens.AccueilScreen
import kappela.com.tonnom.auth.screens.ConnexionScreen
import kappela.com.tonnom.database.AppDatabase

sealed class EcranAuth {
    object Connexion : EcranAuth()
    data class Accueil(val isAdmin: Boolean) : EcranAuth()
    object GestionDonnees : EcranAuth()
}

@Composable
fun AuthApp() {
    var ecranActuel by remember { mutableStateOf<EcranAuth>(EcranAuth.Connexion) }
    var isCurrentUserAdmin by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // Initialiser la base de données
    val database = remember {
        android.util.Log.d("TonNOM_MAIN", "=== CRÉATION DE LA BASE DE DONNÉES ===")
        AppDatabase.getDatabase(context)
    }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (ecranActuel) {
            is EcranAuth.Connexion -> {
                ConnexionScreen(
                    onConnexionReussie = { isAdmin ->
                        isCurrentUserAdmin = isAdmin
                        if (isAdmin) {
                            // Admin va vers l'écran d'accueil pour gérer les comptes
                            ecranActuel = EcranAuth.Accueil(isAdmin)
                        } else {
                            // Utilisateur simple va directement vers la gestion de données
                            ecranActuel = EcranAuth.GestionDonnees
                        }
                    }
                )
            }
            
            is EcranAuth.Accueil -> {
                AccueilScreen(
                    isAdmin = (ecranActuel as EcranAuth.Accueil).isAdmin,
                    onDeconnexion = {
                        ecranActuel = EcranAuth.Connexion
                    },
                    onAccederDonnees = {
                        ecranActuel = EcranAuth.GestionDonnees
                    }
                )
            }
            
            is EcranAuth.GestionDonnees -> {
                DataApp(
                    onRetourAccueil = {
                        if (isCurrentUserAdmin) {
                            ecranActuel = EcranAuth.Accueil(true)
                        } else {
                            ecranActuel = EcranAuth.Connexion
                        }
                    },
                    isUserMode = !isCurrentUserAdmin  // true pour utilisateur normal, false pour admin
                )
            }
        }
    }
}