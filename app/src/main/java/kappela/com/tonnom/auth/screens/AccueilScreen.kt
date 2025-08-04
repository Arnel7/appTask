package kappela.com.tonnom.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
//import androidx.compose.text.input.PasswordVisualTransformation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kappela.com.tonnom.auth.entities.Role
import kappela.com.tonnom.auth.entities.UtilisateurAuth
import kappela.com.tonnom.database.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilScreen(
    isAdmin: Boolean,
    onDeconnexion: () -> Unit,
    onAccederDonnees: () -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    
    // États pour les utilisateurs
    val utilisateurs by database.utilisateurAuthDao().getTousUtilisateurs().collectAsState(initial = emptyList())
    
    // Debug: Afficher le nombre d'utilisateurs
    LaunchedEffect(utilisateurs) {
        android.util.Log.d("TonNOM_UI", "Utilisateurs dans l'UI: ${utilisateurs.size}")
        utilisateurs.forEach { user ->
            android.util.Log.d("TonNOM_UI", "User: ${user.username}, Role: ${user.roleId}")
        }
    }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UtilisateurAuth?>(null) }
    var message by remember { mutableStateOf("") }
    
    // Filtrer uniquement les user_sample
    val userSamples = utilisateurs.filter { user ->
        scope.launch {
            val role = database.roleDao().getRoleParId(user.roleId)
            role?.nom == Role.USER_SAMPLE
        }
        true // Temporaire, on affichera tous pour l'instant et on filtrera dans l'UI
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // En-tête moderne avec gradient
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo circulaire
                    Card(
                        modifier = Modifier.size(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "T",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "AppTaskData",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = if (isAdmin) "🔐 Administration" else "👤 Utilisateur",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    IconButton(onClick = onDeconnexion) {
                        Icon(
                            Icons.Default.ExitToApp, 
                            contentDescription = "Déconnexion",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isAdmin) {
            // Menu Admin
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🛠️ Menu Administration",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Boutons d'action améliorés
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            TextButton(
                                onClick = { showCreateDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Add, 
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Créer Utilisateur",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            TextButton(
                                onClick = { 
                                    scope.launch {
                                        try {
                                            val roleUserSample = database.roleDao().getRoleParNom(Role.USER_SAMPLE)
                                            roleUserSample?.let { role ->
                                                database.utilisateurAuthDao().supprimerUtilisateursParRole(role.id)
                                                message = "✅ Tous les User simple supprimés"
                                            }
                                        } catch (e: Exception) {
                                            message = "❌ Erreur: ${e.message}"
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Delete, 
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onError,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Supprimer Tous",
                                        color = MaterialTheme.colorScheme.onError,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            if (message.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Liste des utilisateurs
            Text(
                text = "Utilisateurs Simple (${utilisateurs.filter { it.roleId == 2 }.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(utilisateurs.filter { it.roleId == 2 }) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            selectedUser = user
                            showEditDialog = true
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(user.username)
                            }
                            
                            IconButton(
                                onClick = {
                                    selectedUser = user
                                    showDeleteDialog = true
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Supprimer",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

        } else {
            // Vue utilisateur normal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "👋 Bienvenue !",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vous êtes connecté en tant qu'utilisateur simple.\nL'administrateur peut créer et supprimer votre compte.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        /*
        
        // Section Gestion des données (accessible à tous)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            shape = RoundedCornerShape(12.dp)
        )
        */
    }
    
    // Dialog de création d'utilisateur
    if (showCreateDialog) {
        CreateUserDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { username, password, selectedRole ->
                scope.launch {
                    try {
                        val utilisateurExistant = database.utilisateurAuthDao().getUtilisateurParUsername(username)
                        if (utilisateurExistant != null) {
                            message = "❌ Ce nom d'utilisateur existe déjà"
                        } else {
                            // Récupérer le rôle sélectionné
                            val role = database.roleDao().getRoleParNom(selectedRole)
                            val roleId = role?.id ?: (if (selectedRole == Role.SUPER_ADMIN) 1 else 2)
                            
                            database.utilisateurAuthDao().ajouterUtilisateur(
                                UtilisateurAuth(
                                    username = username,
                                    motDePasse = password,
                                    roleId = roleId
                                )
                            )
                            val roleText = if (selectedRole == Role.SUPER_ADMIN) "Admin" else "Utilisateur"
                            message = "✅ $roleText '$username' créé avec succès"
                            showCreateDialog = false
                        }
                    } catch (e: Exception) {
                        message = "Erreur: ${e.message}"
                    }
                }
            }
        )
    }
    
    // Dialog de suppression
    if (showDeleteDialog && selectedUser != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer l'utilisateur '${selectedUser?.username}' ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                selectedUser?.let { user ->
                                    database.utilisateurAuthDao().supprimerUtilisateur(user)
                                    message = "Utilisateur '${user.username}' supprimé"
                                }
                                showDeleteDialog = false
                                selectedUser = null
                            } catch (e: Exception) {
                                message = "Erreur: ${e.message}"
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
    
    // Dialog d'édition utilisateur
    if (showEditDialog && selectedUser != null) {
        EditUserDialog(
            utilisateur = selectedUser!!,
            onDismiss = { 
                showEditDialog = false
                selectedUser = null
            },
            onConfirm = { newUsername, newPassword ->
                scope.launch {
                    try {
                        // Vérifier si le nouveau username existe déjà (sauf si c'est le même)
                        if (newUsername != selectedUser!!.username) {
                            val existant = database.utilisateurAuthDao().getUtilisateurParUsername(newUsername)
                            if (existant != null) {
                                message = "❌ Ce nom d'utilisateur existe déjà"
                                showEditDialog = false
                                selectedUser = null
                                return@launch
                            }
                        }
                        
                        // Mettre à jour l'utilisateur
                        val utilisateurModifie = selectedUser!!.copy(
                            username = newUsername,
                            motDePasse = newPassword
                        )
                        database.utilisateurAuthDao().modifierUtilisateur(utilisateurModifie)
                        message = "✅ Utilisateur '${newUsername}' modifié avec succès"
                        
                        showEditDialog = false
                        selectedUser = null
                    } catch (e: Exception) {
                        message = "❌ Erreur: ${e.message}"
                        showEditDialog = false
                        selectedUser = null
                    }
                }
            }
        )
    }
}

@Composable
fun CreateUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit // Ajout du rôle
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(Role.USER_SAMPLE) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Créer un Utilisateur") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nom d'utilisateur") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Sélecteur de rôle
                Text(
                    text = "Rôle utilisateur:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Option Admin
                    Card(
                        modifier = Modifier
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == Role.SUPER_ADMIN) 
                                MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = { selectedRole = Role.SUPER_ADMIN }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "👑 Admin",
                                color = if (selectedRole == Role.SUPER_ADMIN) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Complet",
                                color = if (selectedRole == Role.SUPER_ADMIN) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    
                    // Option User
                    Card(
                        modifier = Modifier
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == Role.USER_SAMPLE) 
                                MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = { selectedRole = Role.USER_SAMPLE }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "👤 User",
                                color = if (selectedRole == Role.USER_SAMPLE) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Limité",
                                color = if (selectedRole == Role.USER_SAMPLE) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(username, password, selectedRole) },
                enabled = username.isNotBlank() && password.isNotBlank()
            ) {
                Text("Créer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@Composable
fun EditUserDialog(
    utilisateur: UtilisateurAuth,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var username by remember { mutableStateOf(utilisateur.username) }
    var password by remember { mutableStateOf(utilisateur.motDePasse) }
    var confirmPassword by remember { mutableStateOf(utilisateur.motDePasse) }
    var passwordsMatch by remember { mutableStateOf(true) }
    
    // Vérifier que les mots de passe correspondent
    LaunchedEffect(password, confirmPassword) {
        passwordsMatch = password == confirmPassword
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "✏️ Modifier Utilisateur",
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column {
                Text(
                    "Modifiez les informations de l'utilisateur:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nom d'utilisateur") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Nouveau mot de passe") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmer le mot de passe") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = !passwordsMatch,
                    supportingText = if (!passwordsMatch) {
                        { Text("Les mots de passe ne correspondent pas", color = MaterialTheme.colorScheme.error) }
                    } else null
                )
                
                if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    Text(
                        "Veuillez remplir tous les champs",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(username, password) },
                enabled = username.isNotBlank() && 
                         password.isNotBlank() && 
                         confirmPassword.isNotBlank() && 
                         passwordsMatch &&
                         password.length >= 3
            ) {
                Text("Modifier")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}