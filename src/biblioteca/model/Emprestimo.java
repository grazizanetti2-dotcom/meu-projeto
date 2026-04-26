package biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um empréstimo de item da biblioteca.
 * Utiliza Java Time para calcular datas e atrasos.
 */
public class Emprestimo {

    public enum StatusEmprestimo {
        ATIVO, DEVOLVIDO, ATRASADO
    }

    private final String id;
    private final Usuario usuario;
    private final Item item;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoEfetiva;   // preenchida apenas na devolução
    private StatusEmprestimo status;

    private static final int PRAZO_DIAS = 14;
    private static final double MULTA_POR_DIA = 0.50;

    public Emprestimo(String id, Usuario usuario, Item item) {
        this.id = id;
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(PRAZO_DIAS);
        this.status = StatusEmprestimo.ATIVO;
    }

    public void realizarDevolucao() {
        this.dataDevolucaoEfetiva = LocalDate.now();
        this.status = StatusEmprestimo.DEVOLVIDO;
    }

    public boolean estaAtrasado() {
        LocalDate referencia = (dataDevolucaoEfetiva != null) ? dataDevolucaoEfetiva : LocalDate.now();
        return referencia.isAfter(dataPrevistaDevolucao);
    }

    public long getDiasAtraso() {
        if (!estaAtrasado()) return 0L;
        LocalDate referencia = (dataDevolucaoEfetiva != null) ? dataDevolucaoEfetiva : LocalDate.now();
        return ChronoUnit.DAYS.between(dataPrevistaDevolucao, referencia);
    }

    public double calcularMulta() {
        return getDiasAtraso() * MULTA_POR_DIA;
    }

    public void atualizarStatus() {
        if (status == StatusEmprestimo.ATIVO && estaAtrasado()) {
            status = StatusEmprestimo.ATRASADO;
        }
    }

    // Getters
    public String getId() { return id; }

    public Usuario getUsuario() { return usuario; }

    public Item getItem() { return item; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }

    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }

    public LocalDate getDataDevolucaoEfetiva() { return dataDevolucaoEfetiva; }

    public StatusEmprestimo getStatus() { return status; }

    public static int getPrazoDias() { return PRAZO_DIAS; }

    /**
     * toString() puro: apenas leitura, sem modificar estado.
     */
    @Override
    public String toString() {
        // Calcula atraso para exibição sem alterar o campo status
        boolean atrasado = (status != StatusEmprestimo.DEVOLVIDO) && estaAtrasado();
        String sufixo = atrasado
                ? String.format(" | ⚠ ATRASADO %d dia(s) | Multa: R$ %.2f", getDiasAtraso(), calcularMulta())
                : "";
        StatusEmprestimo statusExibido = atrasado ? StatusEmprestimo.ATRASADO : status;

        return String.format(
                "[%s] Usuário: %s | Item: %s | Emprestado: %s | Devolução prevista: %s | Status: %s%s",
                id, usuario.getNome(), item.getTitulo(),
                dataEmprestimo, dataPrevistaDevolucao, statusExibido, sufixo
        );
    }
}