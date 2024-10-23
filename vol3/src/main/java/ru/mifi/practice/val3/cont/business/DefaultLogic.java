package ru.mifi.practice.val3.cont.business;

import ru.mifi.practice.val3.cont.BusinessLogic;
import ru.mifi.practice.val3.cont.Deserializer;
import ru.mifi.practice.val3.cont.Factory;
import ru.mifi.practice.val3.cont.Http;
import ru.mifi.practice.val3.cont.Location;
import ru.mifi.practice.val3.cont.Weather;
import ru.mifi.practice.val3.cont.deserialize.GsonJson;
import ru.mifi.practice.val3.cont.http.Jdk;
import ru.mifi.practice.val3.cont.sevices.OpenMeteo;
import ru.mifi.practice.val3.cont.sevices.WhoIs;

public record DefaultLogic(Location location, Weather weather) implements BusinessLogic {
    @Override
    public void execute() {
        location.get().flatMap(place -> weather.get(place.latitude(), place.longitude())).ifPresent(System.out::println);
    }

    public static final class Default implements BusinessLogic {
        private final Factory<Location> locationFactory;
        private final Factory<Weather> weatherFactory;

        public Default(Factory<Http> httpFactory) {
            this.locationFactory = () -> new WhoIs(httpFactory.create());
            this.weatherFactory = () -> new OpenMeteo(httpFactory.create());
        }

        public Default() {
            this(httpFactory());
        }

        private static Factory<Deserializer> jsonFactory() {
            return GsonJson::new;
        }

        private static Factory<Http> httpFactory() {
            return () -> new Jdk(jsonFactory().create());
        }

        @Override
        public void execute() {
            new DefaultLogic(locationFactory.create(), weatherFactory.create()).execute();
        }
    }
}
