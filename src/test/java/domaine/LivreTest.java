package domaine;

import exception.LivreIndisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivreTest {

    private Livre livre;

    @BeforeEach
    void setUp() {
        // Initialisation d'un livre valide avant chaque test
        livre = new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", "978-2070612758", 2);
    }

    @Nested
    @DisplayName("Tests du constructeur")
    class ConstructeurTests {

        @Test
        @DisplayName("Doit créer un livre valide avec tous les exemplaires disponibles")
        void creationValide() {
            assertEquals("Le Petit Prince", livre.getTitre());
            assertEquals("Antoine de Saint-Exupéry", livre.getAuteur());
            assertEquals("978-2070612758", livre.getIsbn());
            assertEquals(2, livre.getExemplairesTotal());
            assertEquals(2, livre.getExemplairesDisponibles());
            assertTrue(livre.estDisponible());
        }

        @Test
        @DisplayName("Doit refuser un nombre d'exemplaires inférieur à 1")
        void exemplairesInvalides() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Livre("1984", "George Orwell", "978-0451524935", 0);
            });
        }

        @Test
        @DisplayName("Doit refuser un titre, auteur ou ISBN vide ou null")
        void champsVides() {
            assertThrows(IllegalArgumentException.class, () ->
                    new Livre("", "Auteur", "12345", 1));
            assertThrows(IllegalArgumentException.class, () ->
                    new Livre("Titre", null, "12345", 1));
            assertThrows(IllegalArgumentException.class, () ->
                    new Livre("Titre", "Auteur", "   ", 1));
        }
    }

    @Nested
    @DisplayName("Tests d'emprunt et de retour")
    class GestionExemplairesTests {

        @Test
        @DisplayName("Doit décrémenter les exemplaires lors d'un emprunt")
        void emprunterAvecSucces() {
            livre.emprunterUnExemplaire();
            assertEquals(1, livre.getExemplairesDisponibles());
            assertTrue(livre.estDisponible());

            livre.emprunterUnExemplaire();
            assertEquals(0, livre.getExemplairesDisponibles());
            assertFalse(livre.estDisponible());
        }

        @Test
        @DisplayName("Doit lever LivreIndisponibleException si aucun exemplaire n'est disponible")
        void emprunterLivreIndisponible() {
            livre.emprunterUnExemplaire();
            livre.emprunterUnExemplaire(); // Stock à 0

            assertThrows(LivreIndisponibleException.class, () -> {
                livre.emprunterUnExemplaire();
            });
        }

        @Test
        @DisplayName("Doit incrémenter les exemplaires lors d'un retour")
        void rendreAvecSucces() {
            livre.emprunterUnExemplaire(); // Stock passe à 1
            livre.rendreUnExemplaire();    // Stock repasse à 2

            assertEquals(2, livre.getExemplairesDisponibles());
            assertTrue(livre.estDisponible());
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si tous les exemplaires sont déjà en rayon")
        void rendreLivreDejaComplet() {
            assertThrows(IllegalStateException.class, () -> {
                livre.rendreUnExemplaire();
            });
        }
    }

    @Nested
    @DisplayName("Tests d'égalité (equals et hashCode)")
    class EgaliteTests {

        @Test
        @DisplayName("Deux livres avec le même ISBN doivent être égaux même si le titre diffère")
        void memeIsbn() {
            Livre autreEdition = new Livre("Titre différent", "Autre auteur", "978-2070612758", 5);

            assertEquals(livre, autreEdition);
            assertEquals(livre.hashCode(), autreEdition.hashCode());
        }

        @Test
        @DisplayName("Deux livres avec des ISBN différents ne doivent pas être égaux")
        void isbnDifferent() {
            Livre autreLivre = new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", "000-0000000000", 2);

            assertNotEquals(livre, autreLivre);
        }
    }
}
