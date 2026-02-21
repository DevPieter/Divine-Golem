package nl.devpieter.divine.listeners;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import nl.devpieter.divine.events.skyblock.protector.ProtectorAboutToSpawnEvent;
import nl.devpieter.divine.events.skyblock.protector.ProtectorFightEndEvent;
import nl.devpieter.divine.events.skyblock.protector.ProtectorFightStartEvent;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import nl.devpieter.utilize.utils.minecraft.PlayerUtils;
import nl.devpieter.utilize.utils.minecraft.SoundUtils;
import nl.devpieter.utilize.utils.minecraft.TextUtils;

public class ProtectorListener implements SListener {

    private long fightRealStartTime = -1;
    private long fightInGameStartTime = -1;

    @SEventListener
    private void onProtectorAboutToSpawn(ProtectorAboutToSpawnEvent event) {
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
    }

    @SEventListener
    private void onProtectorFightStart(ProtectorFightStartEvent event) {
        fightRealStartTime = System.currentTimeMillis();

        ClientWorld clientWorld = ClientUtils.getWorld();
        fightInGameStartTime = clientWorld == null ? -1 : clientWorld.getLevelProperties().getTime();
    }

    @SEventListener
    private void onProtectorFightEnd(ProtectorFightEndEvent event) {

        if (event.canceled()) {
            fightRealStartTime = -1;
            fightInGameStartTime = -1;

            return;
        }

        long fightRealEndTime = System.currentTimeMillis();
        long realDurationSeconds = (fightRealEndTime - fightRealStartTime) / 1000;

        ClientWorld clientWorld = ClientUtils.getWorld();
        long fightInGameEndTime = clientWorld == null ? -1 : clientWorld.getLevelProperties().getTime();

        long inGameDurationTicks = fightInGameEndTime - fightInGameStartTime;
        long inGameDurationSeconds = inGameDurationTicks / 20;

        Text durationText = TextUtils.withStyle(Text.of("Fight duration: "), Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)).withBold(true))
                .append(TextUtils.withStyle(Text.of(String.format("%d seconds", realDurationSeconds)), Style.EMPTY.withColor(TextColor.fromRgb(0x55FF55)).withItalic(true)))
                .append(TextUtils.withStyle(Text.of(" (IGT: " + inGameDurationSeconds + " seconds)"), Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA)).withItalic(true)));

        PlayerUtils.sendMessage(durationText, false);
    }
}
