package nl.devpieter.divine;

import nl.devpieter.divine.events.ConnectToServerEvent;
import nl.devpieter.divine.events.skyblock.SkyblockLocationUpdateEvent;
import nl.devpieter.divine.models.HypixelLocation;
import nl.devpieter.divine.utils.HypixelUtils;
import nl.devpieter.sees.Sees;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.events.chat.ReceiveMessageEvent;
import nl.devpieter.utilize.task.TaskManager;
import nl.devpieter.utilize.task.tasks.RunLaterTask;
import nl.devpieter.utilize.utils.common.RandomUtils;
import nl.devpieter.utilize.utils.minecraft.NetworkUtils;
import org.jetbrains.annotations.Nullable;

public class HypixelManager implements SListener {

    private static final HypixelManager INSTANCE = new HypixelManager();

    private final Sees sees = Sees.getSharedInstance();

    private boolean hasReceivedLocRaw = false;
    private boolean requestingLocRaw = false;
    private int locRawRequestCounter = 0;

    private boolean isOnHypixel = false;
    private @Nullable HypixelLocation currentLocation = null;

    private HypixelManager() {
    }

    public static HypixelManager getInstance() {
        return INSTANCE;
    }

    public boolean isOnHypixel() {
        return isOnHypixel;
    }

    public boolean isInSkyblock() {
        if (!isOnHypixel || currentLocation == null) return false;
        return "skyblock".equalsIgnoreCase(currentLocation.gametype());
    }

    public boolean isInTheEnd() {
        if (!isInSkyblock() || currentLocation == null || currentLocation.map() == null) return false;
        return "the end".equalsIgnoreCase(currentLocation.map());
    }

    @SEventListener
    private void onConnectToServer(ConnectToServerEvent event) {
        stopRequestingLocRaw();
        hasReceivedLocRaw = false;

        isOnHypixel = HypixelUtils.isHypixelServer(event.ip());
        if (isOnHypixel) startRequestingLocRaw();
    }

    @SEventListener
    private void onReceiveMessage(ReceiveMessageEvent event) {
        if (!isOnHypixel || hasReceivedLocRaw) return;

        String message = event.message().getString();
        if (message.isEmpty()) return;

        HypixelLocation loc = HypixelUtils.parseLocRaw(message);
        if (loc == null) return;

        stopRequestingLocRaw();
        hasReceivedLocRaw = true;

        boolean wasInSkyblock = isInSkyblock();
        boolean wasInTheEnd = isInTheEnd();

        HypixelLocation previousLocation = currentLocation;
        currentLocation = loc;

        event.cancel();

        boolean isInSkyblock = isInSkyblock();
        boolean isInTheEnd = isInTheEnd();

        if (!isInSkyblock && !wasInSkyblock) return;

        sees.dispatch(new SkyblockLocationUpdateEvent(
                wasInSkyblock,
                isInSkyblock,
                wasInTheEnd,
                isInTheEnd,
                previousLocation,
                currentLocation
        ));
    }

    // Unfortunately, some Hypixel mods don't play well with other mods requesting locraw,
    // so we have to keep requesting it until we get a response, or give up after a certain amount of tries.
    private void startRequestingLocRaw() {
        requestingLocRaw = true;
        locRawRequestCounter = 0;

        requestLocRaw();
    }

    private void stopRequestingLocRaw() {
        requestingLocRaw = false;
        locRawRequestCounter = 0;
    }

    private void requestLocRaw() {
        if (!requestingLocRaw) return;

        NetworkUtils.sendChatCommand("locraw");
        locRawRequestCounter++;

        if (locRawRequestCounter > 5) {
            requestingLocRaw = false;
            return;
        }

        int jitter = RandomUtils.randomIntInclusive(3, 7);

        TaskManager.getInstance().addTask(new RunLaterTask(() -> {
            if (requestingLocRaw) requestLocRaw();
        }, 20 * jitter), TaskManager.TickPhase.PLAYER_TAIL);
    }
}
