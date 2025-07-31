package kappela.com.tonnom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kappela.com.tonnom.data.entities.Personne
import kappela.com.tonnom.data.screens.DetailsPersonneScreen
import kappela.com.tonnom.data.screens.SaisiePersonneScreen
import kappela.com.tonnom.data.screens.TableauPersonnesScreen

sealed class EcranData {
    object SaisiePersonne : EcranData()
    object TableauPersonnes : EcranData()
    data class DetailsPersonne(val personne: Personne) : EcranData()
}

@Composable
fun DataApp(
    onRetourAccueil: () -> Unit = {},
    onRetourOnboarding: () -> Unit = {},
    isUserMode: Boolean = true
) {
    var ecranActuel by remember { mutableStateOf<EcranData>(EcranData.SaisiePersonne) }
    
    // Liste temporaire des personnes pour les utilisateurs normaux
    var personnesTemporaires by remember { mutableStateOf<List<Personne>>(emptyList()) }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (ecranActuel) {
            is EcranData.SaisiePersonne -> {
                SaisiePersonneScreen(
                    onPersonneAjoutee = { personne ->
                        if (isUserMode) {
                            // Mode utilisateur : ajouter à la liste temporaire
                            personnesTemporaires = personnesTemporaires + personne
                        }
                        // Sinon, c'est géré dans l'écran (sauvegarde DB pour admin)
                    },
                    onVoirTableau = {
                        ecranActuel = EcranData.TableauPersonnes
                    },
                    onRetourOnboarding = onRetourAccueil,
                    isUserMode = isUserMode
                )
            }
            
            is EcranData.TableauPersonnes -> {
                TableauPersonnesScreen(
                    onRetourSaisie = {
                        ecranActuel = EcranData.SaisiePersonne
                    },
                    onVoirDetails = { personne ->
                        ecranActuel = EcranData.DetailsPersonne(personne)
                    },
                    onRetourOnboarding = onRetourAccueil,
                    isUserMode = isUserMode,
                    personnesTemporaires = if (isUserMode) personnesTemporaires else emptyList()
                )
            }
            
            is EcranData.DetailsPersonne -> {
                val ecranDetails = ecranActuel as EcranData.DetailsPersonne
                DetailsPersonneScreen(
                    personne = ecranDetails.personne,
                    onRetour = {
                        ecranActuel = EcranData.TableauPersonnes
                    }
                )
            }
        }
    }
}