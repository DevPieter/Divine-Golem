package nl.devpieter.divine.events.skyblock.protector;

import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.sees.event.SEvent;
import org.jetbrains.annotations.NotNull;

public record ProtectorLocationUpdateEvent(
        @NotNull GolemLocation previousLocation,
        @NotNull GolemLocation location
) implements SEvent {
}
