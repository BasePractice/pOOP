package ru.mifi.practice.val3.poly.module;

import dagger.Module;
import dagger.Provides;
import ru.mifi.practice.val3.cont.Deserializer;
import ru.mifi.practice.val3.cont.Factory;
import ru.mifi.practice.val3.cont.Http;
import ru.mifi.practice.val3.cont.http.Ok;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public final class HttpOkModule {
    @Provides
    @Singleton
    @Inject
    public Factory<Http> provideHttp(Factory<Deserializer> deFactory) {
        return () -> new Ok(deFactory.create());
    }
}
