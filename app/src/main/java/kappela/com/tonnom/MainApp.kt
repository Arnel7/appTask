package kappela.com.tonnom

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun MainApp() {
    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    
    // Gestion du bouton back Android - double tap pour quitter
    BackHandler {
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
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // Démarrage direct sur le système d'authentification
        AuthApp()
    }
}