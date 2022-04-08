package carsharing.config;

import carsharing.CommandLineInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineConfig {

    @Bean
    CommandLineRunner cli(CommandLineInterface commandLineInterface) {
        return args -> commandLineInterface.run();
    }
}
