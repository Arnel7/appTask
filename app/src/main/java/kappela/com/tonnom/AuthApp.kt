package kappela.com.tonnom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kappela.com.tonnom.auth.screens.AccueilScreen
import kappela.com.tonnom.auth.screens.ConnexionScreen
import kappela.com.tonnom.auth.screens.InscriptionScreen
import kappela.com.tonnom.database.AppDatabase

sealed class EcranAuth {
    object Connexion : EcranAuth()
    object Inscription : EcranAuth()
    data class Accueil(val isAdmin: Boolean) : EcranAuth()
    object GestionDonnees : EcranAuth()
}

@Composable
fun AuthApp(
    onRetourOnboarding: () -> Unit = {}
) {
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
                        ecranActuel = EcranAuth.Accueil(isAdmin)
                    },
                    onAllerInscription = {
                        ecranActuel = EcranAuth.Inscription
                    },
                    onRetourOnboarding = onRetourOnboarding
                )
            }
            
            is EcranAuth.Inscription -> {
                InscriptionScreen(
                    onInscriptionReussie = {
                        ecranActuel = EcranAuth.Connexion
                    },
                    onRetourConnexion = {
                        ecranActuel = EcranAuth.Connexion
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
                    },
                    onRetourOnboarding = onRetourOnboarding
                )
            }
            
            is EcranAuth.GestionDonnees -> {
                DataApp(
                    onRetourOnboarding = onRetourOnboarding
                )
            }
        }
    }
}