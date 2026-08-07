package nl.devpieter.divine.events;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import nl.devpieter.sees.event.SEvent;

import java.util.List;

public record PlayerListUpdateEvent(List<ClientboundPlayerInfoUpdatePacket.Entry> entries) implements SEvent {
}
