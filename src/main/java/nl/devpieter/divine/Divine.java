package nl.devpieter.divine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import nl.devpieter.divine.config.HudManager;
import nl.devpieter.divine.config.widgets.CountdownHudWidget;
import nl.devpieter.divine.config.widgets.TrackerHudWidget;
import nl.devpieter.divine.listeners.*;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.divine.statics.Settings;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.managers.PacketManager;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import org.lwjgl.glfw.GLFW;

public class Divine implements ClientModInitializer {

    private final KeyBinding.Category category = new KeyBinding.Category(Identifier.of("divine", "key_category"));

    private final KeyBinding editHudKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.divine.edit_hud",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            category
    ));

    @Override
    public void onInitializeClient() {
        Settings.load();

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
        hudManager.registerWidget(new TrackerHudWidget(), new ScreenPosition(20, 20));
        hudManager.registerWidget(new CountdownHudWidget(), new ScreenPosition(20, 80));

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.divine.debug",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) hudManager.openEditScreen();
            if (editHudKeyBinding.wasPressed()) hudManager.openEditScreen();
        });
    }
}
