package exceptions;

/**
 * Lançada quando os parâmetros de busca são inválidos ou inconsistentes.
 */
public class BuscaInvalidaException extends Exception {

    private static final long serialVersionUID = 1L;

    public BuscaInvalidaException(String message) {
        super(message);
    }

    public BuscaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}