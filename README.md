# Projet Médiathèque - Gestion d'emprunts

Application de gestion de médiathèque développée en Java suivant les principes de Domain-Driven Design (DDD) et les bonnes pratiques de programmation orientée objet.

## Table des matières

- [Présentation](#présentation)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Lancement](#lancement)
- [Utilisation](#utilisation)
- [Tests](#tests)
- [Structure du projet](#structure-du-projet)
- [Fonctionnalités](#fonctionnalités)

## Présentation

Cette application permet de gérer une médiathèque avec les fonctionnalités suivantes :
- Gestion des membres (actifs/inactifs)
- Gestion des œuvres (Livres et DVDs)
- Gestion des emprunts avec quota maximum
- Interface en ligne de commande (CLI) interactive

## Architecture

Cette application suit une architecture **DDD (Domain-Driven Design) pragmatique** avec une séparation stricte des responsabilités, sans framework ni magie.

### Vision globale

```
CLI ──▶ Service ──▶ Repositories ──▶ Domain
           ▲
         Tests
```

**Principe directeur :**
- **Le domaine ne dépend de rien** (couche la plus pure)
- **Les règles métier vivent dans le service**, pas dans la CLI
- **Les invariants sont garantis** par les constructeurs et méthodes du domaine

### Organisation des packages

```
exam/
├── domain/          # Cœur métier PUR (aucune dépendance externe)
│   ├── oeuvre/
│   │   ├── Oeuvre.java
│   │   ├── Livre.java
│   │   └── Dvd.java
│   ├── membre/
│   │   ├── Membre.java
│   │   └── StatutMembre.java
│   └── emprunt/
│       └── Emprunt.java
│
├── repo/            # Contrats (interfaces) - DIP (Dependency Inversion Principle)
│   ├── OeuvreRepository.java
│   ├── MembreRepository.java
│   └── EmpruntRepository.java
│
├── repo.impl/       # Implémentations en mémoire
│   ├── InMemoryOeuvreRepository.java
│   ├── InMemoryMembreRepository.java
│   └── InMemoryEmpruntRepository.java
│
├── service/         # Règles métier et orchestration
│   ├── MediathequeService.java
│   ├── EmpruntPolicy.java
│   └── MediathequeServiceTest.java
│
├── cli/             # I/O seulement (couche mince)
│   └── MediathequeCLI.java
│
├── util/            # Utilitaires
│   └── IdGenerator.java
│
└── Main.java        # Point d'entrée
```

### Principes architecturaux

#### 1. **Domain Layer** (`domain/`)
- **Rôle** : Entités métier pures, sans dépendances externes
- **Responsabilités** :
  - Encapsulation des données
  - Validation des invariants métier
  - Logique métier intrinsèque aux entités
- **Règle** : Aucune dépendance vers les autres couches

#### 2. **Repository Layer** (`repo/` et `repo.impl/`)
- **`repo/`** : Interfaces définissant les contrats de persistance (DIP)
- **`repo.impl/`** : Implémentations concrètes (ici en mémoire)
- **Avantage** : Facilement remplaçable par une implémentation base de données sans modifier le service

#### 3. **Service Layer** (`service/`)
- **Rôle** : Orchestration et règles métier complexes
- **Responsabilités** :
  - Coordination entre repositories
  - Application des politiques métier (ex: `EmpruntPolicy`)
  - Validation des règles transversales
- **Testabilité** : Facilement testable via injection des repositories

#### 4. **CLI Layer** (`cli/`)
- **Rôle** : Interface utilisateur minimale
- **Responsabilités** :
  - Lecture des entrées utilisateur
  - Affichage des résultats
  - Délégation au service pour toute logique métier
- **Règle** : Aucune logique métier dans la CLI

### Encapsulation et invariants

- Les **constructeurs** garantissent la création d'objets valides
- Les **méthodes du domaine** préservent les invariants
- Les **règles métier** sont explicites et testables

### Avantages de cette architecture

1. **Testabilité** : Chaque couche peut être testée indépendamment
2. **Maintenabilité** : Séparation claire des responsabilités
3. **Évolutivité** : Facile d'ajouter de nouvelles fonctionnalités
4. **Respect des principes SOLID** :
   - **S**ingle Responsibility : Chaque classe a une responsabilité unique
   - **O**pen/Closed : Extension via interfaces
   - **L**iskov Substitution : Polymorphisme respecté
   - **I**nterface Segregation : Interfaces ciblées
   - **D**ependency Inversion : Dépendances vers les abstractions

## Prérequis

- **Java 11** ou supérieur
- **Maven 3.6+** (pour la compilation et les tests)
- Un terminal/console pour l'interface en ligne de commande

### Vérification des prérequis

```bash
# Vérifier Java
java -version

# Vérifier Maven
mvn -version
```

## Installation

1. **Cloner ou télécharger le projet**

```bash
git clone <url-du-repo>
cd EXAMEN-3H---Projet-M-diath-que-PROGRAMMING-PARADIGMS-COURSE-ESIEE-IT-mathis_lebel
```

2. **Compiler le projet**

```bash
mvn clean compile
```

Cette commande va :
- Télécharger les dépendances (JUnit 5 pour les tests)
- Compiler le code source
- Créer les fichiers `.class` dans `target/classes`

## Lancement

### Méthode 1 : Avec Maven (recommandé)

```bash
mvn exec:java -Dexec.mainClass="exam.Main"
```

### Méthode 2 : Directement avec Java

```bash
# Compiler d'abord
mvn clean compile

# Lancer l'application
java -cp "target/classes:$(mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout)" exam.Main
```

### Méthode 3 : Depuis un IDE

1. Ouvrir le projet dans votre IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Localiser la classe `exam.Main`
3. Exécuter la méthode `main()`

## Utilisation

### Démarrage

Au lancement, l'application :
1. Affiche un message de bienvenue
2. Initialise automatiquement des données de test :
   - 3 membres (2 actifs, 1 inactif)
   - 4 œuvres (2 livres, 2 DVDs)
3. Affiche le menu principal

### Menu principal

```
╔════════════════════════════════════╗
║        MENU PRINCIPAL              ║
╠════════════════════════════════════╣
║ 1. Lister les œuvres disponibles  ║
║ 2. Emprunter une œuvre            ║
║ 3. Rendre une œuvre               ║
║ 4. Lister les emprunts actifs     ║
║ 5. Ajouter une œuvre              ║
║ 6. Ajouter un membre              ║
║ 0. Quitter                        ║
╚════════════════════════════════════╝
```

### Options détaillées

#### Option 1 : Lister les œuvres disponibles
- Affiche toutes les œuvres disponibles (non empruntées)
- Pour chaque œuvre, affiche :
  - ID, Type (Livre/DVD), Titre
  - Pour les livres : Auteur et ISBN
  - Pour les DVDs : Réalisateur et Durée

#### Option 2 : Emprunter une œuvre
- Demande l'identifiant du membre
- Demande l'identifiant de l'œuvre
- Vérifie les conditions :
  - Le membre existe et est actif
  - L'œuvre existe et est disponible
  - Le membre n'a pas atteint son quota (3 emprunts maximum)
- Crée l'emprunt et affiche son ID
- Marque l'œuvre comme indisponible

**Messages d'erreur possibles :**
- "Le membre avec l'ID X n'existe pas"
- "Le membre avec l'ID X est inactif"
- "L'œuvre avec l'ID X n'existe pas"
- "L'œuvre avec l'ID X n'est pas disponible"
- "Le membre a atteint son quota d'emprunts actifs"

#### Option 3 : Rendre une œuvre
- Demande l'identifiant de l'emprunt
- Vérifie que l'emprunt existe et n'a pas déjà été retourné
- Marque l'emprunt comme retourné
- Marque l'œuvre comme disponible
- Affiche un message de confirmation

**Messages d'erreur possibles :**
- "L'emprunt avec l'ID X n'existe pas"
- "L'emprunt avec l'ID X a déjà été retourné"

#### Option 4 : Lister les emprunts actifs d'un membre
- Demande l'identifiant du membre
- Affiche tous les emprunts actifs (non retournés) du membre
- Pour chaque emprunt : ID, ID de l'œuvre, date d'emprunt
- Affiche un message si aucun emprunt actif

#### Option 5 : Ajouter une œuvre
- Demande le type (1 = Livre, 2 = DVD)
- Pour un Livre : ID, Titre, Auteur, ISBN
- Pour un DVD : ID, Titre, Réalisateur, Durée (en minutes)
- Valide les données et crée l'œuvre
- Affiche un message de confirmation

#### Option 6 : Ajouter un membre
- Demande l'identifiant et le nom
- Crée un nouveau membre (actif par défaut)
- Affiche un message de confirmation

#### Option 0 : Quitter
- Ferme l'application proprement

### Gestion des erreurs

L'application est **robuste** et ne plante jamais :
- Validation de toutes les entrées utilisateur
- Gestion des `NumberFormatException` pour les nombres
- Messages d'erreur clairs et explicites
- Redemande en cas d'entrée invalide
- Le menu se réaffiche après chaque action

## Tests

### Exécuter tous les tests

```bash
mvn test
```

### Résultats attendus

```
Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Couverture des tests

Les tests couvrent :

1. **Tests des préconditions d'emprunt** :
   - Membre inexistant
   - Membre inactif
   - Quota atteint
   - Œuvre inexistante
   - Œuvre déjà empruntée
   - Identifiants invalides

2. **Tests du parcours nominal** :
   - Emprunt réussi
   - Retour réussi
   - Cycle complet (emprunter → rendre → réemprunter)

3. **Tests des postconditions** :
   - Double retour impossible
   - Libération du quota après retour

4. **Tests paramétrés** :
   - Différents nombres d'emprunts pour tester le quota

### Structure des tests

Tous les tests suivent le pattern **AAA (Arrange-Act-Assert)** :
- **Arrange** : Préparation du contexte (repositories, service, données)
- **Act** : Exécution de l'action à tester
- **Assert** : Vérification des résultats

Exemple :
```java
@Test
void emprunter_quandMembreInexistant_doitLeverIllegalStateException() {
    // Arrange : Le membre n'existe pas
    long membreIdInexistant = 999;
    long oeuvreId = livre1.getId();

    // Act & Assert : Vérifier que l'exception est levée
    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> service.emprunter(membreIdInexistant, oeuvreId)
    );

    // Assert : Vérifier le message d'erreur
    assertTrue(exception.getMessage().contains("n'existe pas"));
}
```

## Structure du projet

### Fichiers principaux

- **`exam/Main.java`** : Point d'entrée de l'application
- **`exam/cli/MediathequeCLI.java`** : Interface en ligne de commande
- **`exam/service/MediathequeService.java`** : Service métier principal
- **`exam/service/EmpruntPolicy.java`** : Politique d'emprunt (quota = 3)
- **`exam/service/MediathequeServiceTest.java`** : Tests unitaires

### Domain (Cœur métier)

- **`exam/domain/oeuvre/Oeuvre.java`** : Classe abstraite pour les œuvres
- **`exam/domain/oeuvre/Livre.java`** : Classe concrète pour les livres
- **`exam/domain/oeuvre/Dvd.java`** : Classe concrète pour les DVDs
- **`exam/domain/membre/Membre.java`** : Représente un membre
- **`exam/domain/membre/StatutMembre.java`** : Enum pour le statut (ACTIF/INACTIF)
- **`exam/domain/emprunt/Emprunt.java`** : Représente un emprunt

### Repositories

- **Interfaces** : `exam/repo/*Repository.java`
- **Implémentations** : `exam/repo/impl/InMemory*Repository.java`

### Utilitaires

- **`exam/util/IdGenerator.java`** : Générateur d'identifiants uniques

## Fonctionnalités

### Règles métier

1. **Quota d'emprunts** : Un membre peut emprunter au maximum **3 œuvres** simultanément
2. **Disponibilité** : Une œuvre ne peut être empruntée que si elle est disponible
3. **Membre actif** : Seuls les membres actifs peuvent emprunter
4. **Unicité** : Les identifiants doivent être strictement positifs
5. **Validation** : Tous les champs obligatoires sont validés (titre, nom, etc.)

### Invariants du domaine

- **Oeuvre** : `id > 0`, `titre != null && !titre.isBlank()`, `disponible` initialisé à `true`
- **Membre** : `id > 0`, `nom != null && !nom.isBlank()`, `statut` initialisé à `ACTIF`
- **Emprunt** : `id > 0`, `idMembre > 0`, `idOeuvre > 0`, `dateEmprunt != null`, `dateRetour` initialement `null`

### Persistance

- **En mémoire** : Utilisation de `Map<Long, T>` pour le stockage
- **Collections non modifiables** : Toutes les méthodes `find*` retournent `List.copyOf()`
- **Pas de synchronisation** : Contexte mono-thread assumé

## Configuration

### Maven (`pom.xml`)

- **Java** : Version 11
- **JUnit** : Version 5.10.0
- **Encodage** : UTF-8

### Structure des sources

- **Source directory** : `exam/`
- **Test directory** : `exam/` (fichiers `*Test.java` exclus de la compilation principale)

## Exemples d'utilisation

### Scénario 1 : Emprunter un livre

```
1. Choisir l'option 2 (Emprunter)
2. Entrer l'ID du membre : 1
3. Entrer l'ID de l'œuvre : 1
4. Emprunt créé avec succès ! ID de l'emprunt : 1
```

### Scénario 2 : Rendre une œuvre

```
1. Choisir l'option 3 (Rendre)
2. Entrer l'ID de l'emprunt : 1
3. Œuvre rendue avec succès !
```

### Scénario 3 : Vérifier les emprunts actifs

```
1. Choisir l'option 4 (Lister les emprunts actifs)
2. Entrer l'ID du membre : 1
3. Affichage de la liste des emprunts actifs
```

## Dépannage

### Problème : "The import org.junit cannot be resolved"

**Solution** : Recharger le projet Maven dans votre IDE
- VS Code/Cursor : `Cmd+Shift+P` → "Java: Reload Projects"
- IntelliJ : Clic droit sur `pom.xml` → "Maven" → "Reload Project"

### Problème : Erreurs de compilation

**Solution** :
```bash
mvn clean compile
```

### Problème : Les tests ne passent pas

**Solution** :
```bash
mvn clean test
```

## Statistiques du projet

- **Lignes de code** : ~2000+
- **Classes** : 20+
- **Tests** : 21 tests unitaires
- **Couverture** : Tous les scénarios critiques testés

## Concepts Java utilisés

- **Encapsulation** : Champs privés, méthodes publiques contrôlées
- **Héritage** : `Livre` et `Dvd` héritent de `Oeuvre`
- **Polymorphisme** : Traitement uniforme des œuvres
- **Collections** : `Map`, `List`, `Optional`
- **Streams** : Pour les filtres et transformations
- **Exceptions** : `IllegalArgumentException`, `IllegalStateException`
- **JUnit 5** : Tests unitaires avec annotations
- **Pattern AAA** : Structure des tests

## Licence

Voir le fichier `LICENSE` pour plus d'informations.

## Auteur

**mathis_lebel**

Projet développé dans le cadre du cours de Programming Paradigms - ESIEE IT

---

**Note** : Ce projet est une démonstration pédagogique des principes de DDD et de la programmation orientée objet en Java.
