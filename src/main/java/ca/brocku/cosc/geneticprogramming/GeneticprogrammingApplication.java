package ca.brocku.cosc.geneticprogramming;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
public class GeneticprogrammingApplication implements WebMvcConfigurer, CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GeneticprogrammingApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void run(String... args) throws Exception {
        File trainingdata = new File("trainingdata.txt");
        if(!trainingdata.exists()){
            try {
                Files.copy(new ClassPathResource("regression/exampledata.txt").getInputStream(), trainingdata.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File resultsDirectory = new File("results");
        if(!resultsDirectory.exists()){
            resultsDirectory.mkdir();
        }

    }
}
