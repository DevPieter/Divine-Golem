package nl.devpieter.divine.listeners;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import nl.devpieter.divine.events.PlayerListUpdateEvent;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.packet.IPacketListener;

import java.lang.reflect.Type;

public class PlayerListPacketListener implements IPacketListener<ClientboundPlayerInfoUpdatePacket> {

    private final Sees sees = Sees.getSharedInstance();

    @Override
    public Type getPacketType() {
        return ClientboundPlayerInfoUpdatePacket.class;
    }

    @Override
    public boolean onPacket(ClientboundPlayerInfoUpdatePacket packet) {
        sees.dispatch(new PlayerListUpdateEvent(packet.entries()));
        return false;
    }
}
