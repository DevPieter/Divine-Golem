package nl.devpieter.divine;

import net.fabricmc.api.ClientModInitializer;
import nl.devpieter.divine.listeners.DebugListener;
import nl.devpieter.divine.listeners.GameJoinPacketListener;
import nl.devpieter.divine.listeners.PlayerListPacketListener;
import nl.devpieter.divine.listeners.ProtectorListener;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.managers.PacketManager;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;

public class Divine implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Sees sees = Sees.getSharedInstance();
        sees.subscribe(HypixelManager.getInstance());
        sees.subscribe(GolemManager.getInstance());

        if (ClientUtils.isDevEnv()) sees.subscribe(new DebugListener());
        sees.subscribe(new ProtectorListener());

        PacketManager packetManager = PacketManager.getInstance();
        packetManager.subscribe(new GameJoinPacketListener());
        packetManager.subscribe(new PlayerListPacketListener());

//        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                "key.divine.debug",
//                InputUtil.Type.KEYSYM,
//                GLFW.GLFW_KEY_N,
//                KeyBinding.Category.MISC
//        ));
//
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (keyBinding.wasPressed()) {
//
//            }
//        });
    }
}
