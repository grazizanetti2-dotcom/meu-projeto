package biblioteca.exception;

/**
 * Exceção lançada quando o usuário atingiu o limite de empréstimos simultâneos.
 */
public class LimiteEmprestimosException extends Exception {
    public LimiteEmprestimosException(String nomeUsuario, int limite) {
        super("Usuário '" + nomeUsuario + "' já atingiu o limite de " + limite + " empréstimos simultâneos.");
    }
}