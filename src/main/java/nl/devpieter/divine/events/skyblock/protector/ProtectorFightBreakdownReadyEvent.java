package nl.devpieter.divine.events.skyblock.protector;

import nl.devpieter.divine.models.fightBreakdown.ProtectorFightBreakdown;
import nl.devpieter.sees.event.SEvent;

public record ProtectorFightBreakdownReadyEvent(ProtectorFightBreakdown breakdown) implements SEvent {
}
