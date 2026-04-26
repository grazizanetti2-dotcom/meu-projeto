# 📚 Sistema de Gerenciamento de Biblioteca
 
Projeto acadêmico desenvolvido em **Java 21** para gerenciar o empréstimo de livros em uma biblioteca, com menu interativo em console.
 
---
 
## 🎯 Conceitos Java Aplicados
 
| Conceito | Onde é aplicado |
|---|---|
| **Tipos de dados** | `int`, `double`, `boolean`, `String`, `long` em todas as classes |
| **Operadores** | Aritméticos (multa), relacionais, lógicos (`&&`, `||`), ternário |
| **Controle de fluxo** | `if/else`, `switch` expressions (Java 14+) |
| **Estruturas de repetição** | `while` (menu), `for-each` (listagens) |
| **Encapsulamento** | Atributos `private final` com getters em todas as classes |
| **Herança** | `Livro` e `Revista` herdam de `Item` (classe abstrata) |
| **Polimorfismo** | `getTipoItem()`, `getDescricaoDetalhada()`, `toString()` sobrepostos |
| **Exceções** | 5 exceções checadas customizadas, tratadas com multi-catch |
| **Java NIO** | `Path.of()`, `Files`, `StandardCharsets`, `StandardOpenOption` |
| **Java Time** | `LocalDate`, `LocalDateTime`, `ChronoUnit`, `DateTimeFormatter` |
| **Optional** | `Optional<Emprestimo>` em `realizarDevolucao` — elimina retorno `null` |
 
---
 
## 🐛 Correções aplicadas (Java 21 clean)
 
| Problema | Correção |
|---|---|
| Import `Collectors` não utilizado | Removido — causa warning de compilação |
| `atualizarStatus()` dentro de `toString()` | Removido — `toString()` jamais deve alterar estado |
| `realizarDevolucao` retornava `null` | Substituído por `Optional<Emprestimo>` |
| `null` checado manualmente em `Main` | Substituído por `Optional.ifPresentOrElse()` |
| Campos imutáveis sem `final` | Todos os campos que não mudam são agora `final` |
| `Paths.get()` (legado) | Substituído por `Path.of()` (Java 11+) |
| `ArquivoUtil` instanciável | Construtor `private` adicionado (classe utilitária) |
| `registrarLog` não garantia diretório pai | Verificação `createDirectories` adicionada |
 
---
 
## 🏗️ Estrutura do Projeto
 
```
biblioteca/
├── README.md
├── .gitignore
└── src/main/java/biblioteca/
    ├── Main.java
    ├── model/
    │   ├── Item.java           ← Classe abstrata base (campos final)
    │   ├── Livro.java          ← Herda Item
    │   ├── Revista.java        ← Herda Item
    │   ├── Usuario.java
    │   └── Emprestimo.java     ← Java Time + toString() puro
    ├── service/
    │   └── BibliotecaService.java   ← Lógica de negócio + Optional
    ├── exception/
    │   ├── ItemNaoEncontradoException.java
    │   ├── ItemIndisponivelException.java
    │   ├── LimiteEmprestimosException.java
    │   ├── UsuarioNaoEncontradoException.java
    │   └── ArquivoException.java
    └── util/
        └── ArquivoUtil.java    ← Java NIO com Path.of()
```
 
---
 
## ▶️ Como executar no IntelliJ IDEA
 
1. `File → Open` → selecione a pasta `biblioteca/`
2. `File → Project Structure → SDK` → selecione **Java 21**
3. Clique direito em `src/main/java` → `Mark Directory as → Sources Root`
4. Abra `Main.java` e clique ▶
> A pasta `data/` é criada automaticamente na primeira execução.
 
---
 
 
## 📐 Regras de negócio
 
- Máximo **3 empréstimos simultâneos** por usuário
- Prazo de devolução: **14 dias**
- Multa por atraso: **R$ 0,50/dia**
- Dados salvos em `data/` via Java NIO
