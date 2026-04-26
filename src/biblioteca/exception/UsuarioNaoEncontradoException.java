package biblioteca.exception;

/**
 * Exceção lançada quando um usuário não é encontrado no sistema.
 */
public class UsuarioNaoEncontradoException extends Exception {
    public UsuarioNaoEncontradoException(String matricula) {
        super("Usuário com matrícula '" + matricula + "' não encontrado.");
    }
}
