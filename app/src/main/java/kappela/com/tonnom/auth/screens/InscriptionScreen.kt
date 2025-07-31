package kappela.com.tonnom.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kappela.com.tonnom.auth.entities.Role
import kappela.com.tonnom.auth.entities.UtilisateurAuth
import kappela.com.tonnom.database.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscriptionScreen(
    onInscriptionReussie: () -> Unit,
    onRetourConnexion: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var motDePasse by remember { mutableStateOf("") }
    var confirmMotDePasse by remember { mutableStateOf("") }
    var messageErreur by remember { mutableStateOf("") }
    var messageSucces by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TonNOM",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "Inscription",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        OutlinedTextField(
            value = username,
            onValueChange = { 
                username = it
                messageErreur = ""
                messageSucces = ""
            },
            label = { Text("Nom d'utilisateur") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = motDePasse,
            onValueChange = { 
                motDePasse = it
                messageErreur = ""
                messageSucces = ""
            },
            label = { Text("Mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = confirmMotDePasse,
            onValueChange = { 
                confirmMotDePasse = it
                messageErreur = ""
                messageSucces = ""
            },
            label = { Text("Confirmer le mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        
        if (messageErreur.isNotEmpty()) {
            Text(
                text = messageErreur,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        if (messageSucces.isNotEmpty()) {
            Text(
                text = messageSucces,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                when {
                    username.isBlank() || motDePasse.isBlank() || confirmMotDePasse.isBlank() -> {
                        messageErreur = "Veuillez remplir tous les champs"
                    }
                    username.length < 3 -> {
                        messageErreur = "Le nom d'utilisateur doit contenir au moins 3 caractères"
                    }
                    motDePasse.length < 3 -> {
                        messageErreur = "Le mot de passe doit contenir au moins 3 caractères"
                    }
                    motDePasse != confirmMotDePasse -> {
                        messageErreur = "Les mots de passe ne correspondent pas"
                    }
                    else -> {
                        isLoading = true
                        scope.launch {
                            try {
                                // Vérifier si l'utilisateur existe déjà
                                val utilisateurExistant = database.utilisateurAuthDao().getUtilisateurParUsername(username)
                                if (utilisateurExistant != null) {
                                    messageErreur = "Ce nom d'utilisateur existe déjà"
                                } else {
                                    // Récupérer le rôle "user_sample"
                                    val roleUserSample = database.roleDao().getRoleParNom(Role.USER_SAMPLE)
                                    val roleId = roleUserSample?.id ?: 2 // ID par défaut si non trouvé
                                    
                                    // Créer le nouvel utilisateur
                                    val nouvelUtilisateur = UtilisateurAuth(
                                        username = username,
                                        motDePasse = motDePasse,
                                        roleId = roleId
                                    )
                                    
                                    database.utilisateurAuthDao().ajouterUtilisateur(nouvelUtilisateur)
                                    messageSucces = "Inscription réussie ! Redirection..."
                                    
                                    // Attendre un peu puis rediriger
                                    kotlinx.coroutines.delay(1500)
                                    onInscriptionReussie()
                                }
                            } catch (e: Exception) {
                                messageErreur = "Erreur lors de l'inscription: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("S'inscrire")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onRetourConnexion) {
            Text("Déjà un compte ? Se connecter")
        }
    }
}