package Capstone.VoQal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "Capstone.VoQal.domain")
public class VoQalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoQalApplication.class, args);
	}

}
