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
    onRetourOnboarding: () -> Unit = {}
) {
    var ecranActuel by remember { mutableStateOf<EcranData>(EcranData.SaisiePersonne) }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (ecranActuel) {
            is EcranData.SaisiePersonne -> {
                SaisiePersonneScreen(
                    onPersonneAjoutee = {
                        // Rester sur l'écran de saisie après ajout
                        // L'utilisateur peut choisir d'aller au tableau s'il le souhaite
                    },
                    onVoirTableau = {
                        ecranActuel = EcranData.TableauPersonnes
                    },
                    onRetourOnboarding = onRetourOnboarding
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
                    onRetourOnboarding = onRetourOnboarding
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