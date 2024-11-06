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
            System.err.println("URI inválida");
            return;
        } catch (IllegalArgumentException e){
            System.err.println("URI inválida");
            return;
        }


        try {
            System.out.println("Enviando dados via HTTP(s)");
            httpclient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Dados enviados");

        } catch (IOException | InterruptedException e) {
            System.err.println("Não foi possível enviar dados via HTTP(s)");
            return;
        }
    }
}
