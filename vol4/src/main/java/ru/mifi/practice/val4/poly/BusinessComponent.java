package ru.mifi.practice.val4.poly;

import dagger.Component;
import ru.mifi.practice.val3.cont.BusinessLogic;
import ru.mifi.practice.val4.poly.module.HttpJdkModule;
import ru.mifi.practice.val4.poly.module.JsonGsonModule;
import ru.mifi.practice.val4.poly.module.LogicDefaultModule;
import ru.mifi.practice.val4.poly.module.ServiceDefaultModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ServiceDefaultModule.class, HttpJdkModule.class, JsonGsonModule.class, LogicDefaultModule.class})
public interface BusinessComponent {
    BusinessLogic businessLogic();
}
