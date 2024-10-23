package ru.mifi.practice.val3.cont.deserialize;

import com.google.gson.Gson;
import ru.mifi.practice.val3.cont.Deserializer;

public final class GsonJson implements Deserializer {
    private final Gson gson = new Gson();

    public <T> T deserialize(String text, Class<T> clazz) {
        return gson.fromJson(text, clazz);
    }
}
