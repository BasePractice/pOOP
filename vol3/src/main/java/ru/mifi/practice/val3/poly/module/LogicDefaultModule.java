package ru.mifi.practice.val3.poly.module;

import dagger.Module;
import dagger.Provides;
import ru.mifi.practice.val3.cont.BusinessLogic;
import ru.mifi.practice.val3.cont.Location;
import ru.mifi.practice.val3.cont.Weather;
import ru.mifi.practice.val3.cont.business.DefaultLogic;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public final class LogicDefaultModule {
    @Provides
    @Singleton
    @Inject
    public BusinessLogic provideBusinessLogic(Location location, Weather weather) {
        return new DefaultLogic(location, weather);
    }
}
