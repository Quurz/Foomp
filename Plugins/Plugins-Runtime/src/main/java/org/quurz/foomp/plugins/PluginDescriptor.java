package org.quurz.foomp.plugins;

import java.util.List;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public record PluginDescriptor(
    String id,
    String version,
    String displayName,
    String vendor,
    List<String> requiresHostApi,
    List<Capability> provides
) {
    public PluginDescriptor {
        Objects.requireNonNull(requiresHostApi, nullValue("requiresHostApi"));
        Objects.requireNonNull(provides, nullValue("provides"));

        requiresHostApi
            = List.copyOf(requiresHostApi);
        provides
            = List.copyOf(provides);
    }
}
