# bibliotheque-api

Projet fil rouge du plan de formation avant le stage : gestion d'une bibliothèque (livres, membres, emprunts).

**État actuel : phase 1 terminée (Java pur, sans Spring).** La suite arrive phase par phase : API REST (Spring Boot), base de données, front, CI/CD, Docker, Kubernetes, Kafka.

## Prérequis

- Java 17 ou plus
- Maven 3.8 ou plus

## Lancer

```bash
mvn test                      # lance les 47 tests JUnit
mvn compile                   # compile le projet
java -cp target/classes Demonstration   # joue un petit scénario en console
```

## Organisation du code

| Package | Rôle |
|---|---|
| `domaine` | Livre, Membre, Emprunt, et `ReglesBibliotheque` (les constantes métier) |
| `exception` | Les erreurs métier, toutes filles de `BibliothequeException` |
| `depot` | L'interface `Depot<T>` et son implémentation en mémoire |
| `service` | `Bibliotheque` : applique les règles métier |

## Règles métier et où elles sont garanties

| Règle | Où | Test principal |
|---|---|---|
| R1 Livre indisponible refusé | `Bibliotheque.emprunter`, `Livre.emprunterUnExemplaire` | `BibliothequeTest.emprunterUnLivreIndisponibleEstRefuse` |
| R2 Maximum d'emprunts en cours | `Bibliotheque.emprunter` | `BibliothequeTest.leMembreNePeutPasDepasser...` |
| R3 Retour prévu = emprunt + durée | `Emprunt` (constructeur) | `EmpruntTest.laDateDeRetourPrevue...` |
| R4 Emprunter retire un exemplaire | `Livre.emprunterUnExemplaire` | `LivreTest.emprunterUnExemplaireReduit...` |
| R5 Rendre remet un exemplaire | `Livre.rendreUnExemplaire` | `BibliothequeTest.rendreUnLivreLiberUnExemplaire...` |
| R6 Pas de double retour | `Emprunt.marquerCommeRendu` | `EmpruntTest.rendreDeuxFois...` |
| R7 Définition du retard | `Emprunt.estEnRetard` | `EmpruntTest` (veille, jour même, lendemain) |
| R8 Livre ou membre inexistant | `Bibliotheque.trouver...` | `BibliothequeTest.emprunterAvec...Inconnu...` |
| R9 ISBN et email uniques | `Bibliotheque.ajouterLivre`, `inscrireMembre` | `BibliothequeTest.ajouterUnLivreAvecUnIsbn...` |

## Choix de conception

- **Constantes métier** dans une classe dédiée `ReglesBibliotheque` : changer la durée d'un emprunt = modifier une seule ligne.
- **Pas de statut stocké** dans Emprunt : « en cours » se déduit de `dateRetourEffective`. Une seule source de vérité.
- **Pas de compteur d'emprunts** dans Membre : il se calcule depuis les emprunts, donc il ne peut pas être faux.
- **La date du jour est passée en paramètre** (`estEnRetard`, `emprunter`, `rendre`) : on peut tester un retard sans attendre 15 jours.
- **Vérifier d'abord, modifier ensuite** dans `emprunter` : une erreur ne laisse jamais une opération à moitié faite.
- **Exceptions non vérifiées** avec une classe mère commune : l'API REST pourra les traduire en codes HTTP à un seul endroit.
- **Interface `Depot<T>`** : la logique métier ne dépend pas du stockage ; Spring Data JPA remplacera l'implémentation en mémoire.
