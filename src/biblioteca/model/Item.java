package biblioteca.model;

import java.time.LocalDate;

/**
 * Classe abstrata base para itens da biblioteca.
 * Demonstra: encapsulamento, herança e polimorfismo.
 */
public abstract class Item {

    private final String id;
    private final String titulo;
    private final String autor;
    private final int anoPublicacao;
    private boolean disponivel;
    private final LocalDate dataCadastro;

    public Item(String id, String titulo, String autor, int anoPublicacao) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = true;
        this.dataCadastro = LocalDate.now(); // Java Time
    }

    // Método abstrato: polimorfismo
    public abstract String getTipoItem();

    public abstract String getDescricaoDetalhada();

    // Getters e Setters: encapsulamento
    public String getId() { return id; }

    public String getTitulo() { return titulo; }

    public String getAutor() { return autor; }

    public int getAnoPublicacao() { return anoPublicacao; }

    public boolean isDisponivel() { return disponivel; }

    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public LocalDate getDataCadastro() { return dataCadastro; }

    @Override
    public String toString() {
        String status = disponivel ? "✔ Disponível" : "✘ Emprestado";
        return String.format("[%s] %s | %s | Autor: %s | Ano: %d | %s",
                getTipoItem(), id, titulo, autor, anoPublicacao, status);
    }
}