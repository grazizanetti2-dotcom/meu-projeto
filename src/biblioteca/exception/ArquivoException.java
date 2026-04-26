package biblioteca.exception;

/**
 * Exceção lançada em falhas de leitura/escrita de arquivos (Java NIO).
 */
public class ArquivoException extends Exception {
    public ArquivoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}