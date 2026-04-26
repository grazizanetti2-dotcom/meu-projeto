package biblioteca.service;

import biblioteca.exception.*;
import biblioteca.model.*;
import biblioteca.util.ArquivoUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Optional;

/**
 * Serviço principal da biblioteca.
 * Centraliza toda a lógica de negócio: acervo, usuários e empréstimos.
 * Demonstra: coleções, controle de fluxo, repetição, exceções e Java NIO.
 */
public class BibliotecaService {

    private final Map<String, Item> acervo = new LinkedHashMap<>();
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Emprestimo> emprestimos = new LinkedHashMap<>();

    private static final String DIR_DADOS = "data/";
    private static final String ARQUIVO_LOG = DIR_DADOS + "biblioteca.log";
    private static final String ARQUIVO_EMPRESTIMOS = DIR_DADOS + "emprestimos.txt";

    private int contadorEmprestimos = 1;

    public BibliotecaService() {
        try {
            ArquivoUtil.garantirDiretorio(DIR_DADOS);
        } catch (ArquivoException e) {
            System.err.println("⚠ Aviso: Não foi possível criar o diretório de dados. " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // ACERVO
    // ─────────────────────────────────────────

    public void adicionarItem(Item item) {
        acervo.put(item.getId(), item);
        log("Item adicionado ao acervo: " + item.getTitulo() + " [" + item.getId() + "]");
    }

    public Item buscarItem(String id) throws ItemNaoEncontradoException {
        Item item = acervo.get(id);
        if (item == null) throw new ItemNaoEncontradoException(id);
        return item;
    }

    public List<Item> listarTodosItens() {
        return new ArrayList<>(acervo.values());
    }

    public List<Item> listarItensDisponiveis() {
        // Estrutura de repetição + operadores lógicos
        List<Item> disponiveis = new ArrayList<>();
        for (Item item : acervo.values()) {
            if (item.isDisponivel()) {
                disponiveis.add(item);
            }
        }
        return disponiveis;
    }

    public List<Item> listarItensEmprestados() {
        List<Item> emprestados = new ArrayList<>();
        for (Item item : acervo.values()) {
            if (!item.isDisponivel()) {
                emprestados.add(item);
            }
        }
        return emprestados;
    }

    public List<Item> buscarPorTitulo(String trecho) {
        // Controle de fluxo + repetição
        List<Item> resultado = new ArrayList<>();
        String busca = trecho.toLowerCase();
        for (Item item : acervo.values()) {
            if (item.getTitulo().toLowerCase().contains(busca) ||
                    item.getAutor().toLowerCase().contains(busca)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public List<Item> listarPorTipo(String tipo) {
        List<Item> resultado = new ArrayList<>();
        for (Item item : acervo.values()) {
            if (item.getTipoItem().equalsIgnoreCase(tipo)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    // ─────────────────────────────────────────
    // USUÁRIOS
    // ─────────────────────────────────────────

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getMatricula(), usuario);
        log("Usuário cadastrado: " + usuario.getNome() + " [" + usuario.getMatricula() + "]");
    }

    public Usuario buscarUsuario(String matricula) throws UsuarioNaoEncontradoException {
        Usuario usuario = usuarios.get(matricula);
        if (usuario == null) throw new UsuarioNaoEncontradoException(matricula);
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    // ─────────────────────────────────────────
    // EMPRÉSTIMOS
    // ─────────────────────────────────────────

    /**
     * Realiza um empréstimo com todas as validações necessárias.
     * Lança exceções checadas conforme as regras de negócio.
     */
    public Emprestimo realizarEmprestimo(String matriculaUsuario, String idItem)
            throws UsuarioNaoEncontradoException, ItemNaoEncontradoException,
            ItemIndisponivelException, LimiteEmprestimosException, ArquivoException {

        // Busca com tratamento de exceção
        Usuario usuario = buscarUsuario(matriculaUsuario);
        Item item = buscarItem(idItem);

        // Validações com controle de fluxo
        if (!item.isDisponivel()) {
            throw new ItemIndisponivelException(item.getTitulo());
        }

        if (!usuario.podeEmprestar()) {
            throw new LimiteEmprestimosException(usuario.getNome(), Usuario.getLimiteEmprestimos());
        }

        // Cria o empréstimo
        String idEmprestimo = String.format("EMP%04d", contadorEmprestimos++);
        Emprestimo emprestimo = new Emprestimo(idEmprestimo, usuario, item);

        // Atualiza estados
        item.setDisponivel(false);
        usuario.incrementarEmprestimos();
        emprestimos.put(idEmprestimo, emprestimo);

        // Persiste e registra
        salvarEmprestimos();
        log("Empréstimo realizado: " + idEmprestimo + " | " + usuario.getNome() + " ← " + item.getTitulo());

        return emprestimo;
    }

    /**
     * Realiza a devolução de um empréstimo.
     * Retorna Optional.empty() se o ID não for encontrado.
     */
    public Optional<Emprestimo> realizarDevolucao(String idEmprestimo) throws ArquivoException {
        Emprestimo emprestimo = emprestimos.get(idEmprestimo);

        if (emprestimo == null) {
            System.out.println("⚠ Empréstimo não encontrado: " + idEmprestimo);
            return Optional.empty();
        }

        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
            System.out.println("⚠ Este empréstimo já foi devolvido.");
            return Optional.of(emprestimo);
        }

        emprestimo.realizarDevolucao();
        emprestimo.getItem().setDisponivel(true);
        emprestimo.getUsuario().decrementarEmprestimos();

        salvarEmprestimos();
        log("Devolução realizada: " + idEmprestimo + " | " + emprestimo.getItem().getTitulo());

        return Optional.of(emprestimo);
    }

    public List<Emprestimo> listarEmprestimos() {
        return new ArrayList<>(emprestimos.values());
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        List<Emprestimo> ativos = new ArrayList<>();
        for (Emprestimo e : emprestimos.values()) {
            if (e.getStatus() != Emprestimo.StatusEmprestimo.DEVOLVIDO) {
                e.atualizarStatus(); // Atualiza status de atraso
                ativos.add(e);
            }
        }
        return ativos;
    }

    public List<Emprestimo> listarEmprestimosAtrasados() {
        List<Emprestimo> atrasados = new ArrayList<>();
        for (Emprestimo e : listarEmprestimosAtivos()) {
            if (e.estaAtrasado()) atrasados.add(e);
        }
        return atrasados;
    }

    // ─────────────────────────────────────────
    // RELATÓRIO / ESTATÍSTICAS
    // ─────────────────────────────────────────

    public void exibirEstatisticas() {
        int totalItens = acervo.size();
        int totalDisponiveis = (int) acervo.values().stream().filter(Item::isDisponivel).count();
        int totalEmprestados = totalItens - totalDisponiveis;
        int totalUsuarios = usuarios.size();
        int totalEmprestimos = emprestimos.size();
        int totalAtrasados = listarEmprestimosAtrasados().size();

        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║       ESTATÍSTICAS DA BIBLIOTECA  ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.printf("║  Acervo total      : %-12d ║%n", totalItens);
        System.out.printf("║  Disponíveis       : %-12d ║%n", totalDisponiveis);
        System.out.printf("║  Emprestados       : %-12d ║%n", totalEmprestados);
        System.out.printf("║  Usuários          : %-12d ║%n", totalUsuarios);
        System.out.printf("║  Empréstimos       : %-12d ║%n", totalEmprestimos);
        System.out.printf("║  Atrasados         : %-12d ║%n", totalAtrasados);
        System.out.println("╚══════════════════════════════════╝");
    }

    // ─────────────────────────────────────────
    // PERSISTÊNCIA (Java NIO)
    // ─────────────────────────────────────────

    /**
     * Salva os empréstimos em arquivo de texto usando Java NIO.
     */
    private void salvarEmprestimos() throws ArquivoException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> linhas = new ArrayList<>();
        linhas.add("# Empréstimos da Biblioteca — atualizado em " + LocalDate.now().format(fmt));
        linhas.add("# ID | Matrícula | Nome Usuario | ID Item | Título | Data Empréstimo | Devolução Prevista | Status");
        linhas.add("# ─────────────────────────────────────────────────────────");

        for (Emprestimo e : emprestimos.values()) {
            String linha = String.join("|",
                    e.getId(),
                    e.getUsuario().getMatricula(),
                    e.getUsuario().getNome(),
                    e.getItem().getId(),
                    e.getItem().getTitulo(),
                    e.getDataEmprestimo().format(fmt),
                    e.getDataPrevistaDevolucao().format(fmt),
                    e.getStatus().toString()
            );
            linhas.add(linha);
        }

        ArquivoUtil.escreverArquivo(ARQUIVO_EMPRESTIMOS, linhas);
    }

    private void log(String mensagem) {
        try {
            ArquivoUtil.registrarLog(ARQUIVO_LOG, mensagem);
        } catch (ArquivoException e) {
            System.err.println("⚠ Falha ao registrar log: " + e.getMessage());
        }
    }
}