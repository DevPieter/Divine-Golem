package nl.devpieter.divine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nl.devpieter.divine.config.HudManager;
import nl.devpieter.divine.config.screens.HudEditScreen;
import nl.devpieter.divine.config.widgets.Test2Widget;
import nl.devpieter.divine.config.widgets.TestWidget;
import nl.devpieter.divine.listeners.*;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.managers.PacketManager;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import org.lwjgl.glfw.GLFW;

public class Divine implements ClientModInitializer {

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

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.divine.debug",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                client.setScreen(new HudEditScreen());
            }
        });

        HudManager hudManager = HudManager.getInstance();
        hudManager.registerWidget(new TestWidget(), new ScreenPosition(100, 100));
        hudManager.registerWidget(new Test2Widget(), new ScreenPosition(200, 100));
    }
}
