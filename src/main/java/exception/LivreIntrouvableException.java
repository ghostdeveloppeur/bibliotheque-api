package exception;

public class LivreIntrouvableException extends BibliothequeException {
    public LivreIntrouvableException(Long id) {
        super("Livre introuvable (id = " + id + ")");
    }
}
