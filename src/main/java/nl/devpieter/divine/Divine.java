package nl.devpieter.divine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import nl.devpieter.divine.listeners.*;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.WidgetOptions;
import nl.devpieter.divine.rendering.hud.widgets.CountdownHudWidget;
import nl.devpieter.divine.rendering.hud.widgets.DamagePerSecondHudWidget;
import nl.devpieter.divine.rendering.hud.widgets.LootQualityHudWidget;
import nl.devpieter.divine.rendering.hud.widgets.TrackerHudWidget;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.managers.PacketManager;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import org.lwjgl.glfw.GLFW;

public class Divine implements ClientModInitializer {

    private final KeyBinding.Category category = new KeyBinding.Category(Identifier.of("divine", "main"));

    private final KeyBinding editHudKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.divine.open_hud_editor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            category
    ));

    @Override
    public void onInitializeClient() {
        Sees sees = Sees.getSharedInstance();
        sees.subscribe(HypixelManager.getInstance());
        sees.subscribe(GolemManager.getInstance());

        if (ClientUtils.isDevEnv()) sees.subscribe(new DebugListener());
        sees.subscribe(new ProtectorDropListener());
        sees.subscribe(new ProtectorFightListener());

        PacketManager packetManager = PacketManager.getInstance();
        packetManager.subscribe(new GameJoinPacketListener());
        packetManager.subscribe(new PlayerListPacketListener());

        HudManager hudManager = HudManager.getInstance();
        hudManager.registerWidget(new TrackerHudWidget(), new WidgetOptions(20, 20));
        hudManager.registerWidget(new CountdownHudWidget(), new WidgetOptions(20, 80));
        hudManager.registerWidget(new LootQualityHudWidget(), new WidgetOptions(230, 20));
        hudManager.registerWidget(new DamagePerSecondHudWidget(), new WidgetOptions(230, 50));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (editHudKeyBinding.wasPressed()) hudManager.openEditScreen();
        });
    }
}
