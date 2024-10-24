package ru.mifi.practice.val3.cont.http;

import ru.mifi.practice.val3.cont.Deserializer;
import ru.mifi.practice.val3.cont.Http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class Jdk implements Http {
    private final HttpClient client;
    private final Deserializer deserializer;

    public Jdk(Deserializer deserializer) {
        this.client = HttpClient.newBuilder().build();
        this.deserializer = deserializer;
    }

    @Override
    public <T> Optional<T> get(String url, Class<T> clazz) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();
            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return Optional.of(deserializer.deserialize(response.body(), clazz));
            } else {
                System.out.println(response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
