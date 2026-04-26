package biblioteca.exception;

/**
 * Exceção lançada quando se tenta emprestar um item já emprestado.
 */
public class ItemIndisponivelException extends Exception {
    public ItemIndisponivelException(String titulo) {
        super("O item '" + titulo + "' não está disponível para empréstimo.");
    }
}