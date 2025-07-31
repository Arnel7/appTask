# TonNOM - Système d'Authentification et Gestion de Données

Application Android développée avec **Kotlin** et **Jetpack Compose** implémentant un système d'authentification avec redirection automatique selon les rôles.

## 📱 Fonctionnalités

### 🔐 Authentification
- **Connexion** avec nom d'utilisateur et mot de passe
- **Déconnexion** sécurisée
- **Validation** des champs de saisie
- **Redirection automatique** selon le rôle utilisateur

### 👥 Gestion des Rôles
- **Super Admin** : Accès à l'administration et gestion des comptes
- **Utilisateur** : Accès direct au module de gestion de données

### 🛠️ Administration (Super Admin uniquement)
- **Créer des utilisateurs** 
- **Supprimer des utilisateurs** individuellement
- **Supprimer tous les utilisateurs** en une fois
- **Liste en temps réel** des utilisateurs

### 📊 Gestion de Données (Utilisateurs)
- **Saisie de personnes** avec informations détaillées
- **Tableau des personnes** enregistrées
- **Consultation des détails** de chaque personne

## 🏗️ Architecture Technique

### Base de données (Room)
```
📦 Database
├── 👤 utilisateurs_auth
│   ├── id (clé primaire)
│   ├── username
│   ├── motDePasse
│   └── roleId
└── 🏷️ roles
    ├── id (clé primaire)
    └── nom (super_admin, user_sample)
```

### Structure du projet
```
📁 app/src/main/java/kappela/com/tonnom/
├── 🔐 auth/
│   ├── entities/          # Entités de base de données
│   │   ├── UtilisateurAuth.kt
│   │   └── Role.kt
│   ├── dao/              # Data Access Objects
│   │   ├── UtilisateurAuthDao.kt
│   │   └── RoleDao.kt
│   └── screens/          # Écrans de l'application
│       ├── ConnexionScreen.kt
│       └── AccueilScreen.kt
├── 🗄️ database/
│   └── AppDatabase.kt    # Configuration Room
└── 📱 MainActivity.kt    # Point d'entrée principal
```

## 🚀 Installation et Utilisation

### Prérequis
- **Android Studio** (dernière version)
- **SDK Android** 24+ (Android 7.0)
- **Java 17** ou supérieur

### Installation
1. **Cloner** le projet
```bash
git clone [url-du-projet]
cd TonNOM
```

2. **Ouvrir** dans Android Studio

3. **Synchroniser** le projet (Gradle)

4. **Compiler** et exécuter sur un émulateur ou appareil

### 👨‍💼 Compte Administrateur par défaut
```
Nom d'utilisateur : admin
Mot de passe : admin123
```

## 📖 Guide d'utilisation

### 🔑 Première utilisation
1. Lancez l'application (démarrage direct sur connexion)
2. Utilisez le compte admin par défaut (admin/admin123)
3. Créez vos premiers utilisateurs depuis l'interface d'administration

### 👤 En tant qu'Admin
1. **Se connecter** avec le compte admin
2. **Créer des utilisateurs** via le bouton "Créer User Sample"
3. **Gérer les utilisateurs** depuis la liste
4. **Supprimer** individuellement ou en masse

### 👨‍💻 En tant qu'Utilisateur
1. **Se connecter** avec vos identifiants (créés par l'admin)
2. **Accéder automatiquement** au module de gestion de données
3. **Saisir et gérer** les informations des personnes

## 🛡️ Sécurité

- ✅ **Validation** des entrées utilisateur
- ✅ **Séparation des rôles** stricte
- ✅ **Gestion d'erreurs** complète
- ✅ **Base de données locale** sécurisée

## 🔧 Technologies utilisées

- **Kotlin** - Langage principal
- **Jetpack Compose** - Interface utilisateur moderne
- **Room Database** - Base de données locale
- **Coroutines** - Programmation asynchrone
- **Material Design 3** - Design system

## 📱 Captures d'écran

### Écran de Connexion
- Champs nom d'utilisateur et mot de passe
- Indication du compte admin par défaut
- Information sur la création de comptes par l'admin uniquement

### Écran d'Accueil Admin
- Menu d'administration
- Liste des utilisateurs en temps réel
- Actions de création et suppression

### Module Gestion de Données (Utilisateurs)
- Saisie de nouvelles personnes
- Tableau des personnes enregistrées
- Consultation des détails
- Interface intuitive et moderne

## 🚀 Développement

### Ajouter de nouvelles fonctionnalités
1. **Créer** les nouvelles entités dans `auth/entities/`
2. **Définir** les DAOs correspondants dans `auth/dao/`
3. **Mettre à jour** `AppDatabase.kt`
4. **Créer** les nouveaux écrans dans `auth/screens/`
5. **Modifier** la navigation dans `MainActivity.kt`

### Base de données
- **Version actuelle** : 2
- **Migration** : Automatique avec `fallbackToDestructiveMigration()`
- **Initialisation** : Rôles et admin créés automatiquement

## 📄 Licence

Ce projet est développé à des fins éducatives et de démonstration.

## 👨‍💻 Développeur

Développé avec ❤️ en Kotlin et Jetpack Compose

---

*Pour toute question ou suggestion, n'hésitez pas à ouvrir une issue ou contribuer au projet !*