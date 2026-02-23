package nl.devpieter.divine.events.skyblock.protector;

import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.sees.event.SEvent;
import org.jetbrains.annotations.NotNull;

public record ProtectorStageUpdateEvent(
        @NotNull GolemStage previousStage,
        @NotNull GolemStage stage
) implements SEvent {
}
