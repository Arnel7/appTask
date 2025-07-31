package kappela.com.tonnom.data.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kappela.com.tonnom.data.entities.Personne
import kappela.com.tonnom.data.database.PersonneDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableauPersonnesScreen(
    onRetourSaisie: () -> Unit,
    onVoirDetails: (Personne) -> Unit,
    onRetourOnboarding: () -> Unit = {},
    isUserMode: Boolean = true,
    personnesTemporaires: List<Personne> = emptyList()
) {
    val context = LocalContext.current
    val database = remember { PersonneDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    
    val personnesDB by database.personneDao().getToutesPersonnes().collectAsState(initial = emptyList())
    val personnes = if (isUserMode) personnesTemporaires else personnesDB
    var personneASupprimer by remember { mutableStateOf<Personne?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    
    // Debug log
    LaunchedEffect(personnes) {
        android.util.Log.d("TonNOM_TABLEAU", "Personnes dans le tableau: ${personnes.size}")
        personnes.forEach { personne ->
            android.util.Log.d("TonNOM_TABLEAU", "Personne: ${personne.nom} ${personne.prenoms}")
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // App Bar
        TopAppBar(
            title = {
                Text(
                    text = if (isUserMode) "📊 Mes Données (${personnes.size})" else "📊 Tableau (${personnes.size})",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onRetourSaisie) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Retour saisie"
                    )
                }
            },
            actions = {
                IconButton(onClick = onRetourSaisie) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Ajouter"
                    )
                }
                if (personnes.isNotEmpty() && !isUserMode) {
                    IconButton(onClick = { showDeleteAllDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Supprimer tout"
                        )
                    }
                }
                IconButton(onClick = onRetourOnboarding) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Menu principal"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        
        // Contenu principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (personnes.isEmpty()) {
                // État vide
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aucune personne enregistrée",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ajoutez votre première personne pour commencer !",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onRetourSaisie,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ajouter une personne")
                        }
                    }
                }
            } else {
                // En-tête du tableau
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nom",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1.5f)
                        )
                        Text(
                            text = "Prénoms",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1.5f)
                        )
                        Text(
                            text = "Sexe",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Catégorie",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = "Actions",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Message informatif pour les utilisateurs
                if (isUserMode && personnes.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "💡 Données temporaires",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Vos données ne sont pas enregistrées.\nElles disparaîtront si vous quittez cette section.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (isUserMode && personnes.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = "⚠️ Ces données sont temporaires et ne sont pas sauvegardées",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Liste des personnes
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(personnes) { personne ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(0.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Nom
                                Text(
                                    text = personne.nom,
                                    modifier = Modifier.weight(1.5f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Medium
                                )
                                
                                // Prénoms
                                Text(
                                    text = personne.prenoms,
                                    modifier = Modifier.weight(1.5f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                
                                // Sexe
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (personne.sexe == "Masculin") 
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else 
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (personne.sexe == "Masculin") "M" else "F",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (personne.sexe == "Masculin") 
                                            MaterialTheme.colorScheme.primary
                                        else 
                                            MaterialTheme.colorScheme.secondary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                // Catégorie
                                Text(
                                    text = personne.categorie,
                                    modifier = Modifier.weight(1.2f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                // Actions
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Bouton Détails
                                    IconButton(
                                        onClick = { onVoirDetails(personne) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Info,
                                            contentDescription = "Détails",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    
                                    // Bouton Supprimer (seulement en mode admin)
                                    if (!isUserMode) {
                                        IconButton(
                                            onClick = {
                                                personneASupprimer = personne
                                                showDeleteDialog = true
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Supprimer",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Dialog de suppression individuelle
    if (showDeleteDialog && personneASupprimer != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "🗑️ Confirmer la suppression",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Voulez-vous vraiment supprimer ${personneASupprimer?.nom} ${personneASupprimer?.prenoms} ?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                personneASupprimer?.let { personne ->
                                    database.personneDao().supprimerPersonne(personne)
                                }
                                showDeleteDialog = false
                                personneASupprimer = null
                            } catch (e: Exception) {
                                android.util.Log.e("TonNOM_DELETE", "Erreur suppression: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
    
    // Dialog de suppression totale
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = {
                Text(
                    text = "⚠️ Supprimer toutes les personnes",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Cette action supprimera TOUTES les personnes (${personnes.size}). Cette action est irréversible.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                database.personneDao().supprimerToutesPersonnes()
                                showDeleteAllDialog = false
                            } catch (e: Exception) {
                                android.util.Log.e("TonNOM_DELETE_ALL", "Erreur suppression totale: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text("Supprimer tout", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("Annuler")
                }
            },
            containerColor = MaterialTheme.colorScheme.errorContainer,
            titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
            textContentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}