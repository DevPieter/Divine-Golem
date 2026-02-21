package nl.devpieter.divine.listeners;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import nl.devpieter.divine.enums.GolemDropType;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.divine.events.skyblock.protector.ProtectorDropsFoundEvent;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.utils.minecraft.TextUtils;

public class ProtectorDropListener implements SListener {

    @SEventListener
    private void onProtectorDropsFound(ProtectorDropsFoundEvent event) {

        System.out.println("Protector drops found: " + event.drops());

//        if (event.drops().stream().anyMatch(drop -> drop.type() == GolemDropType.CRYSTAL_FRAGMENT)) {
//            notifyTest();
//        }

        if (event.drops().stream().anyMatch(drop -> drop.type() == GolemDropType.TIER_BOOST_CORE)) {
            notifyTierBoostCoreFound();
        }

        if (event.drops().stream().anyMatch(drop -> drop.type() == GolemDropType.GOLEM_PET && drop.rarity() == Rarity.LEGENDARY)) {
            notifyLegendaryGolemPetFound();
        }
    }

    private void notifyTest() {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
        setTitle(TextUtils.withStyle(Text.of("Crystal Fragment Found"), titleStyle));
    }

    private void notifyTierBoostCoreFound() {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
        setTitle(TextUtils.withStyle(Text.of("Tier Boost Core Found"), titleStyle));
    }

    private void notifyLegendaryGolemPetFound() {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00)).withBold(true);
        setTitle(TextUtils.withStyle(Text.of("Legendary Golem Pet Found"), titleStyle));
    }

    private void setTitle(Text title) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.inGameHud.setTitleTicks(0, 20 * 5, 20 * 2);
        client.inGameHud.setTitle(title);
    }
}
