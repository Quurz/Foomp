package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class PluginManifest {

    public static PluginManifest pluginManifest(final @NonNull InputStream inputStream) {
        Objects.requireNonNull(inputStream, nullValue("inputStream"));
        return null;    // TODO
    }

}
