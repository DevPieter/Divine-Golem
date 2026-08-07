package nl.devpieter.divine.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import nl.devpieter.divine.enums.GolemDropType;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.divine.events.skyblock.protector.ProtectorDropsFoundEvent;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import nl.devpieter.utilize.client.utils.TextUtils;

public class ProtectorDropListener implements SListener {

    @SEventListener
    private void onProtectorDropsFound(ProtectorDropsFoundEvent event) {

        System.out.println("Protector drops found: " + event.drops().size());
        for (var drop : event.drops()) {
            System.out.println("Drop: " + drop.type() + ", Rarity: " + drop.rarity());
        }

        if (event.drops().stream().anyMatch(drop -> drop.type() == GolemDropType.CRYSTAL_FRAGMENT)) {
            notifyTest();
        }

        if (event.drops().stream().anyMatch(drop -> drop.type() == GolemDropType.TIER_BOOST_CORE)) {
            notifyTierBoostCoreFound();
        }

        if (event.drops().stream().anyMatch(drop -> drop.type() == GolemDropType.GOLEM_PET && drop.rarity() == Rarity.LEGENDARY)) {
            notifyLegendaryGolemPetFound();
        }
    }

    private void notifyTest() {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x3be477)).withBold(true);
        setTitle(TextUtils.withStyle(Component.literal("Crystal Fragment"), titleStyle));
    }

    private void notifyTierBoostCoreFound() {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x3be477)).withBold(true);
        setTitle(TextUtils.withStyle(Component.literal("Tier Boost Core"), titleStyle));
    }

    private void notifyLegendaryGolemPetFound() {
        Style titleStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x3be477)).withBold(true);
        setTitle(TextUtils.withStyle(Component.literal("Legendary Golem Pet"), titleStyle));
    }

    private void setTitle(Component title) {
        Minecraft client = Minecraft.getInstance();
        client.gui.hud.setTimes(0, 20 * 5, 20 * 2);
        client.gui.hud.setTitle(title);
    }
}
