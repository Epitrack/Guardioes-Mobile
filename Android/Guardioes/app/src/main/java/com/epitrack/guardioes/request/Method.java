package com.epitrack.guardioes.request;

/**
 * @author Igor Morais
 */
public enum Method {

    OPTIONS     ("OPTIONS"),
    GET         ("GET"),
    HEAD        ("HEAD"),
    POST        ("POST"),
    PUT         ("PUT"),
    PATCH       ("PATCH"),
    DELETE      ("DELETE"),
    TRACE       ("TRACE"),
    CONNECT     ("CONNECT");

    private final String name;

    Method(final String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public static Method getBy(final String name) {

        for (final Method method : Method.values()) {

            if (method.getName().equals(name)) {
                return method;
            }
        }

        throw new IllegalArgumentException("The Method has not found.");
    }
}
