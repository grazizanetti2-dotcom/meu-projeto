package biblioteca.model;

/**
 * Classe Livro — herda de Item.
 * Adiciona atributos específicos: ISBN, editora, número de páginas.
 */
public class Livro extends Item {

    private final String isbn;
    private final String editora;
    private final int numeroPaginas;

    public Livro(String id, String titulo, String autor, int anoPublicacao,
                 String isbn, String editora, int numeroPaginas) {
        super(id, titulo, autor, anoPublicacao);
        this.isbn = isbn;
        this.editora = editora;
        this.numeroPaginas = numeroPaginas;
    }

    @Override
    public String getTipoItem() {
        return "LIVRO";
    }

    @Override
    public String getDescricaoDetalhada() {
        return String.format(
                "Livro: %s\n  Autor    : %s\n  ISBN     : %s\n  Editora  : %s\n  Páginas  : %d\n  Ano      : %d",
                getTitulo(), getAutor(), isbn, editora, numeroPaginas, getAnoPublicacao()
        );
    }

    public String getIsbn() { return isbn; }

    public String getEditora() { return editora; }

    public int getNumeroPaginas() { return numeroPaginas; }
}