package nl.devpieter.divine.events.skyblock.protector;

import nl.devpieter.sees.event.SEvent;

public record ProtectorFightEndEvent(boolean canceled) implements SEvent {
}
