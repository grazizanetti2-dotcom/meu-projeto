package biblioteca.util;

import biblioteca.exception.ArquivoException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Utilitário de arquivo usando Java NIO.
 * Demonstra: Path, Files, StandardCharsets, leitura e escrita de arquivos.
 */
public final class ArquivoUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Classe utilitária: construtor privado impede instanciação
    private ArquivoUtil() {}

    /**
     * Garante que o diretório de dados exista.
     * Usa Path.of() — API moderna do Java 11+.
     */
    public static void garantirDiretorio(String caminho) throws ArquivoException {
        try {
            Path path = Path.of(caminho);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new ArquivoException("Erro ao criar diretório: " + caminho, e);
        }
    }

    /**
     * Escreve uma lista de linhas em um arquivo (sobrescreve).
     */
    public static void escreverArquivo(String caminho, List<String> linhas) throws ArquivoException {
        try {
            Path path = Path.of(caminho);
            Files.write(path, linhas, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao escrever arquivo: " + caminho, e);
        }
    }

    /**
     * Lê todas as linhas de um arquivo.
     * Retorna lista vazia (imutável) se o arquivo não existir.
     */
    public static List<String> lerArquivo(String caminho) throws ArquivoException {
        try {
            Path path = Path.of(caminho);
            if (!Files.exists(path)) {
                return Collections.emptyList();
            }
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao ler arquivo: " + caminho, e);
        }
    }

    /**
     * Adiciona uma linha ao final de um arquivo de log.
     * Garante que o diretório pai exista antes de escrever.
     */
    public static void registrarLog(String caminhoLog, String mensagem) throws ArquivoException {
        try {
            Path path = Path.of(caminhoLog);

            // Garante que o diretório pai exista
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            String linha = String.format("[%s] %s%n", LocalDateTime.now().format(FORMATTER), mensagem);
            Files.writeString(path, linha, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao registrar log: " + caminhoLog, e);
        }
    }

    /**
     * Verifica se um arquivo existe.
     */
    public static boolean arquivoExiste(String caminho) {
        return Files.exists(Path.of(caminho));
    }
}