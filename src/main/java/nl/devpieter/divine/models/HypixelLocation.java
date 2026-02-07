package nl.devpieter.divine.models;

import org.jetbrains.annotations.Nullable;

public record HypixelLocation(
        String server,
        String gametype,
        @Nullable String map
) {
}