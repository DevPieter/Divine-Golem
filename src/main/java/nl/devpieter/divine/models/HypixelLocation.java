package nl.devpieter.divine.models;

import org.jetbrains.annotations.Nullable;

public record HypixelLocation(
        String server,
        @Nullable String gametype,
        @Nullable String map
) {
}