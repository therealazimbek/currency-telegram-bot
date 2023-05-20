package com.therealazimbek.spring.currencybot.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class SpeechToTextService {

    @Value("${google.credentials.file}")
    private String credentialsPath;

    public String transcribeSpeech(byte[] audioData) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        SpeechSettings speechSettings = SpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioData))
                    .build();

            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            StringBuilder transcription = new StringBuilder();

            for (SpeechRecognitionResult result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                transcription.append(alternative.getTranscript());
            }

            return transcription.toString();
        }
    }
}
