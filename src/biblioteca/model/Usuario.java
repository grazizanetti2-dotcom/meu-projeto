package biblioteca.model;

import java.time.LocalDate;

/**
 * Representa um usuário cadastrado na biblioteca.
 */
public class Usuario {

    private final String matricula;
    private final String nome;
    private final String email;
    private final LocalDate dataCadastro;
    private int emprestimosAtivos;

    private static final int LIMITE_EMPRESTIMOS = 3;

    public Usuario(String matricula, String nome, String email) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.dataCadastro = LocalDate.now();
        this.emprestimosAtivos = 0;
    }

    public boolean podeEmprestar() {
        return emprestimosAtivos < LIMITE_EMPRESTIMOS;
    }

    public void incrementarEmprestimos() {
        emprestimosAtivos++;
    }

    public void decrementarEmprestimos() {
        if (emprestimosAtivos > 0) emprestimosAtivos--;
    }

    // Getters
    public String getMatricula() { return matricula; }

    public String getNome() { return nome; }

    public String getEmail() { return email; }

    public LocalDate getDataCadastro() { return dataCadastro; }

    public int getEmprestimosAtivos() { return emprestimosAtivos; }

    public static int getLimiteEmprestimos() { return LIMITE_EMPRESTIMOS; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Email: %s | Empréstimos ativos: %d/%d",
                matricula, nome, email, emprestimosAtivos, LIMITE_EMPRESTIMOS);
    }
}