package nl.devpieter.divine;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.divine.formatter.TextFormatRegistry;
import nl.devpieter.divine.formatter.formats.StyleFormatter;
import nl.devpieter.divine.listeners.*;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.WidgetOptions;
import nl.devpieter.divine.rendering.hud.widgets.*;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.client.utils.ClientUtils;
import nl.devpieter.utilize.managers.PacketManager;
import org.lwjgl.glfw.GLFW;

public class Divine implements ClientModInitializer {

    private final KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath("divine", "main"));

    private final KeyMapping editHudKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.divine.open_hud_editor",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            category
    ));

    @Override
    public void onInitializeClient() {
        Sees sees = Sees.getSharedInstance();
        sees.subscribe(HypixelManager.getInstance());
        sees.subscribe(GolemManager.getInstance());

        sees.subscribe(new DebugListener());
        sees.subscribe(new ProtectorDropListener());
        sees.subscribe(new ProtectorFightListener());

        PacketManager packetManager = PacketManager.getInstance();
        packetManager.subscribe(new GameJoinPacketListener());
        packetManager.subscribe(new PlayerListPacketListener());

        HudManager hudManager = HudManager.getInstance();
        hudManager.registerWidget(new TrackerHudWidget(), new WidgetOptions(20, 20));
        hudManager.registerWidget(new CountdownHudWidget(), new WidgetOptions(20, 90));
        hudManager.registerWidget(new LootQualityHudWidget(), new WidgetOptions(230, 20));
        hudManager.registerWidget(new DamageBreakdownHudWidget(), new WidgetOptions(370, 20));
        hudManager.registerWidget(new TimingBreakdownHudWidget(), new WidgetOptions(370, 60));

        TextFormatRegistry formatRegistry = TextFormatRegistry.getInstance();
        formatRegistry.register(new StyleFormatter("highlight", 0x3be477));
        formatRegistry.register(new StyleFormatter("yes", 0x3be477));
        formatRegistry.register(new StyleFormatter("no", 0xe43b3b));
        formatRegistry.register(new StyleFormatter("r:common", Rarity.COMMON.color()));
        formatRegistry.register(new StyleFormatter("r:uncommon", Rarity.UNCOMMON.color()));
        formatRegistry.register(new StyleFormatter("r:rare", Rarity.RARE.color()));
        formatRegistry.register(new StyleFormatter("r:epic", Rarity.EPIC.color()));
        formatRegistry.register(new StyleFormatter("r:legendary", Rarity.LEGENDARY.color()));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (editHudKeyBinding.consumeClick()) hudManager.openEditScreen();
        });
    }
}
