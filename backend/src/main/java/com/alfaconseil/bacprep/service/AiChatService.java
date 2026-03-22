package com.alfaconseil.bacprep.service;

import com.alfaconseil.bacprep.model.ChatMessage;
import com.alfaconseil.bacprep.model.Matiere;
import com.alfaconseil.bacprep.model.User;
import com.alfaconseil.bacprep.repository.ChatMessageRepository;
import com.alfaconseil.bacprep.repository.MatiereRepository;
import com.alfaconseil.bacprep.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class AiChatService {
    private static final Logger logger = LoggerFactory.getLogger(AiChatService.class);

    @Value("${app.anthropic.api-key:}")
    private String apiKey;

    @Value("${app.anthropic.model:claude-sonnet-4-6}")
    private String model;

    @Value("${app.anthropic.api-url:https://api.anthropic.com/v1/messages}")
    private String apiUrl;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String SYSTEM_PROMPT =
        "Tu es un assistant pédagogique expert pour le Baccalauréat mauritanien séries C et D. " +
        "Tu aides les élèves en Mathématiques, Physique-Chimie et Sciences de la Vie et de la Terre (SVT). " +
        "Réponds toujours en français, de façon claire, structurée et pédagogique. " +
        "Utilise des exemples concrets, des formules explicites et des conseils méthodologiques. " +
        "Structure tes réponses avec des titres, des étapes numérotées et des récapitulatifs.";

    public String chat(String username, String message, Long matiereId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Matiere matiere = matiereId != null ? matiereRepository.findById(matiereId).orElse(null) : null;
        String matiereContext = matiere != null ? " [Contexte: " + matiere.getNom() + "]" : "";
        String fullMessage = message + matiereContext;

        String reponse;
        if (isApiKeyConfigured()) {
            reponse = callAnthropicApi(fullMessage);
        } else {
            reponse = generateFallbackResponse(message, matiere);
        }

        ChatMessage chatMsg = new ChatMessage();
        chatMsg.setUser(user);
        chatMsg.setMessage(message);
        chatMsg.setReponse(reponse);
        chatMsg.setMatiere(matiere);
        chatMessageRepository.save(chatMsg);

        return reponse;
    }

    private boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.isEmpty() && !apiKey.equals("YOUR_API_KEY_HERE");
    }

    @SuppressWarnings("unchecked")
    private String callAnthropicApi(String message) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 1024);
            requestBody.put("system", SYSTEM_PROMPT);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", message);
            messages.add(userMsg);
            requestBody.put("messages", messages);

            WebClient client = webClientBuilder.build();
            Map<String, Object> response = client.post()
                    .uri(apiUrl)
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .header("content-type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
                if (!content.isEmpty()) {
                    return (String) content.get(0).get("text");
                }
            }
        } catch (Exception e) {
            logger.error("Erreur API Anthropic: {}", e.getMessage());
        }
        return generateFallbackResponse(message, null);
    }

    private String generateFallbackResponse(String message, Matiere matiere) {
        String msg = message.toLowerCase();

        // ==================== MATHÉMATIQUES ====================

        if (msg.contains("dérivé") || msg.contains("derivé") || msg.contains("dériver") || msg.contains("derivation")) {
            return "**La Dérivation - Mathématiques Terminale C/D**\n\n" +
                "La dérivée d'une fonction f en un point a mesure le taux de variation instantané de f en ce point. " +
                "Géométriquement, f'(a) représente la pente de la tangente à la courbe de f au point d'abscisse a.\n\n" +
                "**Formules fondamentales :**\n" +
                "- (xⁿ)' = n·xⁿ⁻¹\n" +
                "- (eˣ)' = eˣ\n" +
                "- (ln x)' = 1/x   (x > 0)\n" +
                "- (sin x)' = cos x\n" +
                "- (cos x)' = -sin x\n" +
                "- (constante)' = 0\n\n" +
                "**Règles de calcul :**\n" +
                "- Somme : (u + v)' = u' + v'\n" +
                "- Produit : (u·v)' = u'·v + u·v'\n" +
                "- Quotient : (u/v)' = (u'·v - u·v') / v²\n" +
                "- Composition : (u∘v)' = (u'∘v) · v'\n\n" +
                "**Application : Étude de variations**\n" +
                "1. Calculer f'(x)\n" +
                "2. Résoudre f'(x) = 0 pour trouver les extrema\n" +
                "3. Étudier le signe de f'(x) : f croissante si f'(x) > 0, décroissante si f'(x) < 0\n" +
                "4. Dresser le tableau de variations\n\n" +
                "**Exemple :** f(x) = x³ - 3x + 2\n" +
                "f'(x) = 3x² - 3 = 3(x² - 1) = 3(x-1)(x+1)\n" +
                "f'(x) = 0 ⟹ x = -1 ou x = 1\n" +
                "f est croissante sur ]-∞, -1[ et ]1, +∞[, décroissante sur ]-1, 1[\n\n" +
                "Pose-moi un exercice spécifique et je te guidera étape par étape !";
        }

        if (msg.contains("intégr") || msg.contains("primitiv") || msg.contains("calcul intégral")) {
            return "**L'Intégration - Mathématiques Terminale C/D**\n\n" +
                "L'intégrale est l'opération réciproque de la dérivation. " +
                "Graphiquement, ∫ₐᵇ f(x)dx représente l'aire algébrique entre la courbe et l'axe des abscisses.\n\n" +
                "**Primitives usuelles :**\n" +
                "- ∫xⁿ dx = xⁿ⁺¹/(n+1) + C   (n ≠ -1)\n" +
                "- ∫(1/x) dx = ln|x| + C\n" +
                "- ∫eˣ dx = eˣ + C\n" +
                "- ∫sin x dx = -cos x + C\n" +
                "- ∫cos x dx = sin x + C\n" +
                "- ∫eᵃˣ dx = (1/a)·eᵃˣ + C\n\n" +
                "**Calcul d'une intégrale définie :**\n" +
                "∫ₐᵇ f(x)dx = [F(x)]ₐᵇ = F(b) - F(a)\n" +
                "où F est une primitive de f.\n\n" +
                "**Calcul d'aire :**\n" +
                "Si f(x) ≥ 0 sur [a,b] : Aire = ∫ₐᵇ f(x)dx\n" +
                "Si f change de signe : Aire = ∫|f(x)|dx (décomposer en sous-intervalles)\n\n" +
                "**Exemple :** Calculer ∫₀² (3x² - 2x) dx\n" +
                "Primitive : F(x) = x³ - x²\n" +
                "∫₀² (3x² - 2x) dx = [x³ - x²]₀² = (8 - 4) - (0 - 0) = 4\n\n" +
                "**Intégration par parties :** ∫u·v' = [u·v] - ∫u'·v\n" +
                "Utile pour ∫x·eˣ dx, ∫x·ln x dx, etc.\n\n" +
                "Donne-moi un exercice à résoudre !";
        }

        if (msg.contains("limite") || msg.contains("lim ") || msg.contains("asymptote")) {
            return "**Les Limites de Fonctions - Terminale C/D**\n\n" +
                "La limite d'une fonction f en un point a (ou à l'infini) décrit le comportement de f quand x s'approche de a.\n\n" +
                "**Limites usuelles à connaître :**\n" +
                "- lim(xⁿ) = +∞  quand x → +∞  (n > 0)\n" +
                "- lim(eˣ) = +∞  quand x → +∞\n" +
                "- lim(eˣ) = 0   quand x → -∞\n" +
                "- lim(ln x) = +∞ quand x → +∞\n" +
                "- lim(ln x) = -∞ quand x → 0⁺\n" +
                "- lim(sin x / x) = 1 quand x → 0\n\n" +
                "**Formes indéterminées et méthodes :**\n" +
                "- ∞/∞ → Diviser numérateur et dénominateur par le terme dominant\n" +
                "- ∞ - ∞ → Factoriser ou mettre au même dénominateur\n" +
                "- 0/0 → Factoriser, simplifier ou règle de l'Hôpital\n" +
                "- 0 × ∞ → Transformer en 0/0 ou ∞/∞\n\n" +
                "**Croissances comparées (essentielles au Bac) :**\n" +
                "- eˣ >> xⁿ >> ln x  quand x → +∞\n" +
                "- lim(eˣ/xⁿ) = +∞  et  lim(xⁿ·e⁻ˣ) = 0  quand x → +∞\n" +
                "- lim(ln x / xⁿ) = 0  quand x → +∞\n\n" +
                "**Exemple :** lim (3x² - 2x + 1) / (x² + 5)  quand x → +∞\n" +
                "On divise par x² : lim (3 - 2/x + 1/x²) / (1 + 5/x²) = 3/1 = 3\n\n" +
                "Asymptote horizontale : y = 3\n\n" +
                "Quel exercice de limite veux-tu résoudre ?";
        }

        if (msg.contains("suite") || msg.contains("récurrence") || msg.contains("arithmétique") || msg.contains("géométrique")) {
            return "**Les Suites Numériques - Terminale C/D**\n\n" +
                "Une suite est une fonction définie sur ℕ. On note uₙ le terme de rang n.\n\n" +
                "**Suite arithmétique :**\n" +
                "- uₙ₊₁ = uₙ + r  (r = raison)\n" +
                "- Terme général : uₙ = u₀ + n·r\n" +
                "- Somme : S = n·(u₀ + uₙ₋₁)/2\n\n" +
                "**Suite géométrique :**\n" +
                "- uₙ₊₁ = q·uₙ  (q = raison)\n" +
                "- Terme général : uₙ = u₀·qⁿ\n" +
                "- Somme (q ≠ 1) : S = u₀·(1 - qⁿ)/(1 - q)\n\n" +
                "**Convergence :**\n" +
                "- Suite arithmétique : diverge si r ≠ 0\n" +
                "- Suite géométrique : converge vers 0 si |q| < 1, diverge si |q| > 1\n\n" +
                "**Raisonnement par récurrence :**\n" +
                "1. Initialisation : vérifier pour n = 0 (ou n = 1)\n" +
                "2. Hérédité : supposer vrai pour n, montrer que c'est vrai pour n+1\n" +
                "3. Conclusion\n\n" +
                "**Exemple :** Suite définie par u₀ = 3 et uₙ₊₁ = 2uₙ + 1\n" +
                "En posant vₙ = uₙ + 1, on obtient vₙ₊₁ = 2vₙ → suite géométrique de raison 2\n" +
                "vₙ = v₀·2ⁿ = 4·2ⁿ = 2ⁿ⁺² → uₙ = 2ⁿ⁺² - 1\n\n" +
                "Présente-moi ton exercice de suite !";
        }

        if (msg.contains("probabilité") || msg.contains("probabilite") || msg.contains("combinatoire") || msg.contains("dénombrement")) {
            return "**Probabilités - Terminale C/D**\n\n" +
                "Les probabilités mesurent la vraisemblance d'un événement.\n\n" +
                "**Dénombrement :**\n" +
                "- Arrangements : Aⁿₚ = n! / (n-p)!\n" +
                "- Combinaisons : C(n,p) = n! / (p! × (n-p)!)\n" +
                "- C(n,0) = C(n,n) = 1\n" +
                "- C(n,p) = C(n, n-p)\n\n" +
                "**Probabilités conditionnelles :**\n" +
                "- P(A∩B) = P(A) × P(B|A)\n" +
                "- P(B|A) = P(A∩B) / P(A)\n" +
                "- Formule des probabilités totales : P(B) = P(B|A)·P(A) + P(B|Ā)·P(Ā)\n" +
                "- Théorème de Bayes : P(A|B) = P(B|A)·P(A) / P(B)\n\n" +
                "**Variables aléatoires :**\n" +
                "- Espérance : E(X) = Σ xᵢ·P(X=xᵢ)\n" +
                "- Variance : V(X) = E(X²) - [E(X)]²\n" +
                "- Écart-type : σ = √V(X)\n\n" +
                "**Loi binomiale :** X ~ B(n, p)\n" +
                "- P(X=k) = C(n,k)·pᵏ·(1-p)ⁿ⁻ᵏ\n" +
                "- E(X) = np,  V(X) = np(1-p)\n\n" +
                "**Exemple :** On tire 3 cartes d'un jeu de 52. P(avoir au moins 1 as) ?\n" +
                "P(aucun as) = C(48,3)/C(52,3) = 17296/22100 ≈ 0.783\n" +
                "P(au moins 1 as) = 1 - 0.783 ≈ 0.217\n\n" +
                "Donne-moi ton exercice de probabilités !";
        }

        if (msg.contains("logarithme") || msg.contains("ln ") || msg.contains("log ") || msg.contains("log(")) {
            return "**Logarithme Népérien - Terminale C/D**\n\n" +
                "Le logarithme népérien (ln) est la fonction réciproque de l'exponentielle.\n\n" +
                "**Définition :** ln(x) est défini pour x > 0\n" +
                "ln(e) = 1,  ln(1) = 0,  ln(e^a) = a\n\n" +
                "**Propriétés algébriques :**\n" +
                "- ln(a·b) = ln(a) + ln(b)\n" +
                "- ln(a/b) = ln(a) - ln(b)\n" +
                "- ln(aⁿ) = n·ln(a)\n" +
                "- ln(√a) = (1/2)·ln(a)\n\n" +
                "**Dérivée et variations :**\n" +
                "- (ln x)' = 1/x  (x > 0)\n" +
                "- (ln u)' = u'/u\n" +
                "- ln est croissante sur ]0, +∞[\n\n" +
                "**Limites :**\n" +
                "- lim ln(x) = -∞  quand x → 0⁺\n" +
                "- lim ln(x) = +∞  quand x → +∞\n" +
                "- lim x·ln(x) = 0  quand x → 0⁺\n" +
                "- lim ln(x)/x = 0  quand x → +∞\n\n" +
                "**Équations avec ln :**\n" +
                "Pour résoudre ln(f(x)) = k : f(x) = eᵏ (et vérifier f(x) > 0)\n\n" +
                "**Exemple :** Résoudre ln(2x-1) = ln(x+3)\n" +
                "2x - 1 = x + 3  ⟹  x = 4\n" +
                "Vérification : 2(4)-1 = 7 > 0 ✓\n\n" +
                "Pose-moi tes exercices sur le logarithme !";
        }

        if (msg.contains("exponentiel") || msg.contains("e^") || msg.contains("eˣ")) {
            return "**Fonction Exponentielle - Terminale C/D**\n\n" +
                "La fonction exponentielle eˣ est l'unique fonction égale à sa propre dérivée et valant 1 en 0.\n\n" +
                "**Propriétés fondamentales :**\n" +
                "- (eˣ)' = eˣ\n" +
                "- e⁰ = 1,  e¹ = e ≈ 2.718\n" +
                "- eˣ > 0 pour tout x ∈ ℝ\n" +
                "- eˣ⁺ʸ = eˣ · eʸ\n" +
                "- eˣ⁻ʸ = eˣ / eʸ\n" +
                "- (eˣ)ⁿ = eⁿˣ\n" +
                "- e⁻ˣ = 1/eˣ\n\n" +
                "**Variations :**\n" +
                "- eˣ est strictement croissante sur ℝ\n" +
                "- lim eˣ = 0 quand x → -∞\n" +
                "- lim eˣ = +∞ quand x → +∞\n\n" +
                "**Fonction f(x) = eᵃˣ⁺ᵇ :**\n" +
                "- f'(x) = a·eᵃˣ⁺ᵇ\n" +
                "- Même signe que a partout\n\n" +
                "**Résoudre eˢ = eᵗ :**  s = t  (bijectivité)\n" +
                "**Résoudre eˢ > eᵗ :**  s > t  (croissance)\n\n" +
                "**Exemple :** Étudier f(x) = x·eˣ\n" +
                "f'(x) = eˣ + x·eˣ = eˣ(1+x)\n" +
                "f'(x) > 0 ⟺ 1+x > 0 ⟺ x > -1\n" +
                "Minimum en x = -1 : f(-1) = -1/e\n\n" +
                "Donne-moi un exercice !";
        }

        if (msg.contains("équation") || msg.contains("second degré") || msg.contains("discriminant") || msg.contains("racine")) {
            return "**Équations - Terminale C/D**\n\n" +
                "**Équation du second degré : ax² + bx + c = 0**\n\n" +
                "Discriminant : Δ = b² - 4ac\n" +
                "- Si Δ > 0 : deux racines x₁ = (-b-√Δ)/(2a) et x₂ = (-b+√Δ)/(2a)\n" +
                "- Si Δ = 0 : racine double x₀ = -b/(2a)\n" +
                "- Si Δ < 0 : pas de racine réelle\n\n" +
                "**Relations coefficients-racines :**\n" +
                "- x₁ + x₂ = -b/a\n" +
                "- x₁ × x₂ = c/a\n" +
                "- ax² + bx + c = a(x-x₁)(x-x₂)\n\n" +
                "**Équations avec valeur absolue :**\n" +
                "|f(x)| = k  ⟺  f(x) = k ou f(x) = -k  (k ≥ 0)\n\n" +
                "**Équations trigonométriques :**\n" +
                "- sin(x) = a  ⟺  x = arcsin(a) + 2kπ  ou  x = π - arcsin(a) + 2kπ\n" +
                "- cos(x) = a  ⟺  x = ±arccos(a) + 2kπ\n\n" +
                "**Exemple :** 2x² - 7x + 3 = 0\n" +
                "Δ = 49 - 24 = 25  ⟹  √Δ = 5\n" +
                "x₁ = (7-5)/4 = 1/2  ;  x₂ = (7+5)/4 = 3\n" +
                "Vérification : x₁+x₂ = 7/2 = -(-7)/2 ✓  ;  x₁×x₂ = 3/2 = 3/2 ✓\n\n" +
                "Quel type d'équation veux-tu résoudre ?";
        }

        if (msg.contains("matrice") || msg.contains("déterminant") || msg.contains("système linéaire") || msg.contains("gauss")) {
            return "**Matrices et Systèmes Linéaires - Terminale C**\n\n" +
                "**Opérations sur les matrices :**\n" +
                "- Addition : terme à terme (matrices de même taille)\n" +
                "- Multiplication par un scalaire : k×(aᵢⱼ) = (k×aᵢⱼ)\n" +
                "- Produit : (AB)ᵢⱼ = Σₖ aᵢₖ × bₖⱼ (dimensions compatibles !)\n\n" +
                "**Matrice inverse (ordre 2) :**\n" +
                "Pour A = [[a,b],[c,d]], det(A) = ad - bc\n" +
                "Si det(A) ≠ 0 : A⁻¹ = (1/det(A)) × [[d,-b],[-c,a]]\n\n" +
                "**Résolution d'un système par matrices :**\n" +
                "AX = B  ⟹  X = A⁻¹B\n\n" +
                "**Méthode de Gauss (pivot) :**\n" +
                "1. Écrire la matrice augmentée [A|B]\n" +
                "2. Faire des opérations élémentaires sur les lignes\n" +
                "3. Obtenir une forme échelonnée\n" +
                "4. Remonter pour trouver les inconnues\n\n" +
                "**Exemple :**\n" +
                "Système : 2x + y = 5  ;  x - y = 1\n" +
                "A = [[2,1],[1,-1]],  B = [[5],[1]]\n" +
                "det(A) = -2 - 1 = -3\n" +
                "A⁻¹ = (-1/3)×[[-1,-1],[-1,2]] = [[1/3, 1/3],[1/3, -2/3]]\n" +
                "X = A⁻¹B = [[2],[1]]  ⟹  x=2, y=1\n\n" +
                "Donne-moi un exercice sur les matrices !";
        }

        if (msg.contains("trigonomét") || msg.contains("trigo") || msg.contains("sin") || msg.contains("cos") || msg.contains("tan")) {
            return "**Trigonométrie - Terminale C/D**\n\n" +
                "**Valeurs à connaître par cœur :**\n" +
                "| Angle | 0 | π/6 | π/4 | π/3 | π/2 |\n" +
                "| sin   | 0 | 1/2 | √2/2 | √3/2 | 1 |\n" +
                "| cos   | 1 | √3/2 | √2/2 | 1/2 | 0 |\n" +
                "| tan   | 0 | 1/√3 | 1 | √3 | - |\n\n" +
                "**Identités fondamentales :**\n" +
                "- sin²x + cos²x = 1\n" +
                "- 1 + tan²x = 1/cos²x\n" +
                "- cos(2x) = cos²x - sin²x = 1 - 2sin²x = 2cos²x - 1\n" +
                "- sin(2x) = 2 sin x cos x\n\n" +
                "**Formules d'addition :**\n" +
                "- sin(a+b) = sin(a)cos(b) + cos(a)sin(b)\n" +
                "- cos(a+b) = cos(a)cos(b) - sin(a)sin(b)\n\n" +
                "**Formules de linéarisation :**\n" +
                "- cos²x = (1 + cos 2x) / 2\n" +
                "- sin²x = (1 - cos 2x) / 2\n\n" +
                "**Résolution d'équations trig :**\n" +
                "sin x = 1/2 ⟹ x = π/6 + 2kπ ou x = 5π/6 + 2kπ\n\n" +
                "Quel exercice de trigo souhaites-tu résoudre ?";
        }

        if (msg.contains("géométrie") || msg.contains("vecteur") || msg.contains("plan") || msg.contains("espace") || msg.contains("coordonnées")) {
            return "**Géométrie dans l'Espace - Terminale C/D**\n\n" +
                "**Vecteurs dans l'espace :**\n" +
                "Si A(x₁,y₁,z₁) et B(x₂,y₂,z₂) :\n" +
                "- Vecteur AB = (x₂-x₁, y₂-y₁, z₂-z₁)\n" +
                "- ||AB|| = √[(x₂-x₁)² + (y₂-y₁)² + (z₂-z₁)²]\n" +
                "- Milieu M = ((x₁+x₂)/2, (y₁+y₂)/2, (z₁+z₂)/2)\n\n" +
                "**Produit scalaire :**\n" +
                "u⃗·v⃗ = x₁x₂ + y₁y₂ + z₁z₂ = ||u⃗||·||v⃗||·cos θ\n" +
                "u⃗ ⊥ v⃗ ⟺ u⃗·v⃗ = 0\n\n" +
                "**Équation d'un plan :**\n" +
                "ax + by + cz + d = 0  avec n⃗(a,b,c) vecteur normal\n\n" +
                "**Équation d'une droite :**\n" +
                "Passant par A(x₀,y₀,z₀) de direction u⃗(a,b,c) :\n" +
                "x = x₀ + at, y = y₀ + bt, z = z₀ + ct  (t ∈ ℝ)\n\n" +
                "**Distance d'un point à un plan :**\n" +
                "d(M₀, plan) = |ax₀+by₀+cz₀+d| / √(a²+b²+c²)\n\n" +
                "Donne-moi un exercice de géométrie !";
        }

        // ==================== PHYSIQUE-CHIMIE ====================

        if (msg.contains("cinématique") || msg.contains("mouvement") || msg.contains("vitesse") || msg.contains("accélération") || msg.contains("trajectoire")) {
            return "**Cinématique - Physique Terminale C/D**\n\n" +
                "La cinématique étudie les mouvements sans s'occuper des causes.\n\n" +
                "**Mouvement Rectiligne Uniforme (MRU) :**\n" +
                "- Vitesse : v = constante  (a = 0)\n" +
                "- Position : x(t) = x₀ + v·t\n\n" +
                "**Mouvement Rectiligne Uniformément Accéléré (MRUA) :**\n" +
                "- Accélération : a = constante\n" +
                "- Vitesse : v(t) = v₀ + a·t\n" +
                "- Position : x(t) = x₀ + v₀·t + (1/2)·a·t²\n" +
                "- Relation v-x : v² = v₀² + 2·a·(x - x₀)\n\n" +
                "**Chute libre (sans frottement) :**\n" +
                "- Accélération : g = 9,8 m/s² (vers le bas)\n" +
                "- vy(t) = v₀y - g·t  ;  y(t) = y₀ + v₀y·t - (1/2)·g·t²\n\n" +
                "**Mouvement parabolique :**\n" +
                "- Horizontal : x(t) = v₀·cos(α)·t  (MRU)\n" +
                "- Vertical : y(t) = v₀·sin(α)·t - (1/2)·g·t²  (MRUA)\n" +
                "- Équation de la trajectoire : y = x·tan(α) - g·x²/(2v₀²·cos²α)\n\n" +
                "**Méthode de résolution :**\n" +
                "1. Définir le repère (origine, axes)\n" +
                "2. Identifier le type de mouvement\n" +
                "3. Écrire les équations horaires\n" +
                "4. Résoudre selon la question posée\n\n" +
                "Sur quel problème de cinématique as-tu besoin d'aide ?";
        }

        if (msg.contains("dynamique") || msg.contains("newton") || msg.contains("force") || msg.contains("poids") || msg.contains("frottement")) {
            return "**Dynamique - Lois de Newton - Physique**\n\n" +
                "**Les 3 Lois de Newton :**\n\n" +
                "**1ère loi (Inertie) :**\n" +
                "Si ΣF⃗ = 0⃗, alors le corps est en repos ou en MRU\n\n" +
                "**2ème loi (Fondamentale) :**\n" +
                "ΣF⃗ = m·a⃗\n" +
                "Composantes : ΣFx = m·ax  ;  ΣFy = m·ay\n\n" +
                "**3ème loi (Réaction) :**\n" +
                "FAB = -FBA (forces d'action-réaction, opposées)\n\n" +
                "**Forces usuelles :**\n" +
                "- Poids : P = m·g  (vers le bas)\n" +
                "- Réaction normale : N  (perpendiculaire au support)\n" +
                "- Frottement : f = μ·N  (s'opposant au mouvement)\n" +
                "- Tension : T  (le long du fil, vers le centre)\n\n" +
                "**Méthode d'un problème de dynamique :**\n" +
                "1. Identifier le système (corps étudié)\n" +
                "2. Faire le bilan des forces\n" +
                "3. Appliquer ΣF⃗ = m·a⃗\n" +
                "4. Écrire les équations par composante\n" +
                "5. Résoudre le système\n\n" +
                "**Exemple : Plan incliné (angle α, sans frottement)**\n" +
                "Forces : P (poids), N (réaction normale)\n" +
                "Axe x (plan) : P·sin α = m·a  ⟹  a = g·sin α\n" +
                "Axe y (⊥ plan) : N - P·cos α = 0  ⟹  N = m·g·cos α\n\n" +
                "Décris ton problème de dynamique !";
        }

        if (msg.contains("énergie") || msg.contains("energie") || msg.contains("travail") || msg.contains("puissance") || msg.contains("cinétique") || msg.contains("potentiel")) {
            return "**Énergie et Travail - Physique Terminale C/D**\n\n" +
                "**Travail d'une force :**\n" +
                "W(F⃗) = F·d·cos θ  (θ = angle entre F⃗ et déplacement)\n" +
                "- W > 0 : force motrice\n" +
                "- W < 0 : force résistante\n" +
                "- W = 0 : force perpendiculaire au déplacement\n\n" +
                "**Énergie cinétique :**\n" +
                "Ec = (1/2)·m·v²  (en Joules)\n" +
                "Théorème : W_total = ΔEc = Ec_final - Ec_initial\n\n" +
                "**Énergie potentielle de pesanteur :**\n" +
                "Ep = m·g·h  (h = hauteur par rapport à un niveau de référence)\n" +
                "W(P⃗) = -ΔEp = -(Ep_final - Ep_initial)\n\n" +
                "**Conservation de l'énergie mécanique :**\n" +
                "Em = Ec + Ep = constante  (sans frottement)\n" +
                "(1/2)mv₁² + mgh₁ = (1/2)mv₂² + mgh₂\n\n" +
                "**Puissance :**\n" +
                "P = W/t  (Watts = J/s)\n" +
                "P_instantanée = F⃗·v⃗ = F·v·cos θ\n\n" +
                "**Rendement :**\n" +
                "η = W_utile / W_total  (0 ≤ η ≤ 1)\n\n" +
                "Quel exercice d'énergie veux-tu traiter ?";
        }

        if (msg.contains("électricité") || msg.contains("circuit") || msg.contains("résistance") || msg.contains("tension") || msg.contains("courant") || msg.contains("ohm")) {
            return "**Électricité - Physique Terminale C/D**\n\n" +
                "**Loi d'Ohm :**\n" +
                "U = R·I  (U en Volts, R en Ohms, I en Ampères)\n\n" +
                "**Résistances en série :**\n" +
                "R_éq = R₁ + R₂ + R₃ + ...\n" +
                "Même courant partout : I₁ = I₂ = I\n" +
                "Tensions : U = U₁ + U₂\n\n" +
                "**Résistances en parallèle :**\n" +
                "1/R_éq = 1/R₁ + 1/R₂\n" +
                "Même tension : U₁ = U₂ = U\n" +
                "Courants : I = I₁ + I₂\n\n" +
                "**Puissance électrique :**\n" +
                "P = U·I = R·I² = U²/R  (en Watts)\n" +
                "Énergie : E = P·t  (en Joules ou kWh)\n\n" +
                "**Condensateur :**\n" +
                "q = C·u  (C en Farads)\n" +
                "i = C·(du/dt)\n" +
                "Énergie : Ec = (1/2)·C·u²\n\n" +
                "**Circuit RC :**\n" +
                "Charge : u(t) = E·(1 - e^(-t/RC))\n" +
                "Décharge : u(t) = U₀·e^(-t/RC)\n" +
                "Constante de temps : τ = RC\n\n" +
                "Décris ton circuit pour que je t'aide !";
        }

        if (msg.contains("thermodynamique") || msg.contains("chaleur") || msg.contains("température") || msg.contains("gaz parfait")) {
            return "**Thermodynamique - Physique Terminale C/D**\n\n" +
                "**Gaz parfait :**\n" +
                "PV = nRT\n" +
                "- P : pression (Pa)\n" +
                "- V : volume (m³)\n" +
                "- n : quantité de matière (mol)\n" +
                "- R = 8,314 J/(mol·K)\n" +
                "- T : température (Kelvin = °C + 273)\n\n" +
                "**Premier principe de la thermodynamique :**\n" +
                "ΔU = W + Q\n" +
                "- ΔU : variation d'énergie interne\n" +
                "- W : travail reçu par le gaz\n" +
                "- Q : chaleur reçue\n\n" +
                "**Transformations classiques :**\n" +
                "- Isotherme (T = cte) : ΔU = 0  ⟹  Q = -W\n" +
                "- Isochore (V = cte) : W = 0  ⟹  ΔU = Q\n" +
                "- Isobare (P = cte) : W = -P·ΔV  ;  Q = nCpΔT\n" +
                "- Adiabatique (Q = 0) : ΔU = W\n\n" +
                "**Deuxième principe :**\n" +
                "La chaleur se transfert spontanément du corps chaud vers le corps froid.\n" +
                "Entropie : ΔS ≥ 0 pour un système isolé.\n\n" +
                "**Machines thermiques :**\n" +
                "Rendement Carnot : η = 1 - T_froide/T_chaude\n\n" +
                "Quel problème de thermo veux-tu résoudre ?";
        }

        if (msg.contains("optique") || msg.contains("lentille") || msg.contains("réfraction") || msg.contains("réflexion") || msg.contains("prisme")) {
            return "**Optique - Physique Terminale C/D**\n\n" +
                "**Lois de Snell-Descartes :**\n" +
                "- Réflexion : angle incident = angle réfléchi\n" +
                "- Réfraction : n₁·sin(i₁) = n₂·sin(i₂)\n" +
                "  (n = indice de réfraction du milieu)\n\n" +
                "**Lentilles minces convergentes :**\n" +
                "- Focale f > 0\n" +
                "- Relation conjugaison : 1/OA' - 1/OA = 1/f\n" +
                "- Grandissement : γ = OA'/OA = A'B'/AB\n" +
                "- Image réelle si A' du même côté que la lumière\n\n" +
                "**Lentilles divergentes :**\n" +
                "- Focale f < 0\n" +
                "- Image toujours virtuelle, droite, réduite\n\n" +
                "**Construction géométrique :**\n" +
                "3 rayons particuliers pour une lentille convergente :\n" +
                "1. Rayon parallèle à l'axe → passe par F'\n" +
                "2. Rayon passant par O → non dévié\n" +
                "3. Rayon passant par F → émerge parallèle à l'axe\n\n" +
                "**Prisme :**\n" +
                "Déviation minimale : n·sin(A/2) = sin((A+Dm)/2)\n" +
                "où A = angle du prisme, Dm = déviation minimale\n\n" +
                "Décris ton exercice d'optique !";
        }

        if (msg.contains("ondes") || msg.contains("onde") || msg.contains("fréquence") || msg.contains("longueur d'onde") || msg.contains("interférence")) {
            return "**Ondes - Physique Terminale C/D**\n\n" +
                "**Caractéristiques d'une onde :**\n" +
                "- Période T (en secondes)\n" +
                "- Fréquence f = 1/T (en Hz)\n" +
                "- Longueur d'onde λ = v/f = v·T  (en mètres)\n" +
                "- Vitesse de propagation v (en m/s)\n\n" +
                "**Relation fondamentale :**\n" +
                "v = λ·f = λ/T\n" +
                "Lumière dans le vide : c = 3×10⁸ m/s\n\n" +
                "**Effet Doppler :**\n" +
                "f_observé = f_source × (v ± v_observateur) / (v ∓ v_source)\n" +
                "- Source s'approche : fréquence augmente\n" +
                "- Source s'éloigne : fréquence diminue\n\n" +
                "**Interférences :**\n" +
                "- Constructives : d₂ - d₁ = k·λ  (k entier)\n" +
                "- Destructives : d₂ - d₁ = (k+1/2)·λ\n\n" +
                "**Spectre électromagnétique :**\n" +
                "Gamma < Rayons X < UV < Visible < IR < Micro-ondes < Radio\n" +
                "Visible : 400 nm (violet) à 700 nm (rouge)\n\n" +
                "Pose-moi ton problème d'ondes !";
        }

        if (msg.contains("chimie") || msg.contains("acide") || msg.contains("base") || msg.contains("ph ") || msg.contains("oxydation") || msg.contains("réduction") || msg.contains("mol")) {
            return "**Chimie - Terminale C/D**\n\n" +
                "**Acides et Bases (Brønsted-Lowry) :**\n" +
                "- Acide : cède un proton H⁺\n" +
                "- Base : capte un proton H⁺\n" +
                "- pH = -log[H₃O⁺]\n" +
                "- Acide fort : pH = -log(Ca)\n" +
                "- Base forte : pH = 14 + log(Cb)\n" +
                "- pH < 7 : acide  ;  pH = 7 : neutre  ;  pH > 7 : basique\n\n" +
                "**Constante d'acidité Ka :**\n" +
                "AH + H₂O ⇌ A⁻ + H₃O⁺\n" +
                "Ka = [A⁻][H₃O⁺] / [AH]\n" +
                "pKa = -log Ka  ;  pH = pKa + log([A⁻]/[AH])\n\n" +
                "**Oxydoréduction :**\n" +
                "- Oxydant : capte des électrons (réduit)\n" +
                "- Réducteur : cède des électrons (oxydé)\n" +
                "- Demi-équation : Ox + n·e⁻ → Red\n" +
                "- Équation bilan : combiner les demi-équations\n\n" +
                "**Quantité de matière :**\n" +
                "n = m/M = C·V = V_gaz/22,4 (à CNTP)\n\n" +
                "**Dosage (titrage) :**\n" +
                "À l'équivalence : n_acide × 1 = n_base × 1 pour AH/BOH\n" +
                "Ca·Va = Cb·Vb  (acide/base monoprotonique)\n\n" +
                "Quel exercice de chimie as-tu ?";
        }

        // ==================== SVT ====================

        if (msg.contains("génétique") || msg.contains("adn") || msg.contains("gène") || msg.contains("chromosome") || msg.contains("allèle") || msg.contains("heredité")) {
            return "**Génétique - SVT Terminale D**\n\n" +
                "**Structure de l'ADN :**\n" +
                "- Double hélice : 2 brins antiparallèles liés par des ponts H\n" +
                "- Nucléotide = Phosphate + Sucre (désoxyribose) + Base azotée\n" +
                "- Complémentarité des bases : A-T (2 liaisons H) et G-C (3 liaisons H)\n\n" +
                "**Réplication de l'ADN (semi-conservative) :**\n" +
                "1. Déroulement de la double hélice (hélicase)\n" +
                "2. Synthèse des nouveaux brins (ADN polymérase)\n" +
                "3. Chaque cellule fille reçoit un brin parental + un brin néosynthétisé\n\n" +
                "**Synthèse des protéines :**\n" +
                "ADN (noyau) → ARNm (transcription) → Protéine (traduction, ribosome)\n" +
                "- Transcription : ADN → ARNm  (ARN polymérase)\n" +
                "- Traduction : ARNm → Protéine  (ribosome + ARNt)\n" +
                "- Codon = 3 nucléotides = 1 acide aminé\n" +
                "- Codon initiateur : AUG (méthionine)\n" +
                "- Codons stop : UAA, UAG, UGA\n\n" +
                "**Lois de Mendel :**\n" +
                "1ère loi (ségrégation) : les deux allèles d'un gène se séparent lors de la méiose\n" +
                "2ème loi (assortiment indépendant) : les gènes de loci différents se séparent indépendamment\n\n" +
                "**Génotype et Phénotype :**\n" +
                "- Homozygote dominant AA : phénotype dominant\n" +
                "- Hétérozygote Aa : phénotype dominant (si dominance complète)\n" +
                "- Homozygote récessif aa : phénotype récessif\n\n" +
                "Donne-moi ton exercice de génétique !";
        }

        if (msg.contains("photosynthèse") || msg.contains("chlorophylle") || msg.contains("chloroplaste")) {
            return "**Photosynthèse - SVT Terminale D**\n\n" +
                "**Équation globale :**\n" +
                "6CO₂ + 6H₂O + énergie lumineuse → C₆H₁₂O₆ + 6O₂\n\n" +
                "**Lieu :** Chloroplaste (thylakoïdes et stroma)\n\n" +
                "**Phase claire (photophosphorylation) :**\n" +
                "- Lieu : membranes des thylakoïdes\n" +
                "- Capture de l'énergie lumineuse par la chlorophylle\n" +
                "- Photolyse de l'eau : H₂O → 2H⁺ + 1/2 O₂ + 2e⁻\n" +
                "- Synthèse d'ATP et de NADPH₂\n" +
                "- Dégagement d'O₂\n\n" +
                "**Phase sombre - Cycle de Calvin (fixation du CO₂) :**\n" +
                "- Lieu : stroma du chloroplaste\n" +
                "- Fixation du CO₂ sur le RuBP (ribulose-bisphosphate)\n" +
                "- Réduction du PGA (3-phosphoglycérate) en G3P\n" +
                "- Régénération du RuBP (consomme ATP)\n" +
                "- Synthèse de glucose\n\n" +
                "**Facteurs limitants :**\n" +
                "- Intensité lumineuse\n" +
                "- Concentration en CO₂\n" +
                "- Température (enzymes)\n\n" +
                "**Importance écologique :**\n" +
                "Producteurs primaires → base de la chaîne alimentaire\n" +
                "Régulation du CO₂ atmosphérique\n\n" +
                "Décris ton exercice sur la photosynthèse !";
        }

        if (msg.contains("respiration") || msg.contains("mitochondrie") || msg.contains("atp") || msg.contains("glycolyse") || msg.contains("krebs")) {
            return "**Respiration Cellulaire - SVT Terminale D**\n\n" +
                "**Équation globale :**\n" +
                "C₆H₁₂O₆ + 6O₂ → 6CO₂ + 6H₂O + 38 ATP\n\n" +
                "**Les 3 étapes :**\n\n" +
                "**1. Glycolyse (cytoplasme) :**\n" +
                "- Glucose → 2 Pyruvate\n" +
                "- Bilan : 2 ATP + 2 NADH\n" +
                "- Se déroule en présence ou absence d'O₂\n\n" +
                "**2. Cycle de Krebs (matrice mitochondriale) :**\n" +
                "- Pyruvate → Acétyl-CoA → cycle (CO₂ libéré)\n" +
                "- Bilan : 2 ATP + 8 NADH + 2 FADH₂\n" +
                "- Nécessite O₂ indirectement\n\n" +
                "**3. Phosphorylation oxydative (crêtes mitochondriales) :**\n" +
                "- NADH et FADH₂ → ATP via la chaîne respiratoire\n" +
                "- L'O₂ est l'accepteur final d'électrons → H₂O\n" +
                "- Bilan : ~34 ATP\n\n" +
                "**Fermentation (sans O₂) :**\n" +
                "- Lactique : Pyruvate → Lactate  (muscles, bactéries)\n" +
                "- Alcoolique : Pyruvate → Éthanol + CO₂  (levures)\n" +
                "- Bilan : seulement 2 ATP\n\n" +
                "**Comparaison :**\n" +
                "Respiration (38 ATP) >> Fermentation (2 ATP)\n\n" +
                "As-tu un exercice sur la respiration ?";
        }

        if (msg.contains("immunologie") || msg.contains("immunité") || msg.contains("anticorps") || msg.contains("lymphocyte") || msg.contains("antigène")) {
            return "**Immunologie - SVT Terminale D**\n\n" +
                "**Les deux types d'immunité :**\n\n" +
                "**1. Immunité innée (non spécifique) :**\n" +
                "- Première ligne de défense (peau, mucus, phagocytes)\n" +
                "- Phagocytose : destruction par les macrophages et neutrophiles\n" +
                "- Inflammation : vasodilatation, recrutement de phagocytes\n" +
                "- Rapide mais non spécifique\n\n" +
                "**2. Immunité adaptative (spécifique) :**\n" +
                "- Spécifique à chaque antigène\n" +
                "- Mémoire immunologique\n\n" +
                "**Réponse humorale (lymphocytes B) :**\n" +
                "Ag → Activation LB → Plasmocytes → Anticorps (Ig)\n" +
                "Anticorps = immunoglobulines (2 chaînes lourdes + 2 chaînes légères)\n" +
                "Fixation Ac-Ag → complexe immun → phagocytose\n\n" +
                "**Réponse cellulaire (lymphocytes T) :**\n" +
                "- LT4 (auxiliaires) : orchestrent la réponse immune\n" +
                "- LT8 (cytotoxiques) : détruisent les cellules infectées\n" +
                "- LT régulateurs : limitent la réponse\n\n" +
                "**Vaccination :**\n" +
                "Injection d'Ag inactivés → mémoire immunitaire → protection rapide lors d'une 2ème infection\n\n" +
                "**SIDA :** VIH détruit les LT4 → immunodéficience\n\n" +
                "Donne-moi ton exercice d'immunologie !";
        }

        if (msg.contains("cellule") || msg.contains("eucaryote") || msg.contains("procaryote") || msg.contains("mitose") || msg.contains("méiose")) {
            return "**La Cellule et la Division Cellulaire - SVT**\n\n" +
                "**Types cellulaires :**\n" +
                "- Procaryote (bactéries) : pas de noyau délimité, ADN dans le nucléoïde\n" +
                "- Eucaryote (plantes, animaux, champignons) : noyau membraneux\n\n" +
                "**Organites cellulaires eucaryotes :**\n" +
                "- Noyau : ADN, transcription\n" +
                "- Mitochondrie : respiration cellulaire, ATP\n" +
                "- Chloroplaste : photosynthèse (végétaux)\n" +
                "- Réticulum endoplasmique : synthèse protéines, lipides\n" +
                "- Appareil de Golgi : tri et export des protéines\n" +
                "- Ribosome : traduction (synthèse protéique)\n" +
                "- Lysosome : digestion intracellulaire\n\n" +
                "**Mitose (division cellulaire ordinaire) :**\n" +
                "Cellule mère 2n → 2 cellules filles 2n identiques\n" +
                "Phases : Prophase → Métaphase → Anaphase → Télophase\n" +
                "- Prophase : condensation des chromosomes\n" +
                "- Métaphase : alignement sur la plaque équatoriale\n" +
                "- Anaphase : séparation des chromatides sœurs\n" +
                "- Télophase : reformation des noyaux\n\n" +
                "**Méiose (division réductionnelle) :**\n" +
                "Cellule mère 2n → 4 cellules filles n (gamètes)\n" +
                "2 divisions successives : Méiose I (réductionnelle) + Méiose II (équationnelle)\n" +
                "Brassage génétique : enjambement (prophase I) + ségrégation aléatoire\n\n" +
                "Décris ton exercice sur la cellule !";
        }

        if (msg.contains("évolution") || msg.contains("sélection naturelle") || msg.contains("darwin") || msg.contains("mutation") || msg.contains("spéciation")) {
            return "**L'Évolution - SVT Terminale D**\n\n" +
                "**Théorie de Darwin :**\n" +
                "- Variation : individus d'une population varient\n" +
                "- Hérédité : variations transmissibles à la descendance\n" +
                "- Sélection naturelle : survie et reproduction des plus adaptés\n" +
                "- Accumulation des changements → évolution\n\n" +
                "**Mécanismes de l'évolution :**\n" +
                "1. Mutation : modification de la séquence ADN\n" +
                "2. Dérive génétique : variation aléatoire dans les petites populations\n" +
                "3. Sélection naturelle : pression du milieu\n" +
                "4. Migration : flux de gènes entre populations\n\n" +
                "**Spéciation :**\n" +
                "Formation d'une nouvelle espèce :\n" +
                "- Isolement géographique (allopatrique)\n" +
                "- Isolement reproductif\n" +
                "- Divergence génétique\n\n" +
                "**Preuves de l'évolution :**\n" +
                "- Fossiles (paléontologie)\n" +
                "- Homologie anatomique (membres antérieurs des vertébrés)\n" +
                "- Biologie moléculaire (similitude ADN)\n" +
                "- Biogéographie\n\n" +
                "**Évolution humaine :**\n" +
                "Australopithèques → Homo habilis → Homo erectus → Homo sapiens\n" +
                "Caractères apparus : bipédie, volume crânien, langage\n\n" +
                "Pose-moi tes questions sur l'évolution !";
        }

        if (msg.contains("écosystème") || msg.contains("écologie") || msg.contains("chaîne alimentaire") || msg.contains("biome") || msg.contains("biodiversité")) {
            return "**Écologie et Écosystèmes - SVT Terminale D**\n\n" +
                "**Niveaux d'organisation :**\n" +
                "Individu → Population → Communauté (biocénose) → Écosystème → Biosphère\n\n" +
                "**Composantes d'un écosystème :**\n" +
                "- Biotope : milieu physique (sol, eau, air, climat)\n" +
                "- Biocénose : ensemble des êtres vivants\n" +
                "- Interaction : prédation, compétition, symbiose, parasitisme\n\n" +
                "**Flux d'énergie :**\n" +
                "Soleil → Producteurs (végétaux) → Consommateurs I → Consommateurs II → Décomposeurs\n" +
                "Rendement écologique : 10% seulement de l'énergie est transférée à chaque niveau\n\n" +
                "**Cycles biogéochimiques :**\n" +
                "- Cycle du carbone : photosynthèse ↔ respiration ↔ combustion\n" +
                "- Cycle de l'azote : fixation → nitrification → dénitrification\n" +
                "- Cycle de l'eau : évaporation → précipitation → ruissellement\n\n" +
                "**Biodiversité :**\n" +
                "- Diversité spécifique, génétique, écosystémique\n" +
                "- Menaces : déforestation, pollution, changement climatique, espèces invasives\n" +
                "- Protection : aires protégées, réduction des émissions, reforestation\n\n" +
                "Décris ton exercice d'écologie !";
        }

        if (msg.contains("reproduction") || msg.contains("fécondation") || msg.contains("gamète") || msg.contains("embryon") || msg.contains("développement")) {
            return "**Reproduction - SVT Terminale D**\n\n" +
                "**Reproduction sexuée :**\n" +
                "- Formation de gamètes haploïdes (n chromosomes) par méiose\n" +
                "- Fécondation → zygote diploïde (2n)\n\n" +
                "**Gamétogenèse chez l'homme :**\n" +
                "- Spermatogenèse : testicules, à partir de la puberté, continue\n" +
                "- Spermatide → spermatozoïde (tête: ADN, pièce intermédiaire: mitochondries, flagelle)\n\n" +
                "**Gamétogenèse chez la femme :**\n" +
                "- Ovogenèse : ovaires, stock à la naissance\n" +
                "- Cycle menstruel : 28 jours\n" +
                "- Ovulation : jour 14 (libération d'un ovocyte II)\n\n" +
                "**Fécondation :**\n" +
                "- Lieu : trompe de Fallope\n" +
                "- Spermatozoïde + Ovocyte II → Zygote (2n = 46 chr)\n\n" +
                "**Développement embryonnaire :**\n" +
                "Zygote → Morula (16 cellules) → Blastula → Gastrula → Organogenèse\n" +
                "- Feuillets : ectoderme, mésoderme, endoderme\n\n" +
                "**Contrôle hormonal :**\n" +
                "- Cycle : FSH, LH, Oestrogènes, Progestérone\n" +
                "- Grossesse : HCG, puis placenta assure la progestérone\n\n" +
                "Donne-moi ton exercice !";
        }

        // ==================== PLANNING / MÉTHODE ====================

        if (msg.contains("planning") || msg.contains("révision") || msg.contains("organiser") || msg.contains("méthode") || msg.contains("préparer")) {
            return "**Stratégie de Révision pour le Bac Mauritanien**\n\n" +
                "**Plan de révision (2 mois avant le Bac) :**\n\n" +
                "**Semaines 1-2 : Révision des bases**\n" +
                "- Matin (2h) : Mathématiques (théorèmes, formules)\n" +
                "- Après-midi (1h30) : Physique-Chimie\n" +
                "- Soir (1h) : SVT (si série D)\n\n" +
                "**Semaines 3-4 : Exercices et applications**\n" +
                "- Faire des exercices type Bac\n" +
                "- 1 annale par semaine par matière\n" +
                "- Corriger et analyser ses erreurs\n\n" +
                "**Semaines 5-6 : Approfondissement**\n" +
                "- Concentrer sur les points faibles\n" +
                "- Refaire les exercices ratés\n" +
                "- Créer des fiches de synthèse\n\n" +
                "**Dernière semaine :**\n" +
                "- Révisions légères, réviser les fiches\n" +
                "- Dormir 8h minimum\n" +
                "- Pas de nouvelles notions\n\n" +
                "**Conseils méthode :**\n" +
                "- Technique Pomodoro : 25 min travail + 5 min pause\n" +
                "- Faire des fiches synthétiques (une fiche = un chapitre)\n" +
                "- Relire les erreurs passées avant chaque session\n" +
                "- Expliquer les notions à voix haute (meilleure mémorisation)\n" +
                "- S'hydrater et faire des pauses actives\n\n" +
                "Dis-moi tes matières faibles et je ferai un planning personnalisé !";
        }

        // ==================== SALUTATION ====================

        if (msg.contains("bonjour") || msg.contains("salut") || msg.contains("bonsoir") || msg.contains("hello") || msg.contains("bac ia") || msg.contains("bacia")) {
            return "**Bonjour ! Je suis BacIA, ton assistant pédagogique !**\n\n" +
                "Je suis spécialisé dans la préparation au Baccalauréat mauritanien (séries C et D).\n\n" +
                "Je peux t'aider dans ces domaines :\n\n" +
                "**Mathématiques :**\n" +
                "- Analyse : dérivées, intégrales, limites, suites\n" +
                "- Algèbre : équations, systèmes, matrices\n" +
                "- Géométrie : vecteurs, produit scalaire, géométrie dans l'espace\n" +
                "- Probabilités et statistiques\n" +
                "- Trigonométrie, logarithme, exponentielle\n\n" +
                "**Physique-Chimie :**\n" +
                "- Mécanique : cinématique, dynamique, énergie\n" +
                "- Électricité : circuits, lois de Kirchhoff, condensateur\n" +
                "- Thermodynamique et gaz parfaits\n" +
                "- Optique : lentilles, réfraction\n" +
                "- Chimie : acide-base, oxydoréduction, titrages\n\n" +
                "**Sciences de la Vie et de la Terre (série D) :**\n" +
                "- Génétique et biologie moléculaire\n" +
                "- Physiologie cellulaire : photosynthèse, respiration\n" +
                "- Immunologie\n" +
                "- Évolution et écologie\n" +
                "- Reproduction\n\n" +
                "**Comment commencer ?** Pose-moi directement ta question ou choisis un sujet ci-dessus !";
        }

        // ==================== RÉPONSE PAR DÉFAUT ====================

        return "**BacIA - Assistant Pédagogique**\n\n" +
            "J'ai bien reçu ta question : \"" + message + "\"\n\n" +
            "Pour te donner la meilleure réponse possible, voici ce que je peux faire :\n\n" +
            "**Explique un concept** en disant, par exemple :\n" +
            "- \"Explique-moi les dérivées\"\n" +
            "- \"Comment fonctionne la photosynthèse ?\"\n" +
            "- \"Qu'est-ce que la loi d'Ohm ?\"\n\n" +
            "**Résous un exercice** en copiant l'énoncé complet\n\n" +
            "**Demande une formule** ou un rappel de cours :\n" +
            "- \"Donne-moi les formules de trigonométrie\"\n" +
            "- \"Rappelle-moi le premier principe de la thermodynamique\"\n\n" +
            "**Thèmes disponibles :**\n" +
            "Dérivées, Intégrales, Limites, Suites, Probabilités, Trigonométrie, Logarithme, Exponentielle, Matrices, Géométrie\n" +
            "Cinématique, Dynamique, Énergie, Électricité, Thermodynamique, Optique, Ondes, Chimie\n" +
            "Génétique, ADN, Cellule, Photosynthèse, Respiration, Immunologie, Évolution, Écologie\n\n" +
            "Note : Pour activer l'IA avancée avec des réponses illimitées, configurez votre clé API Anthropic dans application.properties";
    }

    public List<ChatMessage> getHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return chatMessageRepository.findByUserIdOrderByDateMessageAsc(user.getId());
    }

    public void clearHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        List<ChatMessage> messages = chatMessageRepository.findByUserIdOrderByDateMessageAsc(user.getId());
        chatMessageRepository.deleteAll(messages);
    }
}
