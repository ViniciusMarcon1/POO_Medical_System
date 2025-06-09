package exceptions;

/**
 * Lançada quando o arquivo CSV está mal-formado ou contém dados inválidos.
 */
public class ArquivoInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ArquivoInvalidoException(String message) {
        super(message);
    }

    public ArquivoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}