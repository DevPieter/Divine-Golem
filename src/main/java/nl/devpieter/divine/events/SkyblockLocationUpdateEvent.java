package nl.devpieter.divine.events;

import nl.devpieter.divine.models.HypixelLocation;
import nl.devpieter.sees.event.SEvent;
import org.jetbrains.annotations.Nullable;

public record SkyblockLocationUpdateEvent(
        boolean wasInSkyblock,
        boolean isInSkyblock,
        boolean wasInTheEnd,
        boolean isInTheEnd,
        @Nullable HypixelLocation previousLocation,
        @Nullable HypixelLocation location
) implements SEvent {
}
