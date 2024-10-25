package ru.mifi.practice.vol6.model;

import com.google.gson.annotations.SerializedName;
import ru.mifi.practice.vol6.Security;

import java.io.Serializable;

public record User(@SerializedName("username") String username,
                   @SerializedName("password") String secret) implements Serializable {

    public boolean equalsSecret(String password, Security.Hash hash) {
        return secret.equals(hash.hash(password));
    }
}
