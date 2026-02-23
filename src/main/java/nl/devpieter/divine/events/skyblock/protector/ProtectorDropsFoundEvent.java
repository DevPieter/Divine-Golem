package nl.devpieter.divine.events.skyblock.protector;

import nl.devpieter.divine.models.GolemDrop;
import nl.devpieter.sees.event.SEvent;

import java.util.List;

public record ProtectorDropsFoundEvent(List<GolemDrop> drops) implements SEvent {
}
