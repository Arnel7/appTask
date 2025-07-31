package kappela.com.tonnom

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kappela.com.tonnom.onboarding.OnboardingScreen
import kotlinx.coroutines.delay

sealed class EcranPrincipal {
    object Onboarding : EcranPrincipal()
    object SystemeAuth : EcranPrincipal()
    object SystemeDonnees : EcranPrincipal()
}

@Composable
fun MainApp() {
    var ecranActuel by remember { mutableStateOf<EcranPrincipal>(EcranPrincipal.Onboarding) }
    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    
    // Gestion du bouton back Android
    BackHandler {
        when (ecranActuel) {
            is EcranPrincipal.Onboarding -> {
                // Double tap pour quitter depuis l'onboarding
                val currentTime = System.currentTimeMillis()
                if (currentTime - backPressedTime < 2000) {
                    (context as? androidx.activity.ComponentActivity)?.finish()
                } else {
                    backPressedTime = currentTime
                    Toast.makeText(
                        context,
                        "Appuyez encore une fois pour quitter l'application",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is EcranPrincipal.SystemeAuth, 
            is EcranPrincipal.SystemeDonnees -> {
                // Retour direct à l'onboarding depuis les autres écrans
                ecranActuel = EcranPrincipal.Onboarding
            }
        }
    }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (ecranActuel) {
            is EcranPrincipal.Onboarding -> {
                OnboardingScreen(
                    onChoisirAuthentication = {
                        ecranActuel = EcranPrincipal.SystemeAuth
                    },
                    onChoisirDonnees = {
                        ecranActuel = EcranPrincipal.SystemeDonnees
                    }
                )
            }
            
            is EcranPrincipal.SystemeAuth -> {
                AuthApp(
                    onRetourOnboarding = {
                        ecranActuel = EcranPrincipal.Onboarding
                    }
                )
            }
            
            is EcranPrincipal.SystemeDonnees -> {
                DataApp(
                    onRetourOnboarding = {
                        ecranActuel = EcranPrincipal.Onboarding
                    }
                )
            }
        }
    }
}