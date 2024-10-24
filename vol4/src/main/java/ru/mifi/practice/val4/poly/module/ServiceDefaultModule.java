package ru.mifi.practice.val4.poly.module;

import dagger.Module;
import dagger.Provides;
import ru.mifi.practice.val3.cont.Factory;
import ru.mifi.practice.val3.cont.Http;
import ru.mifi.practice.val3.cont.Location;
import ru.mifi.practice.val3.cont.Weather;
import ru.mifi.practice.val3.cont.sevices.OpenMeteo;
import ru.mifi.practice.val3.cont.sevices.WhoIs;

import javax.inject.Singleton;

@Module
public final class ServiceDefaultModule {
    @Provides
    @Singleton
    public Location provideLocation(Factory<Http> httpFactory) {
        return new WhoIs(httpFactory.create());
    }

    @Provides
    @Singleton
    public Weather provideWeather(Factory<Http> httpFactory) {
        return new OpenMeteo(httpFactory.create());
    }
}
