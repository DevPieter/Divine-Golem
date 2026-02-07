package nl.devpieter.divine;

import nl.devpieter.divine.events.ConnectToServerEvent;
import nl.devpieter.divine.events.SkyblockLocationUpdateEvent;
import nl.devpieter.divine.models.HypixelLocation;
import nl.devpieter.divine.utils.HypixelUtils;
import nl.devpieter.sees.Sees;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.events.chat.ReceiveMessageEvent;
import nl.devpieter.utilize.utils.minecraft.NetworkUtils;
import org.jetbrains.annotations.Nullable;

public class HypixelManager implements SListener {

    private static final HypixelManager INSTANCE = new HypixelManager();

    private final Sees sees = Sees.getSharedInstance();

    private boolean isOnHypixel = false;
    private boolean hasReceivedLocRaw = false;
    private @Nullable HypixelLocation currentLocation = null;

    private HypixelManager() {
    }

    public static HypixelManager getInstance() {
        return INSTANCE;
    }

    @SEventListener
    private void onConnectToServer(ConnectToServerEvent event) {
        hasReceivedLocRaw = false;

        isOnHypixel = HypixelUtils.isHypixelServer(event.ip());
        if (isOnHypixel) NetworkUtils.sendChatCommand("locraw"); // We may need to add some jitter
    }

    @SEventListener
    private void onReceiveMessage(ReceiveMessageEvent event) {
        if (!isOnHypixel || hasReceivedLocRaw) return;

        String message = event.message().getString();
        if (message.isEmpty()) return;

        HypixelLocation loc = HypixelUtils.parseLocRaw(message);
        if (loc == null) return;

        HypixelLocation previousLocation = currentLocation;
        boolean wasInSkyblock = isInSkyblock();
        boolean wasInTheEnd = isInTheEnd();

        currentLocation = loc;
        hasReceivedLocRaw = true;

        event.cancel();

        boolean isInSkyblock = isInSkyblock();
        boolean isInTheEnd = isInTheEnd();

        sees.dispatch(new SkyblockLocationUpdateEvent(
                wasInSkyblock,
                isInSkyblock,
                wasInTheEnd,
                isInTheEnd,
                previousLocation,
                currentLocation
        ));
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
}
