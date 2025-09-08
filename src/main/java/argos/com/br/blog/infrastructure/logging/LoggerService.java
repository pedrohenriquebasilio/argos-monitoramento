package argos.com.br.blog.infrastructure.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class LoggerService {

    private static final String LOG_DIRECTORY = "logs";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LoggerService() {
        createLogDirectory();
    }

    private void createLogDirectory() {
        Path logPath = Paths.get(LOG_DIRECTORY);
        if (!Files.exists(logPath)) {
            try {
                Files.createDirectories(logPath);
            } catch (IOException e) {
                log.error("Erro ao criar diretório de logs: {}", e.getMessage());
            }
        }
    }

    public void logRequest(String service, String method, Object request) {
        String logMessage = String.format("%s - Requisição recebida em %s - Método: %s - Dados: %s", 
                getCurrentTime(), service, method, request);
        writeLog(logMessage);
    }

    public void logResponse(String service, String method, Object response) {
        String logMessage = String.format("%s - Resposta enviada de %s - Método: %s - Dados: %s", 
                getCurrentTime(), service, method, response);
        writeLog(logMessage);
    }

    public void logError(String service, String method, Exception exception) {
        String logMessage = String.format("%s - Erro em %s - Método: %s - Erro: %s", 
                getCurrentTime(), service, method, exception.getMessage());
        writeLog(logMessage);
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }

    private void writeLog(String logMessage) {
        String fileName = LocalDateTime.now().format(DATE_FORMATTER) + ".log";
        File logFile = new File(LOG_DIRECTORY + File.separator + fileName);
        
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            log.error("Erro ao escrever no arquivo de log: {}", e.getMessage());
        }
    }
}