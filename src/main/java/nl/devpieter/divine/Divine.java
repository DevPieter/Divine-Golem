package nl.devpieter.divine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.divine.formatter.TextFormatRegistry;
import nl.devpieter.divine.formatter.formats.StyleFormatter;
import nl.devpieter.divine.listeners.*;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.WidgetOptions;
import nl.devpieter.divine.rendering.hud.widgets.*;
import nl.devpieter.sees.Sees;
import nl.devpieter.utilize.managers.PacketManager;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import org.jetbrains.annotations.NotNull;
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
            if (editHudKeyBinding.wasPressed()) hudManager.openEditScreen();
        });

//        SoundEvent sound = SoundEvents.BLOCK_ANVIL_LAND;

        Identifier MY_SOUND_ID = Identifier.of("tutorial:my_sound");
        SoundEvent MY_SOUND_EVENT = SoundEvent.of(MY_SOUND_ID);
        Registry.register(Registries.SOUND_EVENT, MY_SOUND_ID, MY_SOUND_EVENT);
    }

    public static void playOnMaster(@NotNull SoundEvent soundEvent, float pitch, float volume) {
        play(PositionedSoundInstance.ui(soundEvent, pitch, volume));
    }

    public static void play(@NotNull SoundInstance soundInstance) {
        if (!ClientUtils.hasSoundManager()) return;
        ClientUtils.getSoundManager().play(soundInstance);
    }
}
