package ru.mifi.practice.val3.cont.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.mifi.practice.val3.cont.Deserializer;
import ru.mifi.practice.val3.cont.Http;

import java.io.IOException;
import java.util.Optional;

public final class Ok implements Http {
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final Deserializer deserializer;

    public Ok(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public <T> Optional<T> get(String url, Class<T> clazz) {
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                try (ResponseBody body = responseBody) {
                    String content = body.string();
                    return Optional.of(deserializer.deserialize(content, clazz));
                }
            } else {
                System.err.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
