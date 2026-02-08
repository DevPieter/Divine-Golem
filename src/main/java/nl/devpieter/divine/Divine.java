package nl.devpieter.divine;

import net.fabricmc.api.ClientModInitializer;
import nl.devpieter.divine.listeners.GameJoinPacketListener;
import nl.devpieter.divine.listeners.PlayerListPacketListener;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.managers.PacketManager;

public class Divine implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Sees sees = Sees.getSharedInstance();
        sees.subscribe(HypixelManager.getInstance());
        sees.subscribe(GolemManager.getInstance());

        PacketManager packetManager = PacketManager.getInstance();
        packetManager.subscribe(new GameJoinPacketListener());
        packetManager.subscribe(new PlayerListPacketListener());
    }
}
