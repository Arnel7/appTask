package kappela.com.tonnom.data.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kappela.com.tonnom.data.entities.Personne
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPersonneScreen(
    personne: Personne,
    onRetour: () -> Unit
) {
    val scrollState = rememberScrollState()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy à HH:mm", Locale.FRANCE)
    val dateCreation = dateFormat.format(Date(personne.dateCreation))
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // App Bar
        TopAppBar(
            title = {
                Text(
                    text = "👤 ${personne.prenoms} ${personne.nom}",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onRetour) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        
        // Contenu principal avec scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Photo/Avatar grande
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(60.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Nom complet centré
            Text(
                text = "${personne.prenoms} ${personne.nom}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Informations détaillées
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nom
                DetailCard(
                    icon = Icons.Default.Person,
                    titre = "Nom de famille",
                    valeur = personne.nom,
                    couleur = MaterialTheme.colorScheme.primary
                )
                
                // Prénoms
                DetailCard(
                    icon = Icons.Default.Badge,
                    titre = "Prénoms",
                    valeur = personne.prenoms,
                    couleur = MaterialTheme.colorScheme.secondary
                )
                
                // Sexe
                DetailCard(
                    icon = if (personne.sexe == "Masculin") Icons.Default.Person else Icons.Default.Person,
                    titre = "Sexe",
                    valeur = personne.sexe,
                    couleur = if (personne.sexe == "Masculin") 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.secondary,
                    badge = if (personne.sexe == "Masculin") "M" else "F"
                )
                
                // Catégorie
                DetailCard(
                    icon = Icons.Default.Work,
                    titre = "Catégorie professionnelle",
                    valeur = personne.categorie,
                    couleur = MaterialTheme.colorScheme.tertiary,
                    badge = when (personne.categorie) {
                        "Professionnel" -> "💼"
                        "Étudiant" -> "🎓"
                        "Chômeur" -> "📋"
                        "Retraité" -> "🏖️"
                        else -> "❓"
                    }
                )
                
                // Date de création
                DetailCard(
                    icon = Icons.Default.CalendarToday,
                    titre = "Date d'enregistrement",
                    valeur = dateCreation,
                    couleur = MaterialTheme.colorScheme.outline
                )
                

            }
            
ada            Spacer(modifier = Modifier.height(24.dp))
            
            // Bouton de retour
            Button(
                onClick = onRetour,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Retour au tableau",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun DetailCard(
    icon: ImageVector,
    titre: String,
    valeur: String,
    couleur: androidx.compose.ui.graphics.Color,
    badge: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
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
                    containerColor = couleur.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = couleur
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Texte
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titre,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = valeur,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Badge optionnel
            badge?.let {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = couleur
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, valeur: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valeur,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}