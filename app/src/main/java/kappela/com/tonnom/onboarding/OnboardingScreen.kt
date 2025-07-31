package kappela.com.tonnom.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    onChoisirAuthentication: () -> Unit,
    onChoisirDonnees: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo principal
            Card(
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(70.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "T",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            // Titre et sous-titre
            Text(
                text = "Bienvenue dans TonNOM",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Choisissez l'application que vous souhaitez tester",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            // Cartes de choix
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Option 1: Système d'authentification
                ChoiceCard(
                    icon = Icons.Default.Lock,
                    titre = "🔐 Système d'Authentification",
                    description = "Connexion, inscription, gestion des utilisateurs et rôles (Admin/User)",
                    couleurPrimaire = MaterialTheme.colorScheme.primary,
                    couleurSecondaire = MaterialTheme.colorScheme.onPrimary,
                    onClick = onChoisirAuthentication
                )
                
                // Option 2: Gestion des données personnelles
                ChoiceCard(
                    icon = Icons.Default.Person,
                    titre = "📋 Gestion des Données",
                    description = "Saisie, affichage et gestion des informations personnelles (indépendant)",
                    couleurPrimaire = MaterialTheme.colorScheme.secondary,
                    couleurSecondaire = MaterialTheme.colorScheme.onSecondary,
                    onClick = onChoisirDonnees
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Note informative
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "💡 Les deux systèmes sont complètement indépendants avec leurs propres bases de données",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ChoiceCard(
    icon: ImageVector,
    titre: String,
    description: String,
    couleurPrimaire: androidx.compose.ui.graphics.Color,
    couleurSecondaire: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = couleurPrimaire
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icône
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = couleurSecondaire.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp),
                    tint = couleurSecondaire
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Texte
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = couleurSecondaire,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = couleurSecondaire.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
            
            // Flèche
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = couleurSecondaire,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}