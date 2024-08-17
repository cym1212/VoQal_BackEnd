package Capstone.VoQal.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.firebase")
public class FirebaseProperties {
    private String databaseUrl;
    private String configFile;
}
