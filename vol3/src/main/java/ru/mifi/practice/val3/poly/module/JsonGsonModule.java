package ru.mifi.practice.val3.poly.module;

import dagger.Module;
import dagger.Provides;
import ru.mifi.practice.val3.cont.Deserializer;
import ru.mifi.practice.val3.cont.Factory;
import ru.mifi.practice.val3.cont.deserialize.GsonJson;

import javax.inject.Singleton;

@Module
public final class JsonGsonModule {
    @Provides
    @Singleton
    public Factory<Deserializer> provideJson() {
        return GsonJson::new;
    }
}
