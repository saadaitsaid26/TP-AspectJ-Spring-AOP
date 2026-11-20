# TP AOP : Programmation Orientée Aspect avec AspectJ et Spring AOP

Ce projet constitue l'activité pratique intégrale de programmation orientée aspect (AOP), réalisée dans le cadre de la formation d'ingénieur en Génie Logiciel et Systèmes Informatiques Distribués (GLSID) à l'École Normale Supérieure de l'Enseignement Technique de Mohammedia (ENSET), Université Hassan II de Casablanca.

- **Étudiant :** Saad AIT SAIDOU-ALI
- **Enseignant :** Professeur Mohamed YOUSSFI
- **Année Universitaire :** 2026–2027

---

## 1. Architecture du Projet

Le projet est structuré selon un schéma Maven multi-modules séparant clairement les deux approches :

```text
tp_aop/
├── pom.xml                               # POM parent (Java 17, gestion des dépendances)
│
├── aspectj-demo/                         # Module 1 : Tissage statique AspectJ (CTW)
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   └── java/
│       │       ├── demo/                 # Comparaison syntaxe .aj vs @Aspect
│       │       │   ├── MainAdviceDemo.java
│       │       │   └── aspects/
│       │       │       ├── FirstAspect.aj
│       │       │       └── SecondAspect.java
│       │       └── ma/enset/tpaop/aspectj/
│       │           ├── metier/           # Métier bancaire (Compte, IMetier, MetierImpl)
│       │           │   ├── Compte.java
│       │           │   ├── IMetier.java
│       │           │   └── MetierImpl.java
│       │           ├── aspects/          # LogAspect, SecurityAspect, PatchAspect
│       │           │   ├── LogAspect.java
│       │           │   ├── SecurityAspect.java
│       │           │   └── PatchAspect.java
│       │           ├── security/         # Abstraction d'authentification
│       │           │   ├── Credentials.java
│       │           │   ├── CredentialsProvider.java
│       │           │   ├── ConsoleCredentialsProvider.java
│       │           │   └── FixedCredentialsProvider.java
│       │           └── ui/               # Application console
│       │               └── BankApplication.java
│       └── test/java/                    # 9 tests unitaires et d'intégration JUnit 5
│
├── spring-aop-demo/                      # Module 2 : Tissage dynamique Spring AOP (Proxies)
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   └── java/ma/enset/tpaop/spring/
│       │       ├── aspects/              # Annotations @Log, @SecuredByAspect & Aspects
│       │       │   ├── Log.java
│       │       │   ├── SecuredByAspect.java
│       │       │   ├── LoggingAspect.java
│       │       │   ├── SecurityAspect.java
│       │       │   └── ApplicationContext.java (alias pédagogique)
│       │       ├── config/               # AppConfig (@EnableAspectJAutoProxy)
│       │       │   └── AppConfig.java
│       │       ├── metier/               # IMetier & MetierImpl (avec self-invocation)
│       │       │   ├── IMetier.java
│       │       │   └── MetierImpl.java
│       │       ├── security/             # SecurityContext (RBAC en mémoire)
│       │       │   └── SecurityContext.java
│       │       └── SpringAopApplication.java # Démonstration 4 scénarios
│       └── test/java/                    # 8 tests unitaires JUnit 5
│
├── docs/                                 # Documentation technique et pédagogique
│   ├── ANALYSE_PEDAGOGIQUE.md            # Analyse détaillée des 5 vidéos et du cours PDF
│   ├── TRACEABILITY.md                   # Matrice de traçabilité exigences/tests/rapport
│   └── RESULTATS_VALIDATION.md           # Preuves d'exécution et bilan des tests
│
└── rapport/                              # Compte rendu académique universitaire en LaTeX
    ├── main.tex
    ├── references.bib
    ├── GUIDE_CAPTURES.md                 # Protocole de réalisation des captures réelles
    ├── sections/                         # Sections modulaires en LaTeX
    └── figures/                          # Captures et illustrations du rapport
```

---

## 2. Prérequis Techniques

- **Java JDK :** OpenJDK 17 LTS (ou supérieur, compatible jusqu'à Java 21)
- **Apache Maven :** 3.8.x ou 3.9.x
- **Distribution LaTeX (pour compiler le rapport) :** MiKTeX, TeX Live ou Overleaf
- **IDE recommandé :** IntelliJ IDEA (Community ou Ultimate)

---

## 3. Compilation et Exécution des Tests

### 3.1 Compilation et validation complète (Recommandé)
À la racine du projet :

```bash
mvn clean verify
```

Cette commande compile les aspects natifs AspectJ (`.aj` et `@Aspect`), tisse le bytecode au moment de la compilation, instancie les proxys Spring et exécute l'intégralité des 17 tests JUnit 5.

### 3.2 Exécution des tests par module

Pour exécuter uniquement les tests du module AspectJ (9 tests) :
```bash
mvn test -pl aspectj-demo
```

Pour exécuter uniquement les tests du module Spring AOP (8 tests) :
```bash
mvn test -pl spring-aop-demo
```

---

## 4. Lancement des Démonstrations Applicatives

### 4.1 Module AspectJ

#### Démonstration 1 : Comparaison de Syntaxe (.aj vs @Aspect)
```bash
mvn exec:java -pl aspectj-demo -Dexec.mainClass="demo.MainAdviceDemo"
```
*Sortie attendue :*
```text
Before starting Main AspectJ Syntax...
Before starting Main @Aspect Syntax ...
Application starting ....
After starting Main @Aspect Syntax ...
After Finishing Main AspectJ Syntax...
```

#### Démonstration 2 : Application Bancaire avec Aspects (Logs, Sécurité, Patch)
```bash
mvn exec:java -pl aspectj-demo -Dexec.mainClass="ma.enset.tpaop.aspectj.ui.BankApplication" -Dexec.args="--demo"
```
*Comportement démontré :*
1. Interception de `start()` par `SecurityAspect`.
2. Saisie ou passage des identifiants (`root` / `123`).
3. Démarrage de l'application et création d'un compte.
4. Versement (journalisé par `LogAspect`).
5. Retrait valide (journalisé par `LogAspect`).
6. Retrait excessif (intercepté et refusé par `PatchAspect` avec message "Solde insuffisant").

### 4.2 Module Spring AOP

```bash
mvn exec:java -pl spring-aop-demo -Dexec.mainClass="ma.enset.tpaop.spring.SpringAopApplication"
```
*Comportement démontré :*
1. **Scénario 1 :** Utilisateur authentifié en `ADMIN` -> exécution de `process()` (journalisé et chronométré par `@Log`) et `compute()` (autorisé).
2. **Scénario 2 :** Utilisateur authentifié en `USER` -> exécution de `process()` (autorisé) puis de `compute()` (bloqué avec exception "Not Authorized").
3. **Scénario 3 :** Identifiants invalides -> exception levée dès l'authentification.
4. **Scénario 4 :** Démonstration de la limitation des proxys Spring lors d'appels internes (`self-invocation`).

---

## 5. Compilation du Rapport Académique en LaTeX

Le rapport universitaire est situé dans le dossier `rapport/`.

```bash
cd rapport
pdflatex -interaction=nonstopmode main.tex
bibtex main
pdflatex -interaction=nonstopmode main.tex
pdflatex -interaction=nonstopmode main.tex
```

Le fichier compilé produit est `rapport/rapport.pdf`.
Consultez `rapport/GUIDE_CAPTURES.md` pour le protocole précis d'insertion des captures d'écran réelles sous IntelliJ IDEA.
