package nl.devpieter.divine.listeners;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.divine.events.skyblock.protector.ProtectorAboutToSpawnEvent;
import nl.devpieter.divine.events.skyblock.protector.ProtectorFightBreakdownReadyEvent;
import nl.devpieter.divine.events.skyblock.protector.ProtectorFightStartEvent;
import nl.devpieter.divine.events.skyblock.protector.ProtectorStageUpdateEvent;
import nl.devpieter.divine.models.fightBreakdown.FightDamageEntry;
import nl.devpieter.divine.models.fightBreakdown.ProtectorFightBreakdown;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import nl.devpieter.utilize.utils.minecraft.SoundUtils;
import nl.devpieter.utilize.utils.minecraft.TextUtils;

public class ProtectorFightListener implements SListener {

    private long fightRealStartTime = -1;
    private long fightInGameStartTime = -1;

    @SEventListener
    private void onProtectorFightBreakdownReady(ProtectorFightBreakdownReadyEvent event) {
        ProtectorFightBreakdown breakdown = event.breakdown();
        StringBuilder sb = new StringBuilder();

        sb.append("# Protector Fight Breakdown");

        /* Your Performance */
        sb.append("\n\n## Your Performance\n\n");

        sb.append(String.format("Your Damage: %,d%n", breakdown.myDamage().damage()));
        sb.append(String.format("Your Placement: %d%n", breakdown.myDamage().position()));
        sb.append(String.format("Zealots Contributed: %d/100%n", breakdown.myZealotContribution().contribution()));

        if (breakdown.hasWitnessedFightStart()) {
            long fightDurationRealSeconds = (breakdown.timingDetails().fightEndRealTime() - breakdown.timingDetails().fightStartRealTime()) / 1000;
            long fightDurationInGameSeconds = (breakdown.timingDetails().fightEndInGameTime() - breakdown.timingDetails().fightStartInGameTime()) / 20;

            double realDps = fightDurationRealSeconds > 0 ? (double) breakdown.myDamage().damage() / fightDurationRealSeconds : breakdown.myDamage().damage();
            double inGameDps = fightDurationInGameSeconds > 0 ? (double) breakdown.myDamage().damage() / fightDurationInGameSeconds : breakdown.myDamage().damage();

            sb.append(String.format("Your Real DPS: %,.2f%n", realDps));
            sb.append(String.format("Your In-Game DPS: %,.2f%n", inGameDps));
        } else {
            sb.append("DPS details unavailable (fight start not witnessed)");
        }

        /* Damage Rankings */
        sb.append("\n\n## Damage Rankings\n\n");

        for (FightDamageEntry entry : breakdown.damageEntries()) {
            sb.append(String.format("%d. %s - %,d damage%n", entry.position(), entry.playerName(), entry.damage()));
        }

        /* Final Blow Details */
        sb.append("\n\n## Final Blow Details\n\n");
        sb.append("Final Blow: ").append(breakdown.finalBlow().playerName());

        int baseQuality = switch (breakdown.myDamage().position()) {
            case 1 -> 200;
            case 2 -> 175;
            case 3 -> 150;
            case 4 -> 125;
            case 5 -> 110;
            case 6, 7, 8 -> 100;
            case 9, 10 -> 90;
            case 11, 12 -> 80;
            default -> breakdown.myDamage().damage() > 0 ? 70 : 10;
        };

        int damageDealt = breakdown.myDamage().damage();
        int zealotContribution = breakdown.myZealotContribution().contribution();

        int firstPlaceDamage = breakdown.damageEntries().stream()
                .filter(entry -> entry.position() == 1)
                .map(FightDamageEntry::damage)
                .findFirst()
                .orElse(0);

        // Safeguard against division by zero
        if (firstPlaceDamage <= 0) firstPlaceDamage = 1;

        double damageRatio = (double) damageDealt / firstPlaceDamage;
        double damageContribution = 50.0 * damageRatio;

        int finalQuality = (int) Math.round(baseQuality + damageContribution + zealotContribution);

        /* Score Breakdown */
        sb.append("\n\n## Score Breakdown\n\n");
        sb.append(String.format("- Base Quality from Placement: %d%n", baseQuality));
        sb.append(String.format("- Damage Contribution: %f%n", damageContribution));
        sb.append(String.format("- Zealot Contribution: %d%n", zealotContribution));
        sb.append(String.format("- Final Quality: %d%n", finalQuality));

        /* Timing Details */
        sb.append("\n\n## Timing Details\n\n");
        if (breakdown.hasWitnessedFightStart()) {
            long fightDurationRealSeconds = (breakdown.timingDetails().fightEndRealTime() - breakdown.timingDetails().fightStartRealTime()) / 1000;
            long fightDurationInGameSeconds = (breakdown.timingDetails().fightEndInGameTime() - breakdown.timingDetails().fightStartInGameTime()) / 20;

            long timeBeforeSpawnRealSeconds = (breakdown.timingDetails().fightStartRealTime() - breakdown.timingDetails().fightAboutToStartRealTime()) / 1000;
            long timeBeforeSpawnInGameSeconds = (breakdown.timingDetails().fightStartInGameTime() - breakdown.timingDetails().fightAboutToStartInGameTime()) / 20;

            sb.append(String.format("- Time Before Spawn: %d seconds (IGT: %d seconds)%n", timeBeforeSpawnRealSeconds, timeBeforeSpawnInGameSeconds));
            sb.append(String.format("- Fight Duration: %d seconds (IGT: %d seconds)%n", fightDurationRealSeconds, fightDurationInGameSeconds));
        } else {
            sb.append("Timing details unavailable (fight start not witnessed)");
        }

        System.out.println(sb);
        MinecraftClient.getInstance().keyboard.setClipboard(sb.toString());
    }

    @SEventListener
    private void onProtectorStageUpdate(ProtectorStageUpdateEvent event) {
        if (event.stage() != GolemStage.AWAKENING) return;

        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
        Text title = TextUtils.withStyle(Text.of("Stage 4 Reached"), titleStyle);

        MinecraftClient client = MinecraftClient.getInstance();
        client.inGameHud.setTitleTicks(0, 20 * 5, 20 * 2);
        client.inGameHud.setTitle(title);

        SoundEvent sound = SoundEvents.BLOCK_ANVIL_LAND;
        SoundUtils.playOnMaster(sound, 1.0f, 1.0f);
    }

    @SEventListener
    private void onProtectorAboutToSpawn(ProtectorAboutToSpawnEvent event) {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
        Text title = TextUtils.withStyle(Text.of("Stage 5 Reached"), titleStyle);

        MinecraftClient client = MinecraftClient.getInstance();
        client.inGameHud.setTitleTicks(0, 20 * 5, 20 * 2);
        client.inGameHud.setTitle(title);

        SoundEvent sound = SoundEvents.BLOCK_ANVIL_LAND;
        SoundUtils.playOnMaster(sound, 1.0f, 1.0f);
    }

    @SEventListener
    private void onProtectorFightStart(ProtectorFightStartEvent event) {
        fightRealStartTime = System.currentTimeMillis();

        ClientWorld clientWorld = ClientUtils.getWorld();
        fightInGameStartTime = clientWorld == null ? -1 : clientWorld.getLevelProperties().getTime();
    }

//    @SEventListener
//    private void onProtectorFightEnd(ProtectorFightEndEvent event) {
//
//        if (event.canceled()) {
//            fightRealStartTime = -1;
//            fightInGameStartTime = -1;
//
//            return;
//        }
//
//        long fightRealEndTime = System.currentTimeMillis();
//        long realDurationSeconds = (fightRealEndTime - fightRealStartTime) / 1000;
//
//        ClientWorld clientWorld = ClientUtils.getWorld();
//        long fightInGameEndTime = clientWorld == null ? -1 : clientWorld.getLevelProperties().getTime();
//
//        long inGameDurationTicks = fightInGameEndTime - fightInGameStartTime;
//        long inGameDurationSeconds = inGameDurationTicks / 20;
//
//        Text durationText = TextUtils.withStyle(Text.of("Fight duration: "), Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)).withBold(true))
//                .append(TextUtils.withStyle(Text.of(String.format("%d seconds", realDurationSeconds)), Style.EMPTY.withColor(TextColor.fromRgb(0x55FF55)).withItalic(true)))
//                .append(TextUtils.withStyle(Text.of(" (IGT: " + inGameDurationSeconds + " seconds)"), Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA)).withItalic(true)));
//
//        PlayerUtils.sendMessage(durationText, false);
//    }
}
