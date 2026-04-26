package biblioteca.model;

/**
 * Classe Revista — herda de Item.
 * Demonstra polimorfismo: mesmo método getTipoItem() com comportamento diferente.
 */
public class Revista extends Item {

    private final String issn;
    private final int edicao;
    private final String categoria;

    public Revista(String id, String titulo, String autor, int anoPublicacao,
                   String issn, int edicao, String categoria) {
        super(id, titulo, autor, anoPublicacao);
        this.issn = issn;
        this.edicao = edicao;
        this.categoria = categoria;
    }

    @Override
    public String getTipoItem() {
        return "REVISTA";
    }

    @Override
    public String getDescricaoDetalhada() {
        return String.format(
                "Revista: %s\n  Editora  : %s\n  ISSN     : %s\n  Edição   : %d\n  Categoria: %s\n  Ano      : %d",
                getTitulo(), getAutor(), issn, edicao, categoria, getAnoPublicacao()
        );
    }

    public String getIssn() { return issn; }

    public int getEdicao() { return edicao; }

    public String getCategoria() { return categoria; }
}