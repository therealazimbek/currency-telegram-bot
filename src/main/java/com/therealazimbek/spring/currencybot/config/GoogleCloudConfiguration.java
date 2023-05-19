package com.therealazimbek.spring.currencybot.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudConfiguration {

    @Value("${google.credentials.file}")
    private String credentialsFile;

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        FileInputStream serviceAccountStream = new FileInputStream(credentialsFile);
        return GoogleCredentials.fromStream(serviceAccountStream);
    }

    @Bean
    public SpeechClient speechClient() throws IOException {
        SpeechSettings speechSettings = SpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(googleCredentials()))
                .build();

        return SpeechClient.create(speechSettings);
    }
}