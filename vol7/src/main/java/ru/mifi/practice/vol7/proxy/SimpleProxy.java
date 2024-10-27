package ru.mifi.practice.vol7.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class SimpleProxy {

    private int doSomething() {
        return 1;
    }

    public Simple createProxy(Simple simple) {
        return (Simple) Proxy.newProxyInstance(SimpleProxy.class.getClassLoader(),
            new Class[]{Simple.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if ("doSomething".equals(method.getName())) {
                        return doSomething();
                    }
                    return method.invoke(simple, args);
                }
            });
    }
}
