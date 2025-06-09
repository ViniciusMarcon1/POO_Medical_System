package exceptions;

/**
 * Lançada em falhas de leitura ou gravação binária dos dados.
 */
public class PersistenciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}