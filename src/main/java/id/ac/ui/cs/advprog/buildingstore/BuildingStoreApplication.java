package id.ac.ui.cs.advprog.buildingstore;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class BuildingStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildingStoreApplication.class, args);
    }

}
