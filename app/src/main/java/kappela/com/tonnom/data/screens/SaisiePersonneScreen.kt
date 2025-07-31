package kappela.com.tonnom.data.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kappela.com.tonnom.data.entities.Personne
import kappela.com.tonnom.data.entities.Sexe
import kappela.com.tonnom.data.entities.Categorie
import kappela.com.tonnom.data.database.PersonneDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaisiePersonneScreen(
    onPersonneAjoutee: () -> Unit,
    onVoirTableau: () -> Unit,
    onRetourOnboarding: () -> Unit = {}
) {
    var nom by remember { mutableStateOf("") }
    var prenoms by remember { mutableStateOf("") }
    var sexeSelectionne by remember { mutableStateOf(Sexe.MASCULIN.libelle) }
    var categorieSelectionnee by remember { mutableStateOf(Categorie.PROFESSIONNEL.libelle) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var messageErreur by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val database = remember { PersonneDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // App Bar
        TopAppBar(
            title = {
                Text(
                    text = "📝 Saisie de Données",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onRetourOnboarding) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            },
            actions = {
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
        
        // Contenu principal avec scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Formulaire principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Champ Nom
                    Text(
                        text = "👤 Informations personnelles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    OutlinedTextField(
                        value = nom,
                        onValueChange = { nom = it },
                        label = { Text("Nom") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Champ Prénoms
                    OutlinedTextField(
                        value = prenoms,
                        onValueChange = { prenoms = it },
                        label = { Text("Prénoms") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Section Sexe
                    Text(
                        text = "⚥ Sexe",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Sexe.getAllSexes().forEach { sexe ->
                                Row(
                                    modifier = Modifier
                                        .selectable(
                                            selected = (sexe == sexeSelectionne),
                                            onClick = { sexeSelectionne = sexe },
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (sexe == sexeSelectionne),
                                        onClick = { sexeSelectionne = sexe }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = sexe,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Section Catégorie
                    Text(
                        text = "💼 Catégorie",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    var expanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = categorieSelectionnee,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Sélectionner une catégorie") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Categorie.getAllCategories().forEach { categorie ->
                                DropdownMenuItem(
                                    text = { Text(categorie) },
                                    onClick = {
                                        categorieSelectionnee = categorie
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Bouton Ajouter
                    Button(
                        onClick = {
                            when {
                                nom.isBlank() -> {
                                    messageErreur = "Le nom est obligatoire"
                                    showErrorDialog = true
                                }
                                prenoms.isBlank() -> {
                                    messageErreur = "Les prénoms sont obligatoires"
                                    showErrorDialog = true
                                }
                                else -> {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            val nouvellePersonne = Personne(
                                                nom = nom.trim(),
                                                prenoms = prenoms.trim(),
                                                sexe = sexeSelectionne,
                                                categorie = categorieSelectionnee
                                            )
                                            
                                            database.personneDao().ajouterPersonne(nouvellePersonne)
                                            
                                            // Réinitialiser le formulaire
                                            nom = ""
                                            prenoms = ""
                                            sexeSelectionne = Sexe.MASCULIN.libelle
                                            categorieSelectionnee = Categorie.PROFESSIONNEL.libelle
                                            
                                            showSuccessDialog = true
                                            onPersonneAjoutee()
                                            
                                        } catch (e: Exception) {
                                            messageErreur = "Erreur lors de l'ajout: ${e.message}"
                                            showErrorDialog = true
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                "✅ Ajouter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Boutons de navigation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onVoirTableau,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "📊 Voir le Tableau",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        OutlinedButton(
                            onClick = onRetourOnboarding,
                            modifier = Modifier.height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Menu principal"
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Dialog de succès
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Text(
                    text = "✅ Succès",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Text("La personne a été ajoutée avec succès !")
            },
            confirmButton = {
                TextButton(
                    onClick = { showSuccessDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
    
    // Dialog d'erreur
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "⚠️ Erreur",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = messageErreur)
            },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            },
            containerColor = MaterialTheme.colorScheme.errorContainer,
            titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
            textContentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}