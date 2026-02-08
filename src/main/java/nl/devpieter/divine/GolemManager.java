package nl.devpieter.divine;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.divine.events.PlayerListUpdateEvent;
import nl.devpieter.divine.events.SkyblockLocationUpdateEvent;
import nl.devpieter.divine.utils.RegexUtils;
import nl.devpieter.divine.utils.SkyblockUtils;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.events.chat.ReceiveMessageEvent;
import nl.devpieter.utilize.task.TaskManager;
import nl.devpieter.utilize.task.tasks.RunLaterTask;
import nl.devpieter.utilize.utils.minecraft.SoundUtils;
import nl.devpieter.utilize.utils.minecraft.TextUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class GolemManager implements SListener {

    private static final GolemManager INSTANCE = new GolemManager();

    private final Pattern STAGE_UPDATE_PATTERN = Pattern.compile("You feel a tremor from beneath the earth!", Pattern.CASE_INSENSITIVE);
    private final Pattern STAGE_UPDATE_5000_PATTERN = Pattern.compile("The ground begins to shake as an End Stone Protector rises from below!", Pattern.CASE_INSENSITIVE);
    private final Pattern SPAWN_PATTERN = Pattern.compile("BEWARE - An End Stone Protector has risen!", Pattern.CASE_INSENSITIVE);
    private final Pattern DEATH_PATTERN = Pattern.compile("END STONE PROTECTOR DOWN!", Pattern.CASE_INSENSITIVE);

    private final HypixelManager hypixelManager = HypixelManager.getInstance();
    private final TaskManager taskManager = TaskManager.getInstance();

    private boolean scanningForLocation = false;
    private int scanCounter = 0;

    private GolemStage currentStage = GolemStage.UNDEFINED;
    private GolemLocation currentLocation = GolemLocation.UNDEFINED;

    private GolemManager() {
    }

    public static GolemManager getInstance() {
        return INSTANCE;
    }

    @SEventListener
    private void onSkyblockLocationUpdate(SkyblockLocationUpdateEvent event) {
        stopLocationScan();

        currentStage = GolemStage.UNDEFINED;
        currentLocation = GolemLocation.UNDEFINED;
    }

    @SEventListener
    private void onReceiveMessage(ReceiveMessageEvent event) {
        if (!hypixelManager.isInTheEnd()) return;

        String message = event.message().getString().trim();
        if (message.isEmpty()) return;

        if (RegexUtils.matches(STAGE_UPDATE_PATTERN, message)) {
            System.out.println("==== Golem stage update (kill counter milestone).");
        } else if (RegexUtils.matches(STAGE_UPDATE_5000_PATTERN, message)) {
            System.out.println("==== Golem stage update (5000 kills).");

            Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
            Style subtitleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)).withItalic(true);

            Text title = TextUtils.withStyle(Text.of("Stage 5 Reached"), titleStyle);
            Text subtitle = TextUtils.withStyle(Text.of("The End Stone Protector will spawn in ~20 seconds!"), subtitleStyle);

            MinecraftClient client = MinecraftClient.getInstance();
            client.inGameHud.setTitleTicks(0, 20 * 5, 20 * 2);
            client.inGameHud.setTitle(title);
            client.inGameHud.setSubtitle(subtitle);

            SoundEvent sound = SoundEvents.BLOCK_ANVIL_LAND;
            SoundUtils.playOnMaster(sound, 1.0f, 1.0f);

        } else if (RegexUtils.matches(SPAWN_PATTERN, message)) {
            System.out.println("==== Golem stage update (protector spawn).");
        } else if (RegexUtils.matches(DEATH_PATTERN, message)) {
            System.out.println("==== Golem stage update (protector death).");
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
        currentStage = detectedStage;

        if (currentStage == GolemStage.UNDEFINED || currentStage == GolemStage.RESTING) stopLocationScan();
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
        GolemLocation location = SkyblockUtils.getCurrentGolemLocation();

        if (location != GolemLocation.UNDEFINED) {
            GolemLocation previousLocation = currentLocation;
            currentLocation = location;
            scanningForLocation = false;

            if (currentLocation != previousLocation) {
                // TODO - Dispatch event

//                Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
//                Style subtitleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)).withBold(false);
//                Style locationStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x55FF55)).withItalic(true);
//
//                MutableText title = TextUtils.withStyle(Text.of("\nProtector Location Detected"), titleStyle);
//                MutableText subtitle = TextUtils.withStyle(Text.of("Location: "), subtitleStyle).append(TextUtils.withStyle(Text.of(location.locationName()), locationStyle));
//
//                PlayerUtils.sendMessage(title.append(Text.of("\n")).append(subtitle).append(Text.of("\n")), false);
            }

            return;
        }

        scanCounter++;

        taskManager.addTask(new RunLaterTask(() -> {
            if (scanningForLocation) performLocationScan();
        }, 20 * 2), TaskManager.TickPhase.PLAYER_TAIL);
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
