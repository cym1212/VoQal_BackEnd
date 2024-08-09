package Capstone.VoQal.infra.GeoIP.config;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class GeoIp2Config {

    @Value("${geoip2.database.path}")
    private String path;

    @Bean
    public DatabaseReader databaseReader() throws IOException, GeoIp2Exception {
        Resource resource = new ClassPathResource(path);
        return new DatabaseReader.Builder(resource.getInputStream()).build();
    }
}
