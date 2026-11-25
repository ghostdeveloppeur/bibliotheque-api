package exception;

public class MembreIntrouvableException extends BibliothequeException {
    public MembreIntrouvableException(Long id) {
        super("Membre introuvable (id = " + id + ")");
    }
}
