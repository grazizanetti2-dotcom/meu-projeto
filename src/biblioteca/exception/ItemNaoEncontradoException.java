package biblioteca.exception;

/**
 * Exceção lançada quando um item não é encontrado no acervo.
 */
public class ItemNaoEncontradoException extends Exception {
    public ItemNaoEncontradoException(String id) {
        super("Item com ID '" + id + "' não encontrado no acervo.");
    }
}