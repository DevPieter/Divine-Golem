package nl.devpieter.divine.listeners;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import nl.devpieter.divine.events.ConnectToServerEvent;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.listeners.packet.IPacketListener;
import nl.devpieter.utilize.task.TaskManager;
import nl.devpieter.utilize.task.tasks.RunLaterTask;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;

import java.lang.reflect.Type;

public class GameJoinPacketListener implements IPacketListener<GameJoinS2CPacket> {

    private final Sees sees = Sees.getSharedInstance();
    private final TaskManager taskManager = TaskManager.getInstance();

    @Override
    public Type getPacketType() {
        return GameJoinS2CPacket.class;
    }

    @Override
    public boolean onPacket(GameJoinS2CPacket gameJoinS2CPacket) {
        taskManager.addTask(new RunLaterTask(this::handleGameJoin, 10), TaskManager.TickPhase.PLAYER_TAIL);
        return false;
    }

    private void handleGameJoin() {
        MinecraftClient client = ClientUtils.getClient();
        if (client.world == null || client.getCurrentServerEntry() == null) return;

        String ip = client.getCurrentServerEntry().address;
        sees.dispatch(new ConnectToServerEvent(ip));
    }
}
