package com.alfaconseil.bacprep.init;

import com.alfaconseil.bacprep.model.*;
import com.alfaconseil.bacprep.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private UserRepository userRepository;
    @Autowired private MatiereRepository matiereRepository;
    @Autowired private CoursRepository coursRepository;
    @Autowired private ExerciceRepository exerciceRepository;
    @Autowired private PlanningRepository planningRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("demo")) {
            logger.info("Données déjà initialisées.");
            return;
        }
        logger.info("Initialisation des données...");

        // Create users
        User demo = new User();
        demo.setUsername("demo");
        demo.setEmail("demo@bacprep.mr");
        demo.setPassword(passwordEncoder.encode("demo"));
        demo.setNom("Ould Ahmed");
        demo.setPrenom("Mohamed");
        demo.setSerie("C");
        demo.setRole(Role.ETUDIANT);
        demo.setActif(true);
        demo.setDateInscription(LocalDateTime.now());
        demo = userRepository.save(demo);

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@alfaconseil.mr");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setNom("Alfa");
        admin.setPrenom("Admin");
        admin.setSerie("C");
        admin.setRole(Role.ADMIN);
        admin.setActif(true);
        admin.setDateInscription(LocalDateTime.now());
        userRepository.save(admin);

        User enseignant = new User();
        enseignant.setUsername("prof");
        enseignant.setEmail("prof@bacprep.mr");
        enseignant.setPassword(passwordEncoder.encode("prof123"));
        enseignant.setNom("Ould Moussa");
        enseignant.setPrenom("Ibrahim");
        enseignant.setSerie("C");
        enseignant.setRole(Role.ENSEIGNANT);
        enseignant.setActif(true);
        enseignant.setDateInscription(LocalDateTime.now());
        userRepository.save(enseignant);

        // Create matieres
        Matiere maths = new Matiere();
        maths.setNom("Mathématiques");
        maths.setCode("MATH");
        maths.setDescription("Analyse, Algèbre, Géométrie, Probabilités - Terminales C & D");
        maths.setCouleur("#2196F3");
        maths.setIcone("calculate");
        maths.setOrdreAffichage(1);
        maths = matiereRepository.save(maths);

        Matiere physique = new Matiere();
        physique.setNom("Physique-Chimie");
        physique.setCode("PHY");
        physique.setDescription("Mécanique, Électricité, Thermodynamique, Chimie - Terminales C & D");
        physique.setCouleur("#FF5722");
        physique.setIcone("science");
        physique.setOrdreAffichage(2);
        physique = matiereRepository.save(physique);

        Matiere svt = new Matiere();
        svt.setNom("Sciences Naturelles");
        svt.setCode("SVT");
        svt.setDescription("Biologie, Génétique, Physiologie, Écologie - Terminale D");
        svt.setCouleur("#4CAF50");
        svt.setIcone("eco");
        svt.setOrdreAffichage(3);
        svt = matiereRepository.save(svt);

        // MATHS COURS
        createCoursMaths(maths);
        // PHYSIQUE COURS
        createCoursPhysique(physique);
        // SVT COURS
        createCoursSVT(svt);

        // EXERCICES QCM
        createExercicesQCM(maths, physique, svt);

        // ANNALES
        createAnnales(maths, physique);

        // PLANNING for demo user
        createPlanningDemo(demo, maths, physique, svt);

        logger.info("Données initialisées avec succès !");
    }

    private void createCoursMaths(Matiere maths) {
        Cours c1 = new Cours();
        c1.setMatiere(maths);
        c1.setNumeroChapitre(1);
        c1.setTitre("Les Suites Numériques");
        c1.setChapitre("Chapitre 1");
        c1.setType(Cours.TypeCours.COURS);
        c1.setContenu("""
# Les Suites Numériques

## 1. Définitions et Notations

Une **suite numérique** est une fonction de ℕ (ou d'une partie de ℕ) dans ℝ.

### Notations
- On note la suite : (uₙ)ₙ∈ℕ ou simplement (uₙ)
- uₙ est le **terme général** (terme de rang n)
- u₀ : terme initial, u₁ : terme de rang 1, etc.

## 2. Modes de Définition

### Définition Explicite
uₙ = f(n) pour tout n ∈ ℕ

**Exemple :** uₙ = 2n + 1 → u₀ = 1, u₁ = 3, u₂ = 5, ...

### Définition Récurrente
On donne u₀ (ou u₁) et une relation : uₙ₊₁ = f(uₙ)

**Exemple :** u₀ = 1 et uₙ₊₁ = 2uₙ + 1

## 3. Suites Arithmétiques

Une suite (uₙ) est **arithmétique** de raison r si :
**uₙ₊₁ = uₙ + r** pour tout n

### Terme général
**uₙ = u₀ + n·r** (ou uₙ = u₁ + (n-1)·r)

### Somme des termes
**Sₙ = (n+1)(u₀ + uₙ)/2** pour les termes u₀, u₁, ..., uₙ

### Exemples
- Suite 3, 7, 11, 15... → r = 4, u₀ = 3, uₙ = 3 + 4n
- Suite des entiers pairs : uₙ = 2n (r = 2)

## 4. Suites Géométriques

Une suite (uₙ) est **géométrique** de raison q ≠ 0 si :
**uₙ₊₁ = q·uₙ** pour tout n

### Terme général
**uₙ = u₀ · qⁿ**

### Somme des termes (q ≠ 1)
**Sₙ = u₀ · (1 - qⁿ⁺¹)/(1 - q)**

### Exemples
- Suite 2, 6, 18, 54... → q = 3, u₀ = 2, uₙ = 2·3ⁿ
- Suite 1, 1/2, 1/4, 1/8... → q = 1/2

## 5. Sens de Variation

Une suite est :
- **Croissante** si uₙ₊₁ - uₙ > 0 (ou uₙ₊₁/uₙ > 1 si tous positifs)
- **Décroissante** si uₙ₊₁ - uₙ < 0
- **Constante** si uₙ₊₁ = uₙ

## 6. Convergence et Limites

- **Suite convergente** : lim uₙ = L (L ∈ ℝ) quand n → +∞
- **Suite divergente** : tend vers ±∞ ou n'a pas de limite

### Théorèmes
- Si (uₙ) est croissante et majorée → elle converge
- Si (uₙ) est décroissante et minorée → elle converge
- **Théorème des gendarmes :** Si vₙ ≤ uₙ ≤ wₙ et lim vₙ = lim wₙ = L, alors lim uₙ = L
""");
        c1.setResume("Suites arithmétiques (uₙ = u₀+nr), géométriques (uₙ = u₀·qⁿ), convergence et limites.");
        c1.setDureeLecture(45);
        coursRepository.save(c1);

        Cours c2 = new Cours();
        c2.setMatiere(maths);
        c2.setNumeroChapitre(2);
        c2.setTitre("Dérivation et Applications");
        c2.setChapitre("Chapitre 2");
        c2.setType(Cours.TypeCours.COURS);
        c2.setContenu("""
# Dérivation et Applications

## 1. Définition de la Dérivée

La **dérivée** d'une fonction f en un point a est :

**f'(a) = lim[h→0] (f(a+h) - f(a)) / h**

Si cette limite existe, f est **dérivable** en a.

## 2. Tableau des Dérivées Usuelles

| Fonction f(x) | Dérivée f'(x) |
|---------------|----------------|
| k (constante) | 0 |
| xⁿ (n ∈ ℤ) | n·xⁿ⁻¹ |
| √x | 1/(2√x) |
| eˣ | eˣ |
| ln(x) | 1/x |
| sin(x) | cos(x) |
| cos(x) | -sin(x) |

## 3. Règles de Calcul

### Linéarité
(αu + βv)' = αu' + βv'

### Produit
**(u·v)' = u'·v + u·v'**

### Quotient
**(u/v)' = (u'·v - u·v') / v²** (v ≠ 0)

### Composée
**(u∘v)' = (u'∘v)·v'**

## 4. Étude des Variations

### Méthode
1. Calculer f'(x)
2. Résoudre f'(x) = 0 → trouver les points critiques
3. Établir le **tableau de signes** de f'
4. En déduire le **tableau de variations** de f

### Règle
- f'(x) > 0 sur I → f **croissante** sur I
- f'(x) < 0 sur I → f **décroissante** sur I
- f'(a) = 0 avec changement de signe → **extremum local** en a

## 5. Équation de la Tangente

La tangente à la courbe de f au point A(a, f(a)) :
**y = f'(a)·(x - a) + f(a)**

## 6. Application : Optimisation

Pour trouver le **maximum** ou **minimum** d'une fonction :
1. Calculer f'(x)
2. Résoudre f'(x) = 0
3. Vérifier que c'est bien un extremum (tableau de variations)
4. Calculer f à ce point
""");
        c2.setResume("Dérivées usuelles, règles (produit, quotient, composée), tableau de variations, tangente.");
        c2.setDureeLecture(50);
        coursRepository.save(c2);

        Cours c3 = new Cours();
        c3.setMatiere(maths);
        c3.setNumeroChapitre(3);
        c3.setTitre("Les Intégrales");
        c3.setChapitre("Chapitre 3");
        c3.setType(Cours.TypeCours.COURS);
        c3.setContenu("""
# Les Intégrales

## 1. Primitives

F est une **primitive** de f sur I si F' = f sur I.

### Primitives Usuelles
| f(x) | F(x) |
|------|-------|
| k | kx |
| xⁿ (n≠-1) | xⁿ⁺¹/(n+1) |
| 1/x | ln|x| |
| eˣ | eˣ |
| sin(x) | -cos(x) |
| cos(x) | sin(x) |

## 2. L'Intégrale Définie

**Définition :** ∫ₐᵇ f(x)dx = F(b) - F(a)

où F est une primitive de f.

**Notation :** [F(x)]ₐᵇ = F(b) - F(a)

## 3. Propriétés

- **Linéarité :** ∫ₐᵇ (αf + βg)dx = α∫ₐᵇ f·dx + β∫ₐᵇ g·dx
- **Chasles :** ∫ₐᵇ f = ∫ₐᶜ f + ∫ᶜᵇ f
- **Inégalité :** Si f ≥ 0 sur [a,b] alors ∫ₐᵇ f·dx ≥ 0

## 4. Calcul d'Aire

L'aire entre la courbe de f et l'axe des abscisses sur [a,b] :

**Si f ≥ 0 :** A = ∫ₐᵇ f(x)dx

**Si f change de signe :** A = ∫ₐᵇ |f(x)|dx

**Méthode :** Décomposer [a,b] selon le signe de f.

## 5. Intégration par Parties

**∫ u·v' dx = [u·v] - ∫ u'·v dx**

**Exemple :** ∫ x·eˣ dx
- u = x, v' = eˣ → u' = 1, v = eˣ
- ∫ x·eˣ dx = x·eˣ - ∫ eˣ dx = x·eˣ - eˣ + C = eˣ(x-1) + C
""");
        c3.setResume("Primitives usuelles, intégrale définie, calcul d'aires, intégration par parties.");
        c3.setDureeLecture(55);
        coursRepository.save(c3);

        Cours c4 = new Cours();
        c4.setMatiere(maths);
        c4.setNumeroChapitre(4);
        c4.setTitre("Probabilités et Statistiques");
        c4.setChapitre("Chapitre 4");
        c4.setType(Cours.TypeCours.COURS);
        c4.setContenu("""
# Probabilités et Statistiques

## 1. Espace de Probabilité

- **Espace fondamental Ω :** ensemble de tous les résultats possibles
- **Événement :** sous-ensemble de Ω
- **Probabilité P :** fonction de P(Ω) dans [0,1]

### Propriétés
- P(Ω) = 1, P(∅) = 0
- P(A̅) = 1 - P(A)
- P(A∪B) = P(A) + P(B) - P(A∩B)
- Si A et B incompatibles : P(A∪B) = P(A) + P(B)

## 2. Probabilités Conditionnelles

**P(A|B) = P(A∩B) / P(B)** (P(B) > 0)

### Indépendance
A et B indépendants ⟺ P(A∩B) = P(A)·P(B)

### Formule des Probabilités Totales
Si (B₁,...,Bₙ) est une partition de Ω :
**P(A) = Σ P(A|Bᵢ)·P(Bᵢ)**

## 3. Variables Aléatoires

X est une variable aléatoire → elle prend des valeurs x₁, x₂, ..., xₙ

### Espérance
**E(X) = Σ xᵢ·P(X = xᵢ)**

### Variance et Écart-type
**V(X) = E(X²) - [E(X)]²**
σ(X) = √V(X)

## 4. Loi Binomiale B(n, p)

X suit une loi binomiale si X compte le nombre de succès dans n épreuves de Bernoulli.

**P(X = k) = C(n,k) · pᵏ · (1-p)ⁿ⁻ᵏ**

- E(X) = np
- V(X) = np(1-p)
""");
        c4.setResume("Probabilités conditionnelles, indépendance, espérance, variance, loi binomiale.");
        c4.setDureeLecture(45);
        coursRepository.save(c4);

        Cours c5 = new Cours();
        c5.setMatiere(maths);
        c5.setNumeroChapitre(5);
        c5.setTitre("Fonctions Exponentielles et Logarithmes");
        c5.setChapitre("Chapitre 5");
        c5.setType(Cours.TypeCours.COURS);
        c5.setContenu("""
# Fonctions Exponentielles et Logarithmes

## 1. La Fonction Exponentielle

**Définition :** exp est l'unique fonction dérivable sur ℝ telle que :
- f' = f
- f(0) = 1

**Notation :** exp(x) = eˣ (e ≈ 2,718)

### Propriétés
- eˣ > 0 pour tout x ∈ ℝ
- eˣ⁺ʸ = eˣ · eʸ
- eˣ⁻ʸ = eˣ / eʸ
- (eˣ)ⁿ = enˣ
- Dérivée : (eˣ)' = eˣ

### Limites
- lim eˣ = +∞ quand x → +∞
- lim eˣ = 0 quand x → -∞
- lim (eˣ/xⁿ) = +∞ (croissance comparée)
- lim (xⁿ·eˣ) = 0 quand x → -∞

## 2. La Fonction Logarithme Naturel

**Définition :** ln est la fonction réciproque de exp.

ln(x) est défini pour x > 0, et ln(eˣ) = x, e^(ln x) = x.

### Propriétés
- ln(ab) = ln(a) + ln(b)
- ln(a/b) = ln(a) - ln(b)
- ln(aⁿ) = n·ln(a)
- Dérivée : (ln x)' = 1/x

### Limites
- lim ln(x) = +∞ quand x → +∞
- lim ln(x) = -∞ quand x → 0⁺
- lim (ln(x)/xⁿ) = 0 quand x → +∞ (croissance comparée)

## 3. Applications

### Résolution d'Équations
- eˣ = a → x = ln(a) (a > 0)
- ln(x) = b → x = eᵇ

### Modèles de Croissance
- Croissance exponentielle : N(t) = N₀·eᵏᵗ (k > 0)
- Décroissance : N(t) = N₀·e⁻ᵏᵗ (k > 0)
""");
        c5.setResume("Fonction exp, propriétés, limites, logarithme naturel, équations, modèles.");
        c5.setDureeLecture(40);
        coursRepository.save(c5);
    }

    private void createCoursPhysique(Matiere physique) {
        Cours c1 = new Cours();
        c1.setMatiere(physique);
        c1.setNumeroChapitre(1);
        c1.setTitre("Cinématique du Point Matériel");
        c1.setChapitre("Chapitre 1");
        c1.setType(Cours.TypeCours.COURS);
        c1.setContenu("""
# Cinématique du Point Matériel

## 1. Systèmes de Référence

La position d'un point M dépend du **référentiel** choisi.

### Référentiels courants
- **Géocentrique :** centre = centre Terre, axes vers étoiles lointaines
- **Terrestre :** lié à la Terre (le plus utilisé)
- **Héliocentrique :** centre = Soleil

## 2. Vecteur Position et Trajectoire

En coordonnées cartésiennes : **OM⃗ = x(t)·i⃗ + y(t)·j⃗ + z(t)·k⃗**

La **trajectoire** est l'ensemble des positions successives du point.

## 3. Vecteur Vitesse

**v⃗(t) = dOM⃗/dt = ẋ·i⃗ + ẏ·j⃗ + ż·k⃗**

- Composantes : vₓ = dx/dt, vy = dy/dt
- Module : v = √(vₓ² + vy²)
- **La vitesse est toujours tangente à la trajectoire**

## 4. Vecteur Accélération

**a⃗(t) = dv⃗/dt = ẍ·i⃗ + ÿ·j⃗**

## 5. Mouvements Particuliers

### Mouvement Rectiligne Uniforme (MRU)
- a⃗ = 0⃗
- v = constante
- x(t) = x₀ + v·t

### Mouvement Rectiligne Uniformément Accéléré (MRUA)
- a = constante
- v(t) = v₀ + a·t
- x(t) = x₀ + v₀·t + ½a·t²
- **v² = v₀² + 2a(x - x₀)**

## 6. Chute Libre et Tir Parabolique

Dans le champ de pesanteur (sans frottement) :
- Axe horizontal : **aₓ = 0** → mouvement uniforme
- Axe vertical : **ay = -g** (g ≈ 9,8 m/s²)

### Équations du tir oblique (angle θ, vitesse v₀)
- x(t) = v₀·cos(θ)·t
- y(t) = v₀·sin(θ)·t - ½g·t²
- vₓ(t) = v₀·cos(θ)
- vy(t) = v₀·sin(θ) - g·t

### Temps de vol et portée
- Temps de vol : T = 2v₀·sin(θ)/g
- Portée : R = v₀²·sin(2θ)/g
""");
        c1.setResume("Référentiels, vitesse, accélération, MRU, MRUA, chute libre, tir parabolique.");
        c1.setDureeLecture(50);
        coursRepository.save(c1);

        Cours c2 = new Cours();
        c2.setMatiere(physique);
        c2.setNumeroChapitre(2);
        c2.setTitre("Les Lois de Newton");
        c2.setChapitre("Chapitre 2");
        c2.setType(Cours.TypeCours.COURS);
        c2.setContenu("""
# Les Lois de Newton - Dynamique

## 1. Les Trois Lois de Newton

### 1ère Loi (Principe d'Inertie)
> "Tout corps persiste dans son état de repos ou de mouvement rectiligne uniforme si les forces qui s'exercent sur lui se compensent."

**ΣF⃗ = 0⃗ ⟺ a⃗ = 0⃗**

### 2ème Loi (Principe Fondamental de la Dynamique - PFD)
> "L'accélération d'un corps est proportionnelle à la résultante des forces."

**ΣF⃗ = m·a⃗**

- m : masse (kg)
- a⃗ : accélération (m/s²)
- ΣF⃗ : résultante des forces (N)

### 3ème Loi (Principe des Actions Réciproques)
> "Si A exerce une force F⃗ sur B, alors B exerce une force -F⃗ sur A."

## 2. Forces Usuelles

### Poids
**P⃗ = m·g⃗** (vers le bas, g = 9,8 m/s²)

### Réaction Normale
**N⃗** : perpendiculaire au support, dirigée vers le corps.

### Force de Frottement
**f⃗ = -μN·v⃗/|v⃗|** (oppose au mouvement)
- μ : coefficient de frottement cinétique

### Force de Rappel (Ressort)
**F⃗ = -k·x⃗** (k : constante de raideur en N/m)

## 3. Méthode de Résolution

1. Identifier le système et le référentiel
2. Faire l'inventaire des forces
3. Appliquer le PFD : ΣF⃗ = m·a⃗
4. Projeter sur les axes
5. Intégrer pour trouver v(t) et x(t)

## 4. Plan Incliné

Sur un plan incliné d'angle α sans frottement :
- P·sin(α) = m·a (selon le plan)
- N = m·g·cos(α) (perpendiculaire)

**a = g·sin(α)**
""");
        c2.setResume("3 lois de Newton, PFD, poids, réaction, frottement, plan incliné.");
        c2.setDureeLecture(45);
        coursRepository.save(c2);

        Cours c3 = new Cours();
        c3.setMatiere(physique);
        c3.setNumeroChapitre(3);
        c3.setTitre("Électricité - Lois des Circuits");
        c3.setChapitre("Chapitre 3");
        c3.setType(Cours.TypeCours.COURS);
        c3.setContenu("""
# Électricité - Lois des Circuits

## 1. Grandeurs Électriques

### Intensité du courant (I)
- Unité : Ampère (A)
- I = ΔQ/Δt (charge par unité de temps)

### Tension (U)
- Unité : Volt (V)
- Différence de potentiel entre deux points

### Résistance (R)
- Unité : Ohm (Ω)
- Loi d'Ohm : **U = R·I**

## 2. Lois de Kirchhoff

### Loi des Nœuds (courants)
La somme algébrique des intensités en un nœud est nulle :
**ΣI = 0** (courants entrants = courants sortants)

### Loi des Mailles (tensions)
La somme algébrique des tensions dans une maille est nulle :
**ΣU = 0**

## 3. Circuits Série et Parallèle

### Circuit Série
- Intensité identique dans tous les éléments : I = constante
- Tensions s'ajoutent : U = U₁ + U₂ + ...
- Résistance équivalente : **Req = R₁ + R₂ + ...**

### Circuit Parallèle
- Tension identique aux bornes de chaque branche : U = constante
- Intensités s'ajoutent : I = I₁ + I₂ + ...
- Résistance équivalente : **1/Req = 1/R₁ + 1/R₂ + ...**

## 4. Puissance et Énergie Électrique

### Puissance
**P = U·I = R·I² = U²/R** (en Watts)

### Énergie
**E = P·t = U·I·t** (en Joules ou Wh)

## 5. Condensateur et Bobine

### Condensateur (C en Farads)
- i(t) = C·du/dt
- Énergie stockée : **E = ½·C·U²**

### Bobine (L en Henry)
- u(t) = L·di/dt
- Énergie stockée : **E = ½·L·I²**
""");
        c3.setResume("Kirchhoff, circuits série/parallèle, puissance, condensateur, bobine.");
        c3.setDureeLecture(55);
        coursRepository.save(c3);
    }

    private void createCoursSVT(Matiere svt) {
        Cours c1 = new Cours();
        c1.setMatiere(svt);
        c1.setNumeroChapitre(1);
        c1.setTitre("La Cellule et ses Organites");
        c1.setChapitre("Chapitre 1");
        c1.setType(Cours.TypeCours.COURS);
        c1.setContenu("""
# La Cellule - Unité Structurale du Vivant

## 1. Types Cellulaires

### Cellule Procaryote (Bactéries)
- Pas de noyau membranaire
- Chromosome unique circulaire dans le nucléoïde
- Pas d'organites membraneux
- Plus petite (1-10 μm)

### Cellule Eucaryote (Animaux, Plantes, Champignons)
- Noyau délimité par une **double membrane nucléaire**
- Organites membraneux spécialisés
- Plus grande (10-100 μm)

## 2. Structure de la Cellule Eucaryote

### Le Noyau
- Contient l'ADN (information génétique)
- Entouré par la **membrane nucléaire** (enveloppe)
- Contient le **nucléole** (synthèse des ARNr)
- Pores nucléaires : échanges noyau-cytoplasme

### Les Mitochondries
- Siège de la **respiration cellulaire** (production ATP)
- Double membrane : externe lisse, interne plissée (crêtes)
- Possèdent leur propre ADN
- ATP = "monnaie énergétique" de la cellule

### Les Chloroplastes (cellules végétales)
- Siège de la **photosynthèse**
- Double membrane + thylakoïdes (empilés en granum)
- Contiennent la chlorophylle
- Possèdent leur propre ADN

### Le Réticulum Endoplasmique (RE)
- **RE granuleux** (avec ribosomes) : synthèse des protéines
- **RE lisse** : synthèse des lipides

### L'Appareil de Golgi
- Tri et modification des protéines
- Formation de vésicules de sécrétion

### Les Ribosomes
- Synthèse des protéines (traduction)
- Libres dans cytoplasme ou fixés au RE

## 3. Membrane Plasmique

**Modèle de la Mosaïque Fluide :**
- Bicouche phospholipidique
- Protéines transmembranaires et périphériques
- Semi-perméable

### Transport membranaire
- **Diffusion simple :** molécules sans énergie
- **Transport actif :** avec ATP (pompes ioniques)
- **Endocytose :** entrée par invagination
""");
        c1.setResume("Procaryotes vs eucaryotes, noyau, mitochondries, chloroplastes, membrane plasmique.");
        c1.setDureeLecture(45);
        coursRepository.save(c1);

        Cours c2 = new Cours();
        c2.setMatiere(svt);
        c2.setNumeroChapitre(2);
        c2.setTitre("La Division Cellulaire - Mitose et Méiose");
        c2.setChapitre("Chapitre 2");
        c2.setType(Cours.TypeCours.COURS);
        c2.setContenu("""
# Division Cellulaire : Mitose et Méiose

## 1. Le Cycle Cellulaire

Le cycle cellulaire comprend :
1. **Interphase** (G1 + S + G2) : croissance et réplication ADN
2. **Division** (mitose ou méiose)

### Phase S (Synthèse)
- Réplication semi-conservative de l'ADN
- À la fin : 2 chromatides par chromosome (2n chromosomes)

## 2. La Mitose (Division Somatique)

**But :** Produire 2 cellules filles identiques à la cellule mère (2n → 2n)

### Les 4 Phases de la Mitose

**Prophase :**
- Condensation de la chromatine → chromosomes visibles
- Disparition du nucléole et de l'enveloppe nucléaire
- Formation du fuseau mitotique

**Métaphase :**
- Chromosomes alignés à l'équateur cellulaire (plaque métaphasique)
- Attachement des fibres du fuseau aux centromères
- Chromosomes les plus condensés (idéaux pour caryotype)

**Anaphase :**
- Séparation des chromatides sœurs
- Migration vers les pôles opposés

**Télophase :**
- Décondensation des chromosomes
- Reconstitution de l'enveloppe nucléaire
- **Cytocinèse :** division du cytoplasme → 2 cellules filles

## 3. La Méiose (Division Sexuée)

**But :** Produire 4 cellules haploïdes (2n → 4 cellules à n chromosomes)

### Méiose I (Réductionnelle)
- Prophase I : **crossing-over** (échanges entre chromosomes homologues) → diversité génétique
- Métaphase I : paires d'homologues à l'équateur
- Anaphase I : séparation des chromosomes homologues (pas des chromatides)
- Télophase I → 2 cellules à n chromosomes (bivalents)

### Méiose II (Équationnelle)
- Identique à la mitose
- Séparation des chromatides sœurs
- → 4 cellules haploïdes

### Brassage génétique
- **Inter-chromosomique :** ségrégation indépendante
- **Intra-chromosomique :** crossing-over

## 4. Comparaison Mitose / Méiose

| Caractère | Mitose | Méiose |
|-----------|--------|--------|
| Nombre de divisions | 1 | 2 |
| Cellules filles | 2 | 4 |
| Ploïdie | 2n → 2n | 2n → n |
| Rôle | Croissance | Reproduction |
""");
        c2.setResume("Cycle cellulaire, mitose (4 phases), méiose (brassage génétique), comparaison.");
        c2.setDureeLecture(50);
        coursRepository.save(c2);

        Cours c3 = new Cours();
        c3.setMatiere(svt);
        c3.setNumeroChapitre(3);
        c3.setTitre("Génétique et Hérédité");
        c3.setChapitre("Chapitre 3");
        c3.setType(Cours.TypeCours.COURS);
        c3.setContenu("""
# Génétique - Transmission des Caractères

## 1. Vocabulaire Essentiel

- **Gène :** portion d'ADN codant pour une protéine (caractère)
- **Allèle :** forme différente d'un gène
- **Locus :** emplacement du gène sur le chromosome
- **Génotype :** constitution génétique (ex: Aa)
- **Phénotype :** caractère observable
- **Homozygote :** deux allèles identiques (AA ou aa)
- **Hétérozygote :** deux allèles différents (Aa)

## 2. Lois de Mendel

### 1ère Loi (Uniformité des F1)
Le croisement P1 (AA) × P2 (aa) donne des F1 tous identiques (Aa).

### 2ème Loi (Ségrégation des allèles)
À la méiose, les allèles se séparent.
F1 (Aa) × F1 (Aa) → F2 : 1/4 AA + 2/4 Aa + 1/4 aa

**Phénotypiquement** (si A dominant sur a) : 3/4 [A] + 1/4 [a]

### 3ème Loi (Indépendance des gènes)
Deux gènes sur des chromosomes différents se transmettent indépendamment.
Exemple : AaBb × AaBb → 9:3:3:1

## 3. Dominance et Récessivité

- **Allèle dominant (A) :** s'exprime en hétérozygote
- **Allèle récessif (a) :** ne s'exprime qu'en homozygote
- **Codominance :** les deux allèles s'expriment (ex: groupe sanguin AB)

## 4. Hérédité Liée au Sexe

Chromosomes sexuels : XX (femme), XY (homme)
Les gènes portés par X ont une transmission particulière.

**Exemple :** Daltonisme (récessif lié à X)
- Femme daltonienne : XᵈXᵈ
- Homme daltonien : XᵈY
- Femme conductrice : XᴰXᵈ

## 5. Crossing-Over et Liaison

Deux gènes sur le même chromosome sont **liés**.
Les crossing-over créent des **recombinaisons**.

Fréquence de recombinaison = distance génétique (en cM).
""");
        c3.setResume("Génotype/phénotype, lois de Mendel, dominance, hérédité liée au sexe.");
        c3.setDureeLecture(55);
        coursRepository.save(c3);
    }

    private void createExercicesQCM(Matiere maths, Matiere physique, Matiere svt) {
        // QCM Maths - Suites
        Exercice qcmSuites = new Exercice();
        qcmSuites.setMatiere(maths);
        qcmSuites.setTitre("QCM - Suites Numériques");
        qcmSuites.setEnonce("Testez vos connaissances sur les suites arithmétiques et géométriques");
        qcmSuites.setType(Exercice.TypeExercice.QCM);
        qcmSuites.setDifficulte(Exercice.Difficulte.MOYEN);
        qcmSuites.setDureeMinutes(15);
        qcmSuites.setPointsTotal(5);
        qcmSuites = exerciceRepository.save(qcmSuites);

        addQuestion(qcmSuites, 1,
                "La suite (uₙ) est définie par u₀ = 3 et uₙ₊₁ = uₙ + 5. Quel est u₄ ?",
                "Calcul du terme général d'une suite arithmétique de raison 5.",
                new String[]{"23", "25", "20", "28"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmSuites, 2,
                "Une suite géométrique de raison q=2 a u₀=1. Quelle est la somme u₀+u₁+u₂+u₃ ?",
                "Somme des termes d'une suite géométrique: S = u₀(1-qⁿ)/(1-q).",
                new String[]{"15", "7", "14", "16"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmSuites, 3,
                "La suite uₙ = 3n - 1 est :",
                "Une suite arithmétique a la forme uₙ = u₀ + nr.",
                new String[]{"Arithmétique de raison 3", "Géométrique de raison 3", "Ni arithmétique ni géométrique", "Arithmétique de raison -1"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmSuites, 4,
                "Si une suite (uₙ) est croissante et majorée, alors :",
                "Théorème de convergence des suites monotones bornées.",
                new String[]{"Elle converge", "Elle diverge vers +∞", "On ne peut rien conclure", "Elle est géométrique"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmSuites, 5,
                "La suite de Fibonacci : u₁=1, u₂=1, uₙ₊₂=uₙ₊₁+uₙ. Quel est u₆ ?",
                "Calculer successivement: u₃=2, u₄=3, u₅=5, u₆=?",
                new String[]{"8", "13", "5", "10"},
                new boolean[]{true, false, false, false});

        // QCM Physique - Newton
        Exercice qcmNewton = new Exercice();
        qcmNewton.setMatiere(physique);
        qcmNewton.setTitre("QCM - Lois de Newton");
        qcmNewton.setEnonce("Évaluez vos connaissances sur les lois de Newton et la dynamique");
        qcmNewton.setType(Exercice.TypeExercice.QCM);
        qcmNewton.setDifficulte(Exercice.Difficulte.MOYEN);
        qcmNewton.setDureeMinutes(15);
        qcmNewton.setPointsTotal(5);
        qcmNewton = exerciceRepository.save(qcmNewton);

        addQuestion(qcmNewton, 1,
                "Une voiture de masse 1000 kg accélère à 2 m/s². Quelle est la force motrice nette ?",
                "Application directe du PFD: F = m·a",
                new String[]{"2000 N", "500 N", "1000 N", "4000 N"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmNewton, 2,
                "Un objet en chute libre (sans résistance de l'air) depuis 5 m. Quelle est sa vitesse en bas ? (g=10)",
                "Utiliser v² = 2gh : v² = 2×10×5 = 100, v = 10 m/s",
                new String[]{"10 m/s", "5 m/s", "√50 m/s", "100 m/s"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmNewton, 3,
                "Selon la 1ère loi de Newton, si la résultante des forces est nulle, alors :",
                "Principe d'inertie : équilibre = repos ou MRU.",
                new String[]{"L'objet est en repos ou MRU", "L'objet accélère", "L'objet est forcément en repos", "L'objet change de direction"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmNewton, 4,
                "Sur un plan incliné à 30°, sans frottement, l'accélération est (g=10) :",
                "a = g·sin(α) = 10×sin(30°) = 10×0.5 = 5 m/s²",
                new String[]{"5 m/s²", "8,7 m/s²", "10 m/s²", "2,5 m/s²"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmNewton, 5,
                "La 3ème loi de Newton (actions réciproques) signifie que :",
                "Action-réaction : forces égales, opposées, sur des corps différents.",
                new String[]{"Les forces d'interaction sont égales et opposées sur deux corps différents", "Un corps exerce une force sur lui-même", "La résultante est toujours nulle", "Les forces sont dans le même sens"},
                new boolean[]{true, false, false, false});

        // QCM SVT - Génétique
        Exercice qcmGenetique = new Exercice();
        qcmGenetique.setMatiere(svt);
        qcmGenetique.setTitre("QCM - Génétique Mendélienne");
        qcmGenetique.setEnonce("Testez vos connaissances en génétique et transmission des caractères");
        qcmGenetique.setType(Exercice.TypeExercice.QCM);
        qcmGenetique.setDifficulte(Exercice.Difficulte.MOYEN);
        qcmGenetique.setDureeMinutes(15);
        qcmGenetique.setPointsTotal(5);
        qcmGenetique = exerciceRepository.save(qcmGenetique);

        addQuestion(qcmGenetique, 1,
                "Le croisement AA × aa donne en F1 :",
                "Première loi de Mendel : uniformité des hybrides F1.",
                new String[]{"Tous Aa", "1/2 AA + 1/2 aa", "Tous AA", "1/4 AA + 2/4 Aa + 1/4 aa"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmGenetique, 2,
                "En F2, le croisement Aa × Aa donne le ratio phénotypique (A dominant) :",
                "Deuxième loi de Mendel : ratio 3:1",
                new String[]{"3 [A] : 1 [a]", "1:2:1", "tous [A]", "1:1"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmGenetique, 3,
                "Le daltonisme (lié à X, récessif) : un homme daltonien a pour génotype :",
                "Les gènes liés à X : l'homme n'a qu'un allèle (hemizygote).",
                new String[]{"XᵈY", "XᴰXᵈ", "XᴰY", "XᵈXᵈ"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmGenetique, 4,
                "La mitose produit :",
                "La mitose est la division somatique (croissance).",
                new String[]{"2 cellules diploïdes identiques", "4 cellules haploïdes", "4 cellules diploïdes", "2 cellules haploïdes"},
                new boolean[]{true, false, false, false});

        addQuestion(qcmGenetique, 5,
                "Le crossing-over se produit pendant :",
                "Les échanges entre homologues ont lieu en prophase I de la méiose.",
                new String[]{"La prophase I de la méiose", "L'anaphase de la mitose", "La métaphase II", "L'interphase"},
                new boolean[]{true, false, false, false});

        // Exercice analytique Maths
        Exercice exoAnalyse = new Exercice();
        exoAnalyse.setMatiere(maths);
        exoAnalyse.setTitre("Étude Complète d'une Fonction");
        exoAnalyse.setEnonce("""
**Exercice : Étude de f(x) = x³ - 3x + 2**

Soit la fonction f définie sur ℝ par : **f(x) = x³ - 3x + 2**

1. Calculer f'(x) et déterminer les extrema locaux
2. Dresser le tableau de variations de f
3. Déterminer l'équation de la tangente en x₀ = 0
4. Trouver les solutions de f(x) = 0 (factoriser)
5. Calculer ∫₀¹ f(x)dx
""");
        exoAnalyse.setCorrection("""
**Correction :**

**1. Dérivée et extrema :**
f'(x) = 3x² - 3 = 3(x² - 1) = 3(x-1)(x+1)

f'(x) = 0 ⟺ x = -1 ou x = 1
- f'(x) > 0 si x < -1 ou x > 1 → f croissante
- f'(x) < 0 si -1 < x < 1 → f décroissante

- Maximum local en x = -1 : f(-1) = -1 + 3 + 2 = **4**
- Minimum local en x = 1 : f(1) = 1 - 3 + 2 = **0**

**2. Tableau de variations :**
```
x    | -∞    -1    1    +∞
f'(x)|  +     0    -   0    +
f(x) |  ↗     4    ↘   0    ↗
```

**3. Tangente en x₀ = 0 :**
f(0) = 0 - 0 + 2 = 2
f'(0) = 0 - 3 = -3

Équation : **y = -3x + 2**

**4. Solutions de f(x) = 0 :**
On sait que f(1) = 0, donc (x-1) est un facteur.

f(x) = (x-1)(x² + x - 2) = (x-1)(x-1)(x+2) = **(x-1)²(x+2)**

Solutions : **x = 1** (double) et **x = -2**

**5. Intégrale :**
∫₀¹ f(x)dx = [x⁴/4 - 3x²/2 + 2x]₀¹
= (1/4 - 3/2 + 2) - 0
= 1/4 - 6/4 + 8/4
= **3/4**
""");
        exoAnalyse.setType(Exercice.TypeExercice.EXERCICE);
        exoAnalyse.setDifficulte(Exercice.Difficulte.DIFFICILE);
        exoAnalyse.setDureeMinutes(30);
        exoAnalyse.setPointsTotal(20);
        exerciceRepository.save(exoAnalyse);

        // Annale Physique
        Exercice annalePhys = new Exercice();
        annalePhys.setMatiere(physique);
        annalePhys.setTitre("Annale 2022 - Mécanique");
        annalePhys.setEnonce("""
**ANNALE BAC MAURITANIE 2022 - Physique**

**Problème : Chute libre et tir parabolique**

Un ballon est lancé depuis le haut d'un immeuble de hauteur h = 20 m avec une vitesse initiale horizontale v₀ = 10 m/s. On prendra g = 10 m/s².

**Partie A : Équations du mouvement**
1. Écrire les équations horaires x(t) et y(t)
2. Donner l'équation de la trajectoire y = f(x)

**Partie B : Caractéristiques du mouvement**
3. Calculer la durée de la chute
4. Calculer la distance horizontale parcourue (portée)
5. Calculer la vitesse d'impact (module et angle)
""");
        annalePhys.setCorrection("""
**CORRECTION DÉTAILLÉE**

**Système de référence :** Origine au pied de l'immeuble (sol), axe x horizontal, axe y vertical vers le haut.

**Conditions initiales :** x₀ = 0, y₀ = 20 m (hauteur de l'immeuble), vₓ₀ = 10 m/s, vy₀ = 0

**Partie A :**

**1. Équations horaires :**
Bilan des forces : seul le poids P⃗ = m·g⃗ (sans frottement)
D'après le PFD : aₓ = 0 et ay = -g

En intégrant :
- **x(t) = v₀·t = 10t**
- **y(t) = y₀ - ½g·t² = 20 - 5t²**

**2. Équation de la trajectoire :**
De x(t) : t = x/10
En substituant : y = 20 - 5(x/10)² = **20 - x²/20**

C'est une parabole.

**Partie B :**

**3. Durée de la chute :**
Le ballon touche le sol quand y = 0 :
0 = 20 - 5t²
5t² = 20
t² = 4
**t = 2 s**

**4. Portée :**
x = 10 × 2 = **20 m**

**5. Vitesse d'impact :**
- vₓ = 10 m/s (constante)
- vy = -g·t = -10 × 2 = -20 m/s

Module : v = √(vₓ² + vy²) = √(100 + 400) = √500 = **10√5 ≈ 22,4 m/s**

Angle avec l'horizontale : tan(θ) = |vy|/vₓ = 20/10 = 2
**θ = arctan(2) ≈ 63,4°**
""");
        annalePhys.setType(Exercice.TypeExercice.ANNALE);
        annalePhys.setDifficulte(Exercice.Difficulte.DIFFICILE);
        annalePhys.setAnneeBac(2022);
        annalePhys.setDureeMinutes(45);
        annalePhys.setPointsTotal(20);
        exerciceRepository.save(annalePhys);
    }

    private void addQuestion(Exercice exercice, int ordre, String enonce, String explication,
                              String[] options, boolean[] correctes) {
        Question question = new Question();
        question.setExercice(exercice);
        question.setEnonce(enonce);
        question.setExplication(explication);
        question.setOrdre(ordre);
        question.setPoints(1.0);

        if (exercice.getQuestions() == null) {
            exercice.setQuestions(new ArrayList<>());
        }

        question.setOptions(new ArrayList<>());
        for (int i = 0; i < options.length; i++) {
            OptionReponse opt = new OptionReponse();
            opt.setQuestion(question);
            opt.setTexte(options[i]);
            opt.setEstCorrecte(correctes[i]);
            opt.setOrdre(i);
            question.getOptions().add(opt);
        }
        exercice.getQuestions().add(question);
        exerciceRepository.save(exercice);
    }

    private void createAnnales(Matiere maths, Matiere physique) {
        Exercice annale2023 = new Exercice();
        annale2023.setMatiere(maths);
        annale2023.setTitre("Annale 2023 - Analyse Mathématique");
        annale2023.setEnonce("""
**ANNALE BAC MAURITANIE 2023 - Mathématiques Série C**

**Exercice 1 : Étude de fonction (8 points)**

Soit f la fonction définie sur ℝ par : **f(x) = (x+1)e⁻ˣ**

1. Calculer les limites de f en ±∞
2. Calculer f'(x) et étudier le signe de f'(x)
3. Dresser le tableau de variations
4. Déterminer l'équation de la tangente au point d'abscisse x=0
5. Calculer ∫₀¹ f(x)dx

**Exercice 2 : Probabilités (6 points)**

Une urne contient 3 boules rouges et 7 boules bleues.
On tire successivement 2 boules SANS remise.
1. Calculer P(2 rouges)
2. Calculer P(au moins 1 bleue)
3. Sachant que la 1ère est rouge, P(2ème rouge) ?
""");
        annale2023.setCorrection("""
**CORRECTION COMPLÈTE**

**Exercice 1 :**

**1. Limites :**
- lim f(x) quand x→+∞ : xeˣ → +∞ mais e⁻ˣ → 0
  En fait (x+1)e⁻ˣ = (x+1)/eˣ → 0 (croissance comparée) : **lim = 0**
- lim f(x) quand x→-∞ : (x+1) → -∞ et e⁻ˣ → +∞
  **lim = -∞**

**2. Dérivée :**
f'(x) = (x+1)'·e⁻ˣ + (x+1)·(e⁻ˣ)' = e⁻ˣ + (x+1)·(-e⁻ˣ) = e⁻ˣ(1 - x - 1) = -xe⁻ˣ

Signe de f'(x) :
- e⁻ˣ > 0 toujours
- f'(x) = -xe⁻ˣ : signe opposé à x
  - x > 0 → f'(x) < 0
  - x < 0 → f'(x) > 0
  - x = 0 → f'(x) = 0

**3. Tableau de variations :**
```
x    | -∞         0         +∞
f'   |     +      0      -
f    |    ↗      1      ↘ → 0
```
Maximum en x = 0 : f(0) = (0+1)·e⁰ = **1**

**4. Tangente en x = 0 :**
f(0) = 1, f'(0) = 0
Équation : **y = 1** (tangente horizontale)

**5. Intégrale :**
Intégration par parties : u = (x+1), v' = e⁻ˣ → u' = 1, v = -e⁻ˣ
∫₀¹(x+1)e⁻ˣdx = [-(x+1)e⁻ˣ]₀¹ + ∫₀¹e⁻ˣdx
= [-2e⁻¹ + 1] + [-e⁻ˣ]₀¹
= -2/e + 1 + (-1/e + 1)
= **2 - 3/e ≈ 0,896**

**Exercice 2 :**

**1. P(2 rouges) :**
P = (3/10) × (2/9) = **6/90 = 1/15 ≈ 0,067**

**2. P(au moins 1 bleue) :**
P = 1 - P(aucune bleue) = 1 - P(2 rouges) = 1 - 1/15 = **14/15**

**3. P(2ème rouge | 1ère rouge) :**
Sachant que la 1ère est rouge, il reste 2 rouges sur 9 boules :
**P = 2/9**
""");
        annale2023.setType(Exercice.TypeExercice.ANNALE);
        annale2023.setDifficulte(Exercice.Difficulte.DIFFICILE);
        annale2023.setAnneeBac(2023);
        annale2023.setDureeMinutes(120);
        annale2023.setPointsTotal(20);
        exerciceRepository.save(annale2023);
    }

    private void createPlanningDemo(User demo, Matiere maths, Matiere physique, Matiere svt) {
        LocalDate today = LocalDate.now();

        Planning p1 = new Planning();
        p1.setUser(demo);
        p1.setMatiere(maths);
        p1.setTitre("Révision Suites Numériques");
        p1.setDescription("Revoir les suites arithmétiques et géométriques, faire 5 exercices");
        p1.setDateDebut(today);
        p1.setDateFin(today);
        p1.setHeureDebut("08:00");
        p1.setHeureFin("10:00");
        p1.setCouleur("#2196F3");
        p1.setComplete(false);
        planningRepository.save(p1);

        Planning p2 = new Planning();
        p2.setUser(demo);
        p2.setMatiere(physique);
        p2.setTitre("Exercices Cinématique");
        p2.setDescription("Résoudre les annales 2021 et 2022 sur la cinématique");
        p2.setDateDebut(today);
        p2.setDateFin(today);
        p2.setHeureDebut("14:00");
        p2.setHeureFin("16:00");
        p2.setCouleur("#FF5722");
        p2.setComplete(false);
        planningRepository.save(p2);

        Planning p3 = new Planning();
        p3.setUser(demo);
        p3.setMatiere(maths);
        p3.setTitre("QCM Dérivation");
        p3.setDescription("Faire le quiz de dérivation et analyser les erreurs");
        p3.setDateDebut(today.plusDays(1));
        p3.setDateFin(today.plusDays(1));
        p3.setHeureDebut("09:00");
        p3.setHeureFin("10:30");
        p3.setCouleur("#2196F3");
        p3.setComplete(false);
        planningRepository.save(p3);

        Planning p4 = new Planning();
        p4.setUser(demo);
        p4.setMatiere(svt);
        p4.setTitre("Fiche Génétique");
        p4.setDescription("Créer une fiche synthèse sur les lois de Mendel");
        p4.setDateDebut(today.plusDays(1));
        p4.setDateFin(today.plusDays(1));
        p4.setHeureDebut("15:00");
        p4.setHeureFin("17:00");
        p4.setCouleur("#4CAF50");
        p4.setComplete(false);
        planningRepository.save(p4);

        Planning p5 = new Planning();
        p5.setUser(demo);
        p5.setMatiere(physique);
        p5.setTitre("Annale Complète Physique 2022");
        p5.setDescription("Faire l'annale entière en conditions d'examen (3h)");
        p5.setDateDebut(today.plusDays(3));
        p5.setDateFin(today.plusDays(3));
        p5.setHeureDebut("08:00");
        p5.setHeureFin("11:00");
        p5.setCouleur("#FF5722");
        p5.setComplete(false);
        p5.setRappel(true);
        planningRepository.save(p5);
    }
}
