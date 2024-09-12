package com.hase505.strategies.send;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class HTTPSendStrategy implements SendStrategy{
    String uri;
    HttpClient httpclient = HttpClient.newHttpClient();
    HttpRequest request = null;

    public HTTPSendStrategy(String uri){
        this.uri = uri;
    }

    @Override
    public void send(String encodedData) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(encodedData))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            httpclient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
