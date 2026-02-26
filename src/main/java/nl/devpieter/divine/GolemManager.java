package nl.devpieter.divine;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.divine.events.PlayerListUpdateEvent;
import nl.devpieter.divine.events.skyblock.SkyblockLocationUpdateEvent;
import nl.devpieter.divine.events.skyblock.protector.*;
import nl.devpieter.divine.models.GolemDrop;
import nl.devpieter.divine.models.fightBreakdown.*;
import nl.devpieter.divine.models.fightBreakdown.details.*;
import nl.devpieter.divine.utils.GolemUtils;
import nl.devpieter.divine.utils.RegexUtils;
import nl.devpieter.divine.utils.WorldUtils;
import nl.devpieter.sees.Sees;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.events.chat.ReceiveMessageEvent;
import nl.devpieter.utilize.task.TaskManager;
import nl.devpieter.utilize.task.tasks.RunLaterTask;
import nl.devpieter.utilize.utils.minecraft.PlayerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GolemManager implements SListener {

    public static final long GOLEM_SPAWN_DELAY = 20 * 1000L;

    private static final GolemManager INSTANCE = new GolemManager();

    private final Pattern STAGE_UPDATE_PATTERN = Pattern.compile("You feel a tremor from beneath the earth!", Pattern.CASE_INSENSITIVE);
//    private final Pattern STAGE_UPDATE_5000_PATTERN = Pattern.compile("The ground begins to shake as an End Stone Protector rises from below!", Pattern.CASE_INSENSITIVE);
//    private final Pattern SPAWN_PATTERN = Pattern.compile("BEWARE - An End Stone Protector has risen!", Pattern.CASE_INSENSITIVE);
//    private final Pattern DEATH_PATTERN = Pattern.compile("END STONE PROTECTOR DOWN!", Pattern.CASE_INSENSITIVE);

    private final Pattern STAGE_UPDATE_5000_PATTERN = Pattern.compile("The ground begins to shake as an End ?Stone Protector rises from below!", Pattern.CASE_INSENSITIVE);
    private final Pattern SPAWN_PATTERN = Pattern.compile("BEWARE - An End ?Stone Protector has risen!", Pattern.CASE_INSENSITIVE);
    private final Pattern DEATH_PATTERN = Pattern.compile("END ?STONE PROTECTOR DOWN!", Pattern.CASE_INSENSITIVE);

    private final Pattern FINAL_BLOW_PATTERN = Pattern.compile("^(?:\\[[^\\]]+\\]\\s*)?(.+?)\\s+dealt the final blow\\.$", Pattern.CASE_INSENSITIVE);
    private final Pattern DAMAGE_ENTRY_PATTERN = Pattern.compile("^(\\d+)(?:st|nd|rd|th)\\s+Damager\\s+-\\s+(?:\\[[^\\]]+\\]\\s*)?([^-]+?)\\s+-\\s+([\\d,]+)$", Pattern.CASE_INSENSITIVE);
    private final Pattern MY_DAMAGE_PATTERN = Pattern.compile("^Your Damage: ([\\d,]+) \\((?:NEW RECORD!\\) \\()?Position #(\\d+)\\)?$", Pattern.CASE_INSENSITIVE);
    private final Pattern ZEALOT_CONTRIBUTION_PATTERN = Pattern.compile("^Zealots Contributed: (\\d+)/100$", Pattern.CASE_INSENSITIVE);

    private final HypixelManager hypixelManager = HypixelManager.getInstance();
    private final TaskManager taskManager = TaskManager.getInstance();
    private final Sees sees = Sees.getSharedInstance();

    private boolean scanningForLocation = false;
    private int locationScanCounter = 0;

    private boolean scanningForDrops = false;
    private int dropScanCounter = 0;

    private boolean matchingForFightBreakdown = false;
    private long fightBreakdownMatchingStartTime = -1;

    private GolemStage currentStage = GolemStage.UNDEFINED;
    private GolemLocation currentLocation = GolemLocation.UNDEFINED;

    private List<GolemDrop> currentDrops = new ArrayList<>();
    private ProtectorFightBreakdown currentFightBreakdown = null;

    private boolean isAboutToSpawn = false;
    private boolean hasWitnessedFightStart = false;

    private long fightAboutToStartRealTime = -1;
    private long fightAboutToStartInGameTime = -1;
    private long fightStartRealTime = -1;
    private long fightStartInGameTime = -1;
    private long fightEndRealTime = -1;
    private long fightEndInGameTime = -1;

    private boolean isFightActive = false;
    private GolemLocation fightLocation = GolemLocation.UNDEFINED;

    private GolemManager() {
    }

    public static GolemManager getInstance() {
        return INSTANCE;
    }

    public GolemStage currentStage() {
        return currentStage;
    }

    public GolemLocation currentLocation() {
        return currentLocation;
    }

    public boolean isAboutToSpawn() {
        return isAboutToSpawn;
    }

    public long fightAboutToStartRealTime() {
        return fightAboutToStartRealTime;
    }

    public long fightAboutToStartInGameTime() {
        return fightAboutToStartInGameTime;
    }

    public long fightStartRealTime() {
        return fightStartRealTime;
    }

    public long fightStartInGameTime() {
        return fightStartInGameTime;
    }

    public long fightEndRealTime() {
        return fightEndRealTime;
    }

    public long fightEndInGameTime() {
        return fightEndInGameTime;
    }

    public ProtectorFightBreakdown currentFightBreakdown() {
        return currentFightBreakdown;
    }

    public String getFormattedStageText() {
        if (currentStage == GolemStage.UNDEFINED) return "N/A";
        return String.format("%s / %s", currentStage.stageName(), currentStage.stageNumber());
    }

    public String getFormattedLocationText() {
        if (scanningForLocation) return String.format("Scanning%s", ".".repeat(locationScanCounter % 4));
        if (currentLocation == GolemLocation.UNDEFINED) return "N/A";
        return currentLocation.locationName();
    }

    @SEventListener
    private void onSkyblockLocationUpdate(SkyblockLocationUpdateEvent event) {
        stopLocationScan();
        stopDropScan();
        cancelBreakdownMatching();

        resetFightData();

//        currentDrops.clear();
//
//        isAboutToSpawn = false;
//        hasWitnessedFightStart = false;
//
//        fightAboutToStartRealTime = -1;
//        fightAboutToStartInGameTime = -1;
//        fightStartRealTime = -1;
//        fightStartInGameTime = -1;
//        fightEndRealTime = -1;
//        fightEndInGameTime = -1;
//
//        if (isFightActive) {
//            isFightActive = false;
//            sees.dispatch(new ProtectorFightEndEvent(true, false));
//        }
//
//        fightLocation = GolemLocation.UNDEFINED;

        if (currentStage != GolemStage.UNDEFINED) setStageWithEvent(GolemStage.UNDEFINED);
        if (currentLocation != GolemLocation.UNDEFINED) setLocationWithEvent(GolemLocation.UNDEFINED);
    }

    private void resetFightData() {
        currentDrops.clear();
        currentFightBreakdown = null;

        isAboutToSpawn = false;
        hasWitnessedFightStart = false;

        fightAboutToStartRealTime = -1;
        fightAboutToStartInGameTime = -1;
        fightStartRealTime = -1;
        fightStartInGameTime = -1;
        fightEndRealTime = -1;
        fightEndInGameTime = -1;

        if (isFightActive) {
            isFightActive = false;
            sees.dispatch(new ProtectorFightEndEvent(true, false));
        }

        fightLocation = GolemLocation.UNDEFINED;
    }

    @SEventListener
    private void onReceiveMessage(ReceiveMessageEvent event) {
        if (!hypixelManager.isInTheEnd()) return;

        String message = event.message().getString().trim();
        if (message.isEmpty()) return;

        if (RegexUtils.matches(STAGE_UPDATE_PATTERN, message)) {
            sees.dispatch(new ProtectorMilestoneReachedEvent());
        } else if (RegexUtils.matches(STAGE_UPDATE_5000_PATTERN, message)) {
            resetFightData();

            fightAboutToStartRealTime = System.currentTimeMillis();
            fightAboutToStartInGameTime = WorldUtils.getWorldTime();

            isAboutToSpawn = true;

            sees.dispatch(new ProtectorMilestoneReachedEvent());
            sees.dispatch(new ProtectorAboutToSpawnEvent());
        } else if (RegexUtils.matches(SPAWN_PATTERN, message)) {
            fightStartRealTime = System.currentTimeMillis();
            fightStartInGameTime = WorldUtils.getWorldTime();

            isAboutToSpawn = false;
            isFightActive = true;
            hasWitnessedFightStart = true;
            fightLocation = currentLocation;

            sees.dispatch(new ProtectorFightStartEvent());
        } else if (RegexUtils.matches(DEATH_PATTERN, message)) {
            fightEndRealTime = System.currentTimeMillis();
            fightEndInGameTime = WorldUtils.getWorldTime();

            isFightActive = false;
            sees.dispatch(new ProtectorFightEndEvent(false, hasWitnessedFightStart));

            if (fightLocation == GolemLocation.UNDEFINED) {
                Text warningMessage = Text.literal("Couldn't determine fight location, unable to scan for drops!").formatted(Formatting.RED);
                PlayerUtils.sendMessage(warningMessage, false);
            } else startDropScan();

            startBreakdownMatching();
        }

        if (matchingForFightBreakdown) {
            if (System.currentTimeMillis() - fightBreakdownMatchingStartTime > 10000) {
                cancelBreakdownMatching();

                Text warningMessage = Text.literal("Couldn't parse fight breakdown in time!").formatted(Formatting.RED);
                PlayerUtils.sendMessage(warningMessage, false);
            } else matchFightBreakdownMessage(message);
        }
    }

    @SEventListener
    private void onPlayerListUpdate(PlayerListUpdateEvent event) {
        if (!hypixelManager.isInTheEnd()) return;

        List<Text> displayNames = event.entries().stream()
                .map(PlayerListS2CPacket.Entry::displayName)
                .filter(Objects::nonNull)
                .toList();

        GolemStage detectedStage = GolemUtils.getCurrentStage(displayNames);
        if (detectedStage != null) setStageWithEvent(detectedStage);
    }

    @SEventListener
    private void onProtectorStageUpdate(ProtectorStageUpdateEvent event) {
        if (event.stage() == GolemStage.UNDEFINED || event.stage() == GolemStage.RESTING) {
            stopLocationScan();
            setLocationWithEvent(GolemLocation.UNDEFINED);
        } else startLocationScan();
    }

    private void startLocationScan() {
        scanningForLocation = true;
        locationScanCounter = 0;

        performLocationScan();
    }

    private void stopLocationScan() {
        scanningForLocation = false;
        locationScanCounter = 0;
    }

    private void startDropScan() {
        scanningForDrops = true;
        dropScanCounter = 0;

        currentDrops.clear();
        performDropScan();
    }

    private void stopDropScan() {
        scanningForDrops = false;
        dropScanCounter = 0;
    }

    private void startBreakdownMatching() {
        matchingForFightBreakdown = true;
        fightBreakdownMatchingStartTime = System.currentTimeMillis();

        TimingDetails timings = new TimingDetails(
                fightAboutToStartRealTime,
                fightAboutToStartInGameTime,
                fightStartRealTime,
                fightStartInGameTime,
                fightEndRealTime,
                fightEndInGameTime
        );

        currentFightBreakdown = new ProtectorFightBreakdown(hasWitnessedFightStart, timings);
    }

    private void cancelBreakdownMatching() {
        matchingForFightBreakdown = false;
        fightBreakdownMatchingStartTime = -1;

        currentFightBreakdown = null;
    }

    private void performLocationScan() {
        if (!scanningForLocation) return;

        // Shouldn't happen but just in case
        if (!hypixelManager.isInTheEnd()) {
            stopLocationScan();
            return;
        }

        GolemLocation detectedLocation = GolemUtils.getCurrentLocation();
        if (detectedLocation == GolemLocation.UNDEFINED) {
            locationScanCounter++;

            taskManager.addTask(new RunLaterTask(() -> {
                if (scanningForLocation) performLocationScan();
            }, 10), TaskManager.TickPhase.PLAYER_TAIL);

            return;
        }

        scanningForLocation = false;
        setLocationWithEvent(detectedLocation);
    }

    private void performDropScan() {
        if (!scanningForDrops) return;

        // Shouldn't happen but just in case
        if (!hypixelManager.isInTheEnd()) {
            stopDropScan();
            return;
        }

        List<GolemDrop> detectedDrops = GolemUtils.findDrops(fightLocation);

        if (detectedDrops.isEmpty()) {
            dropScanCounter++;

            if (dropScanCounter > 10) {
                stopDropScan();

                // TODO - Maybe send message or show notification
                return;
            }

            taskManager.addTask(new RunLaterTask(() -> {
                if (scanningForDrops) performDropScan();
            }, 10), TaskManager.TickPhase.PLAYER_TAIL);

            return;
        }

        scanningForDrops = false;
        currentDrops = detectedDrops;

        sees.dispatch(new ProtectorDropsFoundEvent(currentDrops));
    }

    private void matchFightBreakdownMessage(String message) {
        String finalBlowPlayer = RegexUtils.findFirstGroup(FINAL_BLOW_PATTERN, message);
        if (finalBlowPlayer != null) currentFightBreakdown.setFinalBlow(new FinalBlowDetails(finalBlowPlayer));

        Matcher damageEntryMatcher = DAMAGE_ENTRY_PATTERN.matcher(message);
        if (damageEntryMatcher.matches()) {
            int position = Integer.parseInt(damageEntryMatcher.group(1));
            String playerName = damageEntryMatcher.group(2);
            int damage = Integer.parseInt(damageEntryMatcher.group(3).replace(",", ""));

            currentFightBreakdown.addDamageEntry(new FightDamageEntry(playerName, position, damage));
        }

        var myDamageMatcher = MY_DAMAGE_PATTERN.matcher(message);
        if (myDamageMatcher.matches()) {
            int damage = Integer.parseInt(myDamageMatcher.group(1).replaceAll(",", ""));
            int position = Integer.parseInt(myDamageMatcher.group(2));

            currentFightBreakdown.setMyDamage(new MyDamageDetails(damage, position));
        }

        Integer zealotContribution = RegexUtils.findFirstGroupAsInt(ZEALOT_CONTRIBUTION_PATTERN, message);
        if (zealotContribution != null)
            currentFightBreakdown.setMyZealotContribution(new MyZealotContributionDetails(zealotContribution));

        if (!currentFightBreakdown.isComplete()) return;
        matchingForFightBreakdown = false;

        sees.dispatch(new ProtectorFightBreakdownReadyEvent(currentFightBreakdown));
    }

    private void setStageWithEvent(GolemStage newStage) {
        if (newStage == currentStage) return;

        GolemStage previousStage = currentStage;
        currentStage = newStage;

        sees.dispatch(new ProtectorStageUpdateEvent(previousStage, currentStage));
    }

    private void setLocationWithEvent(GolemLocation newLocation) {
        if (newLocation == currentLocation) return;

        GolemLocation previousLocation = currentLocation;
        currentLocation = newLocation;

        sees.dispatch(new ProtectorLocationUpdateEvent(previousLocation, currentLocation));
    }
}
