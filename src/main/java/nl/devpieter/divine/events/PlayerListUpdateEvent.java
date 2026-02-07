package nl.devpieter.divine.events;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import nl.devpieter.sees.event.SEvent;

import java.util.List;

public record PlayerListUpdateEvent(List<PlayerListS2CPacket.Entry> entries) implements SEvent {
}
