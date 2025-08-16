package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public record Capability(
    String id,
    String version,
    @NonNull Map<String, String> attributes
) {
    public Capability {
        Objects.requireNonNull(attributes, nullValue("attributes"));

        attributes
            = Map.copyOf(attributes);
    }
}
