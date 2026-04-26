package biblioteca;

import biblioteca.exception.*;
import biblioteca.model.*;
import biblioteca.service.BibliotecaService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * ╔═══════════════════════════════════════════════════════╗
 * ║         SISTEMA DE GERENCIAMENTO DE BIBLIOTECA        ║
 * ╠═══════════════════════════════════════════════════════╣
 * ║  Conceitos aplicados:                                 ║
 * ║  • Tipos de dados primitivos e referência             ║
 * ║  • Operadores (aritméticos, lógicos, relacionais)     ║
 * ║  • Controle de fluxo (if/else, switch)                ║
 * ║  • Estruturas de repetição (while, for, for-each)     ║
 * ║  • OOP: encapsulamento, herança, polimorfismo         ║
 * ║  • Exceções checadas e não checadas                   ║
 * ║  • Java NIO (leitura/escrita de arquivos)             ║
 * ║  • Java Time (LocalDate, ChronoUnit)                  ║
 * ╚═══════════════════════════════════════════════════════╝
 */
public class Main {

    private static final BibliotecaService servico = new BibliotecaService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        popularDadosIniciais();

        boolean executando = true;

        while (executando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Escolha uma opção: ");

            // Controle de fluxo com switch
            switch (opcao) {
                case 1 -> menuAcervo();
                case 2 -> menuEmprestimos();
                case 3 -> menuUsuarios();
                case 4 -> menuRelatorios();
                case 0 -> {
                    System.out.println("\n👋 Encerrando o sistema. Até logo!");
                    executando = false;
                }
                default -> System.out.println("\n⚠ Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    // ─────────────────────────────────────────
    // MENUS
    // ─────────────────────────────────────────

    private static void exibirMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║   BIBLIOTECA — MENU PRINCIPAL ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Acervo                    ║");
        System.out.println("║  2. Empréstimos               ║");
        System.out.println("║  3. Usuários                  ║");
        System.out.println("║  4. Relatórios / Estatísticas ║");
        System.out.println("║  0. Sair                      ║");
        System.out.println("╚══════════════════════════════╝");
    }

    private static void menuAcervo() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n── ACERVO ──────────────────────");
            System.out.println("  1. Listar todos os itens");
            System.out.println("  2. Listar itens disponíveis");
            System.out.println("  3. Listar itens emprestados");
            System.out.println("  4. Buscar por título ou autor");
            System.out.println("  5. Ver detalhes de um item");
            System.out.println("  0. Voltar");
            System.out.println("────────────────────────────────");

            int opcao = lerInteiro("Opção: ");
            switch (opcao) {
                case 1 -> listarTodos();
                case 2 -> listarDisponiveis();
                case 3 -> listarEmprestados();
                case 4 -> buscarItem();
                case 5 -> verDetalhes();
                case 0 -> voltar = true;
                default -> System.out.println("⚠ Opção inválida.");
            }
        }
    }

    private static void menuEmprestimos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n── EMPRÉSTIMOS ─────────────────");
            System.out.println("  1. Realizar empréstimo");
            System.out.println("  2. Realizar devolução");
            System.out.println("  3. Listar empréstimos ativos");
            System.out.println("  4. Listar empréstimos atrasados");
            System.out.println("  5. Histórico completo");
            System.out.println("  0. Voltar");
            System.out.println("────────────────────────────────");

            int opcao = lerInteiro("Opção: ");
            switch (opcao) {
                case 1 -> realizarEmprestimo();
                case 2 -> realizarDevolucao();
                case 3 -> listarEmprestimosAtivos();
                case 4 -> listarEmprestimosAtrasados();
                case 5 -> listarHistoricoCompleto();
                case 0 -> voltar = true;
                default -> System.out.println("⚠ Opção inválida.");
            }
        }
    }

    private static void menuUsuarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n── USUÁRIOS ────────────────────");
            System.out.println("  1. Listar usuários");
            System.out.println("  2. Cadastrar usuário");
            System.out.println("  0. Voltar");
            System.out.println("────────────────────────────────");

            int opcao = lerInteiro("Opção: ");
            switch (opcao) {
                case 1 -> listarUsuarios();
                case 2 -> cadastrarUsuario();
                case 0 -> voltar = true;
                default -> System.out.println("⚠ Opção inválida.");
            }
        }
    }

    private static void menuRelatorios() {
        servico.exibirEstatisticas();
        System.out.println("\n📄 Logs e empréstimos são salvos automaticamente na pasta 'data/'.");
        pausar();
    }

    // ─────────────────────────────────────────
    // AÇÕES DO ACERVO
    // ─────────────────────────────────────────

    private static void listarTodos() {
        List<Item> itens = servico.listarTodosItens();
        System.out.println("\n── ACERVO COMPLETO (" + itens.size() + " itens) ────────");
        // Estrutura de repetição for-each com polimorfismo
        for (Item item : itens) {
            System.out.println("  " + item); // toString() polimórfico
        }
        pausar();
    }

    private static void listarDisponiveis() {
        List<Item> itens = servico.listarItensDisponiveis();
        System.out.println("\n── DISPONÍVEIS (" + itens.size() + " itens) ────────────");
        if (itens.isEmpty()) {
            System.out.println("  Nenhum item disponível no momento.");
        } else {
            for (Item item : itens) {
                System.out.println("  " + item);
            }
        }
        pausar();
    }

    private static void listarEmprestados() {
        List<Item> itens = servico.listarItensEmprestados();
        System.out.println("\n── EMPRESTADOS (" + itens.size() + " itens) ─────────────");
        if (itens.isEmpty()) {
            System.out.println("  Todos os itens estão disponíveis.");
        } else {
            for (Item item : itens) {
                System.out.println("  " + item);
            }
        }
        pausar();
    }

    private static void buscarItem() {
        System.out.print("\nDigite o título ou autor para buscar: ");
        String termo = scanner.nextLine().trim();

        if (termo.isEmpty()) {
            System.out.println("⚠ Busca inválida.");
            return;
        }

        List<Item> resultado = servico.buscarPorTitulo(termo);
        System.out.println("\n── RESULTADO DA BUSCA: '" + termo + "' ──────────");
        if (resultado.isEmpty()) {
            System.out.println("  Nenhum item encontrado.");
        } else {
            for (Item item : resultado) {
                System.out.println("  " + item);
            }
        }
        pausar();
    }

    private static void verDetalhes() {
        System.out.print("\nID do item: ");
        String id = scanner.nextLine().trim().toUpperCase();

        try {
            Item item = servico.buscarItem(id);
            System.out.println("\n── DETALHES ─────────────────────────");
            System.out.println(item.getDescricaoDetalhada()); // polimorfismo
            System.out.println("  Disponível : " + (item.isDisponivel() ? "✔ Sim" : "✘ Não"));
            System.out.println("  Cadastrado : " + item.getDataCadastro());
        } catch (ItemNaoEncontradoException e) {
            System.out.println("⚠ " + e.getMessage());
        }
        pausar();
    }

    // ─────────────────────────────────────────
    // AÇÕES DE EMPRÉSTIMO
    // ─────────────────────────────────────────

    private static void realizarEmprestimo() {
        System.out.print("\nMatrícula do usuário: ");
        String matricula = scanner.nextLine().trim().toUpperCase();
        System.out.print("ID do item: ");
        String idItem = scanner.nextLine().trim().toUpperCase();

        try {
            Emprestimo emprestimo = servico.realizarEmprestimo(matricula, idItem);
            System.out.println("\n✔ Empréstimo realizado com sucesso!");
            System.out.println("  " + emprestimo);
            System.out.println("  Prazo de devolução: " + emprestimo.getDataPrevistaDevolucao()
                    + " (" + Emprestimo.getPrazoDias() + " dias)");
        } catch (UsuarioNaoEncontradoException | ItemNaoEncontradoException |
                 ItemIndisponivelException | LimiteEmprestimosException e) {
            // Tratamento de múltiplas exceções checadas
            System.out.println("\n✘ Empréstimo não realizado: " + e.getMessage());
        } catch (ArquivoException e) {
            System.out.println("\n⚠ Empréstimo realizado, mas houve erro ao salvar: " + e.getMessage());
        }
        pausar();
    }

    private static void realizarDevolucao() {
        System.out.print("\nID do empréstimo (ex: EMP0001): ");
        String id = scanner.nextLine().trim().toUpperCase();

        try {
            Optional<Emprestimo> resultado = servico.realizarDevolucao(id);

            // Optional: processa apenas se presente
            resultado.ifPresentOrElse(
                    emprestimo -> {
                        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
                            System.out.println("\n✔ Devolução realizada com sucesso!");
                            System.out.println("  Item: " + emprestimo.getItem().getTitulo());
                            System.out.println("  Devolvido em: " + emprestimo.getDataDevolucaoEfetiva());
                            // Operador ternário para multa
                            double multa = emprestimo.calcularMulta();
                            String multaMsg = (multa > 0)
                                    ? String.format("  ⚠ Multa por atraso: R$ %.2f (%d dia(s))", multa, emprestimo.getDiasAtraso())
                                    : "  Sem multa por atraso.";
                            System.out.println(multaMsg);
                        }
                    },
                    () -> System.out.println("⚠ Não foi possível localizar o empréstimo informado.")
            );
        } catch (ArquivoException e) {
            System.out.println("\n⚠ Devolução registrada, mas houve erro ao salvar: " + e.getMessage());
        }
        pausar();
    }

    private static void listarEmprestimosAtivos() {
        List<Emprestimo> ativos = servico.listarEmprestimosAtivos();
        System.out.println("\n── EMPRÉSTIMOS ATIVOS (" + ativos.size() + ") ──────────────");
        if (ativos.isEmpty()) {
            System.out.println("  Nenhum empréstimo ativo.");
        } else {
            for (Emprestimo e : ativos) {
                System.out.println("  " + e);
            }
        }
        pausar();
    }

    private static void listarEmprestimosAtrasados() {
        List<Emprestimo> atrasados = servico.listarEmprestimosAtrasados();
        System.out.println("\n── EMPRÉSTIMOS ATRASADOS (" + atrasados.size() + ") ──────────");
        if (atrasados.isEmpty()) {
            System.out.println("  Nenhum empréstimo em atraso.");
        } else {
            for (Emprestimo e : atrasados) {
                System.out.printf("  %s | Dias de atraso: %d | Multa: R$ %.2f%n",
                        e.getId(), e.getDiasAtraso(), e.calcularMulta());
            }
        }
        pausar();
    }

    private static void listarHistoricoCompleto() {
        List<Emprestimo> todos = servico.listarEmprestimos();
        System.out.println("\n── HISTÓRICO COMPLETO (" + todos.size() + ") ──────────────");
        if (todos.isEmpty()) {
            System.out.println("  Nenhum empréstimo registrado.");
        } else {
            for (Emprestimo e : todos) {
                System.out.println("  " + e);
            }
        }
        pausar();
    }

    // ─────────────────────────────────────────
    // AÇÕES DE USUÁRIO
    // ─────────────────────────────────────────

    private static void listarUsuarios() {
        List<Usuario> usuarios = servico.listarUsuarios();
        System.out.println("\n── USUÁRIOS CADASTRADOS (" + usuarios.size() + ") ──────────");
        for (Usuario u : usuarios) {
            System.out.println("  " + u);
        }
        pausar();
    }

    private static void cadastrarUsuario() {
        System.out.print("\nMatrícula: ");
        String matricula = scanner.nextLine().trim().toUpperCase();
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();
        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();

        // Validação simples com operadores lógicos
        if (matricula.isEmpty() || nome.isEmpty() || email.isEmpty()) {
            System.out.println("⚠ Todos os campos são obrigatórios.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("⚠ E-mail inválido.");
            return;
        }

        Usuario usuario = new Usuario(matricula, nome, email);
        servico.cadastrarUsuario(usuario);
        System.out.println("✔ Usuário cadastrado: " + usuario);
        pausar();
    }

    // ─────────────────────────────────────────
    // UTILITÁRIOS
    // ─────────────────────────────────────────

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                String entrada = scanner.nextLine().trim();
                return Integer.parseInt(entrada); // Pode lançar NumberFormatException
            } catch (NumberFormatException e) {
                System.out.println("⚠ Por favor, digite apenas números inteiros.");
            }
        }
    }

    private static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    // ─────────────────────────────────────────
    // DADOS INICIAIS
    // ─────────────────────────────────────────

    private static void popularDadosIniciais() {
        // Livros — Herança de Item
        servico.adicionarItem(new Livro("L001", "Clean Code", "Robert C. Martin",
                2008, "978-0132350884", "Prentice Hall", 464));
        servico.adicionarItem(new Livro("L002", "Design Patterns", "GoF",
                1994, "978-0201633610", "Addison-Wesley", 395));
        servico.adicionarItem(new Livro("L003", "Java: Como Programar", "Deitel",
                2017, "978-8543004792", "Pearson", 992));
        servico.adicionarItem(new Livro("L004", "Estruturas de Dados em Java", "Michael Goodrich",
                2013, "978-8521637349", "Bookman", 736));
        servico.adicionarItem(new Livro("L005", "O Programador Pragmático", "Hunt e Thomas",
                1999, "978-8562261657", "Bookman", 352));

        // Revistas — Polimorfismo
        servico.adicionarItem(new Revista("R001", "IEEE Software", "IEEE",
                2024, "0740-7459", 41, "Engenharia de Software"));
        servico.adicionarItem(new Revista("R002", "Java Magazine", "Oracle",
                2023, "1553-6742", 88, "Programação"));
        servico.adicionarItem(new Revista("R003", "Communications of the ACM", "ACM",
                2024, "0001-0782", 67, "Ciência da Computação"));

        // Usuários
        servico.cadastrarUsuario(new Usuario("USR001", "Ana Lima", "ana.lima@email.com"));
        servico.cadastrarUsuario(new Usuario("USR002", "Bruno Souza", "bruno.souza@email.com"));
        servico.cadastrarUsuario(new Usuario("USR003", "Carla Mendes", "carla.mendes@email.com"));

        System.out.println("✔ Sistema iniciado com " +
                servico.listarTodosItens().size() + " itens e " +
                servico.listarUsuarios().size() + " usuários cadastrados.");
    }
}