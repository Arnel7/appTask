# TonNOM - Système d'Authentification Android

Application Android développée avec **Kotlin** et **Jetpack Compose** implémentant un système d'authentification complet avec gestion des rôles.

## 📱 Fonctionnalités

### 🔐 Authentification
- **Connexion** avec nom d'utilisateur et mot de passe
- **Inscription** pour créer de nouveaux comptes utilisateur
- **Déconnexion** sécurisée
- **Validation** des champs de saisie

### 👥 Gestion des Rôles
- **Super Admin** : Accès complet à l'administration
- **User Sample** : Utilisateur standard avec accès limité

### 🛠️ Administration (Super Admin uniquement)
- **Créer des utilisateurs** User Sample
- **Supprimer des utilisateurs** individuellement
- **Supprimer tous les utilisateurs** User Sample en une fois
- **Liste en temps réel** des utilisateurs

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
│       ├── InscriptionScreen.kt
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

### 🔑 Première connexion
1. Lancez l'application
2. Utilisez le compte admin par défaut
3. Créez vos premiers utilisateurs User Sample

### 👤 En tant qu'Admin
1. **Se connecter** avec le compte admin
2. **Créer des utilisateurs** via le bouton "Créer User Sample"
3. **Gérer les utilisateurs** depuis la liste
4. **Supprimer** individuellement ou en masse

### 👨‍💻 En tant qu'Utilisateur
1. **S'inscrire** via l'écran d'inscription
2. **Se connecter** avec vos identifiants
3. **Accéder** à l'écran d'accueil utilisateur

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
- Lien vers l'inscription
- Indication du compte admin par défaut

### Écran d'Inscription
- Création de nouveaux comptes User Sample
- Validation des mots de passe
- Retour vers la connexion

### Écran d'Accueil Admin
- Menu d'administration
- Liste des utilisateurs en temps réel
- Actions de création et suppression

### Écran d'Accueil Utilisateur
- Message de bienvenue
- Interface simplifiée
- Bouton de déconnexion

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