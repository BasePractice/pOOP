package ru.mifi.practice.vol6.model;

import com.google.gson.annotations.SerializedName;
import ru.mifi.practice.vol6.security.Security;

import java.io.Serializable;
import java.util.Optional;

public record User(@SerializedName("username") String username,
                   @SerializedName("password") String secret) implements Serializable {
    public boolean equalsSecret(String password, Security.Hash hash) {
        return Optional.ofNullable(secret)
            .map(s -> s.equals(hash.hash(password))).orElse(false);
    }
}
