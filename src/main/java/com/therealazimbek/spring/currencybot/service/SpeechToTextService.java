package com.therealazimbek.spring.currencybot.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpeechToTextService {

    private final SpeechClient speechClient;

    public SpeechToTextService(@Autowired SpeechClient speechClient) {
        this.speechClient = speechClient;
    }

    public String transcribeAudio(byte[] audioData) throws IOException {
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("en-US")
                .build();

        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioData))
                .build();

        RecognizeResponse response = speechClient.recognize(config, audio);

        StringBuilder transcription = new StringBuilder();
        for (SpeechRecognitionResult result : response.getResultsList()) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            transcription.append(alternative.getTranscript());
        }

        return transcription.toString();
    }
}
