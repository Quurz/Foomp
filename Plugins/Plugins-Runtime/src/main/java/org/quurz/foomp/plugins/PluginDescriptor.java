package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class PluginDescriptor {

    public static PluginDescriptor pluginDescriptor(final @NonNull PluginCoordinate coordinate) {
        Objects.requireNonNull(coordinate, nullValue("coordinate"));
        return new PluginDescriptor(coordinate);
    }

    private final PluginCoordinate coordinate;

    private PluginDescriptor(final PluginCoordinate coordinate) {
        this.coordinate
            = coordinate;
    }

    public PluginCoordinate getCoordinate() {
        return this.coordinate;
    }

}
