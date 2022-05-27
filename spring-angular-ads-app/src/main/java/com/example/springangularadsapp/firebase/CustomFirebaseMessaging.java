package com.example.springangularadsapp.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFirebaseMessaging {
    private FirebaseApp app;

    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("/firebase/pwads-app-firebase-adminsdk-s1o5f-5d2c784e03.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();

        try {
            app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
        } catch (Exception ignored) {

        }
        return FirebaseMessaging.getInstance(app);
    }
}
