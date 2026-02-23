package nl.devpieter.divine.events.skyblock;

import nl.devpieter.divine.models.HypixelLocation;
import nl.devpieter.sees.event.SEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SkyblockLocationUpdateEvent(
        boolean wasInSkyblock,
        boolean isInSkyblock,
        boolean wasInTheEnd,
        boolean isInTheEnd,
        @Nullable HypixelLocation previousLocation,
        @NotNull HypixelLocation location
) implements SEvent {
}
