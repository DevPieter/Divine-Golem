package nl.devpieter.divine.listeners;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import nl.devpieter.divine.events.PlayerListUpdateEvent;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.listeners.packet.IPacketListener;

import java.lang.reflect.Type;

public class PlayerListPacketListener implements IPacketListener<PlayerListS2CPacket> {

    private final Sees sees = Sees.getSharedInstance();

    @Override
    public Type getPacketType() {
        return PlayerListS2CPacket.class;
    }

    @Override
    public boolean onPacket(PlayerListS2CPacket packet) {
        sees.dispatch(new PlayerListUpdateEvent(packet.getEntries()));
        return false;
    }
}
