package nl.devpieter.divine.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import nl.devpieter.divine.events.ConnectToServerEvent;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.client.task.TaskManager;
import nl.devpieter.utilize.client.task.enums.TickPhase;
import nl.devpieter.utilize.client.task.tasks.RunLaterTask;
import nl.devpieter.utilize.client.utils.ClientUtils;
import nl.devpieter.utilize.packet.IPacketListener;

import java.lang.reflect.Type;

public class GameJoinPacketListener implements IPacketListener<ClientboundLoginPacket> {

    private final Sees sees = Sees.getSharedInstance();
    private final TaskManager taskManager = TaskManager.getInstance();

    @Override
    public Type getPacketType() {
        return ClientboundLoginPacket.class;
    }

    @Override
    public boolean onPacket(ClientboundLoginPacket gameJoinS2CPacket) {
        taskManager.addTask(new RunLaterTask(this::handleGameJoin, 10), TickPhase.PLAYER_TAIL);
        return false;
    }

    private void handleGameJoin() {
        Minecraft client = ClientUtils.getClient();
        if (client.getCurrentServer() == null) return;

        String ip = client.getCurrentServer().ip;
        sees.dispatch(new ConnectToServerEvent(ip));
    }
}
