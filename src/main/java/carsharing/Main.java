package carsharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

@SpringBootApplication
public class Main {

    private static final String DATABASE_FILE_NAME_ARG = "-databaseFileName";
    private static final String DEFAULT_DATABASE_FILE_NAME = "carsharing";

    private static String databasePath = null;

    public static void main(String[] args) {
        tryToReadDatabasePath(args);
        SpringApplication.run(Main.class, args);
    }

    private static void tryToReadDatabasePath(String[] args) {
        String dbName = DEFAULT_DATABASE_FILE_NAME;
        for (int i = 0; i < args.length - 1; i++) {
            if (Objects.equals(args[i], DATABASE_FILE_NAME_ARG)) {
                dbName = args[i + 1];
            }
        }
        databasePath = "./src/carsharing/db/" + dbName;
    }

    @Bean
    String dbPath() {
        return databasePath;
    }

}