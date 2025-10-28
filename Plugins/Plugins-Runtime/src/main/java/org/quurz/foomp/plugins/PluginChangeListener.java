package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface PluginChangeListener {

    void onPluginLoad(final @NonNull PluginChangeEvent<?> event);

    void onPluginUnload(final @NonNull PluginChangeEvent<?> event);

}
