package Capstone.VoQal.global.firebase.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    // Firebase Custom Token 생성 메서드 (ID만 포함)
    public String createFirebaseCustomToken(String userId) throws FirebaseAuthException {
        // Firebase Custom Token 생성 (ID만 포함)
        return FirebaseAuth.getInstance().createCustomToken(userId);
    }
}