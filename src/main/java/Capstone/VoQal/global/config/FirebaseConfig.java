package Capstone.VoQal.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;
    private final FirebaseProperties firebaseProperties;


    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // 설정 파일 경로로부터 리소스를 로드
        InputStream serviceAccount = resourceLoader.getResource(firebaseProperties.getConfigFile()).getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseProperties.getDatabaseUrl())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }
}
