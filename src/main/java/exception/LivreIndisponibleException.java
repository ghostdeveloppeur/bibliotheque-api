package exception;

public class LivreIndisponibleException extends BibliothequeException {
    public LivreIndisponibleException(String titre) {
        super("Aucun exemplaire disponible de « "+titre+" »");
    }
}
