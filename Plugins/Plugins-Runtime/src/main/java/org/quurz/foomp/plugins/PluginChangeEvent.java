package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public sealed abstract class PluginChangeEvent<A>
        permits PluginChangeEvent.PluginLoadEvent,
                PluginChangeEvent.PluginUnloadEvent {

    static <A> PluginLoadEvent<A> pluginLoadEvent(final @NonNull A plugin) {
        Objects.requireNonNull(plugin, nullValue("plugin"));
        return new PluginLoadEvent<>(plugin);
    }

    static <A> PluginUnloadEvent<A> pluginUnloadEvent() {
        return new PluginUnloadEvent<>();
    }

    protected boolean veto;

    protected PluginChangeEvent() {
        this.veto
            = false;
    }

    void setVeto() {
        this.veto
            = true;
    }

    public boolean isVeto() {
        return this.veto;
    }

    final static class PluginLoadEvent<A>
            extends PluginChangeEvent<A> {

        private final A plugin;

        private PluginLoadEvent(final @NonNull A plugin) {
            super();
            this.plugin
                = plugin;
        }

        public A getPlugin() {
            return this.plugin;
        }

    }

    final static class PluginUnloadEvent<A>
            extends PluginChangeEvent<A> {

        private PluginUnloadEvent() {
            super();
        }

    }

}
