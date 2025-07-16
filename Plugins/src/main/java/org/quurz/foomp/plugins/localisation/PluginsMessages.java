package org.quurz.foomp.plugins.localisation;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public final class PluginsMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("ProxiesMessages", Locale.getDefault());

    private PluginsMessages() {}

    public static String notAnInterface(final String className) {
        Objects.requireNonNull(className);
        return String.format(RESOURCE_BUNDLE.getString("NOT_AN_INTERFACE"), className);
    }

    public static String notAConcreteClass(final String className) {
        Objects.requireNonNull(className);
        return String.format(RESOURCE_BUNDLE.getString("NOT_A_CONCRETE_CLASS"), className);
    }

    public static String failedToCreateProxyClass(final Class<?> contractClass,
                                                  final String canonicalName) {
        Objects.requireNonNull(contractClass);
        Objects.requireNonNull(canonicalName);
        return String.format(
            RESOURCE_BUNDLE.getString("FAILED_TO_CREATE_PROXY_CLASS"),
            contractClass.getName(),
            canonicalName
        );
    }

    public static String failedToCreateProxyInstance(final Class<?> proxyClass) {
        Objects.requireNonNull(proxyClass);
        return String.format(
            RESOURCE_BUNDLE.getString("FAILED_TO_CRATE_PROXY_INSTANCE"),
            proxyClass.getName()
        );
    }

}
