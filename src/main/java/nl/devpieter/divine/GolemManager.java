package nl.devpieter.divine;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.divine.events.PlayerListUpdateEvent;
import nl.devpieter.divine.events.skyblock.SkyblockLocationUpdateEvent;
import nl.devpieter.divine.events.skyblock.protector.*;
import nl.devpieter.divine.utils.RegexUtils;
import nl.devpieter.divine.utils.SkyblockUtils;
import nl.devpieter.sees.Sees;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.events.chat.ReceiveMessageEvent;
import nl.devpieter.utilize.task.TaskManager;
import nl.devpieter.utilize.task.tasks.RunLaterTask;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class GolemManager implements SListener {

    private static final GolemManager INSTANCE = new GolemManager();

    private final Pattern STAGE_UPDATE_PATTERN = Pattern.compile("You feel a tremor from beneath the earth!", Pattern.CASE_INSENSITIVE);
    private final Pattern STAGE_UPDATE_5000_PATTERN = Pattern.compile("The ground begins to shake as an End Stone Protector rises from below!", Pattern.CASE_INSENSITIVE);
    private final Pattern SPAWN_PATTERN = Pattern.compile("BEWARE - An End Stone Protector has risen!", Pattern.CASE_INSENSITIVE);
    private final Pattern DEATH_PATTERN = Pattern.compile("END STONE PROTECTOR DOWN!", Pattern.CASE_INSENSITIVE);

    private final long GOLEM_SPAWN_DURATION = 20 * 1000L;

    private final HypixelManager hypixelManager = HypixelManager.getInstance();
    private final TaskManager taskManager = TaskManager.getInstance();
    private final Sees sees = Sees.getSharedInstance();

    private boolean scanningForLocation = false;
    private int scanCounter = 0;

    private GolemStage currentStage = GolemStage.UNDEFINED;
    private GolemLocation currentLocation = GolemLocation.UNDEFINED;

    private boolean isAboutToSpawn = false;
    private long predictedSpawnTimeMillis = -1;

    private boolean isFightActive = false;

    private GolemManager() {
    }

    public static GolemManager getInstance() {
        return INSTANCE;
    }

    @SEventListener
    private void onSkyblockLocationUpdate(SkyblockLocationUpdateEvent event) {
        stopLocationScan();

        isAboutToSpawn = false;
        predictedSpawnTimeMillis = -1;

        if (isFightActive) {
            isFightActive = false;
            sees.dispatch(new ProtectorFightEndEvent(true));
        }

        if (currentStage != GolemStage.UNDEFINED) {
            sees.dispatch(new ProtectorStageUpdateEvent(currentStage, GolemStage.UNDEFINED));
            currentStage = GolemStage.UNDEFINED;
        }
        if (currentLocation != GolemLocation.UNDEFINED) {
            sees.dispatch(new ProtectorLocationUpdateEvent(currentLocation, GolemLocation.UNDEFINED));
            currentLocation = GolemLocation.UNDEFINED;
        }
    }

    @SEventListener
    private void onReceiveMessage(ReceiveMessageEvent event) {
        if (!hypixelManager.isInTheEnd()) return;

        String message = event.message().getString().trim();
        if (message.isEmpty()) return;

        if (RegexUtils.matches(STAGE_UPDATE_PATTERN, message)) {
            sees.dispatch(new ProtectorMilestoneReachedEvent());
        } else if (RegexUtils.matches(STAGE_UPDATE_5000_PATTERN, message)) {
            isAboutToSpawn = true;
            predictedSpawnTimeMillis = System.currentTimeMillis() + GOLEM_SPAWN_DURATION;

            sees.dispatch(new ProtectorMilestoneReachedEvent());
            sees.dispatch(new ProtectorAboutToSpawnEvent(predictedSpawnTimeMillis));
        } else if (RegexUtils.matches(SPAWN_PATTERN, message)) {
            isFightActive = true;
            predictedSpawnTimeMillis = -1;

            sees.dispatch(new ProtectorFightStartEvent());
        } else if (RegexUtils.matches(DEATH_PATTERN, message)) {
            isFightActive = false;
            sees.dispatch(new ProtectorFightEndEvent(false));
        }
    }

    @SEventListener
    private void onPlayerListUpdate(PlayerListUpdateEvent event) {
        if (!hypixelManager.isInTheEnd()) return;

        List<Text> displayNames = event.entries().stream()
                .map(PlayerListS2CPacket.Entry::displayName)
                .filter(Objects::nonNull)
                .toList();

        GolemStage detectedStage = SkyblockUtils.getCurrentGolemStage(displayNames);
        if (detectedStage == null || detectedStage == currentStage) return;

        GolemStage previousStage = currentStage;
        currentStage = detectedStage;

        sees.dispatch(new ProtectorStageUpdateEvent(previousStage, currentStage));
    }

    @SEventListener
    private void onProtectorStageUpdate(ProtectorStageUpdateEvent event) {
        if (event.stage() == GolemStage.UNDEFINED || event.stage() == GolemStage.RESTING) stopLocationScan();
        else startLocationScan();
    }

    private void startLocationScan() {
        scanningForLocation = true;
        scanCounter = 0;

        performLocationScan();
    }

    private void stopLocationScan() {
        scanningForLocation = false;
        scanCounter = 0;
    }

    private void performLocationScan() {
        if (!scanningForLocation) return;

        // Shouldn't happen but just in case
        if (!hypixelManager.isInTheEnd()) {
            stopLocationScan();
            return;
        }

        GolemLocation detectedLocation = SkyblockUtils.getCurrentGolemLocation();
        if (detectedLocation == GolemLocation.UNDEFINED) {
            scanCounter++;

            taskManager.addTask(new RunLaterTask(() -> {
                if (scanningForLocation) performLocationScan();
            }, 20 * 2), TaskManager.TickPhase.PLAYER_TAIL);

            return;
        }

        scanningForLocation = false;
        if (detectedLocation == currentLocation) return;

        GolemLocation previousLocation = currentLocation;
        currentLocation = detectedLocation;

        sees.dispatch(new ProtectorLocationUpdateEvent(previousLocation, currentLocation));
    }

    public boolean scanningForLocation() {
        return scanningForLocation;
    }

    public int scanCounter() {
        return scanCounter;
    }

    public GolemStage currentStage() {
        return currentStage;
    }

    public GolemLocation currentLocation() {
        return currentLocation;
    }

    public String getFormattedStageText() {
        if (currentStage == GolemStage.UNDEFINED) return "N/A";
        return String.format("%s / %s", currentStage.stageName(), currentStage.stageNumber());
    }

    public String getFormattedLocationText() {
        if (currentStage == GolemStage.RESTING) return "N/A";
        if (scanningForLocation) return String.format("Scanning%s", ".".repeat(scanCounter % 4));

        if (currentLocation == GolemLocation.UNDEFINED) return "N/A";
        return currentLocation.locationName();
    }
}
