package ru.mifi.practice.vol7.proxy;

import java.lang.reflect.Proxy;

public final class SimpleProxy {

    private int doSomething2() {
        return 1;
    }

    public Simple createProxy(Simple simple) {
        return (Simple) Proxy.newProxyInstance(SimpleProxy.class.getClassLoader(),
            new Class[]{Simple.class}, (proxy, method, args) -> {
                if ("doSomething".equals(method.getName())) {
                    return doSomething2();
                }
                return method.invoke(simple, args);
            });
    }
}
