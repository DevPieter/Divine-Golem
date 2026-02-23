package nl.devpieter.divine.config;

import net.minecraft.client.MinecraftClient;
import nl.devpieter.divine.config.screens.HudEditScreen;
import nl.devpieter.divine.config.widget.IHudWidget;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.divine.statics.Settings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HudManager {

    private static final HudManager INSTANCE = new HudManager();

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final List<IHudWidget> widgets = new ArrayList<>();
    private final HashMap<String, ScreenPosition> defaultWidgetPositions = new HashMap<>();

    private HudManager() {
    }

    public static HudManager getInstance() {
        return INSTANCE;
    }

    public void registerWidget(IHudWidget widget, ScreenPosition position) {
        defaultWidgetPositions.put(widget.identifier(), position);

        if (!Settings.HUD_WIDGET_POSITIONS.containsKey(widget.identifier())) {
            Settings.HUD_WIDGET_POSITIONS.put(widget.identifier(), position);
        }

        widgets.add(widget);
    }

    public List<IHudWidget> widgets() {
        return widgets;
    }

    public @Nullable ScreenPosition getWidgetPosition(String identifier) {
        return Settings.HUD_WIDGET_POSITIONS.get(identifier);
    }

    public void setWidgetPosition(String identifier, ScreenPosition position) {
        if (!Settings.HUD_WIDGET_POSITIONS.containsKey(identifier)) return;
        Settings.HUD_WIDGET_POSITIONS.put(identifier, position);
    }

    public void resetWidgetPosition(String identifier) {
        if (!defaultWidgetPositions.containsKey(identifier)) return;

        ScreenPosition defaultPos = defaultWidgetPositions.get(identifier);
        Settings.HUD_WIDGET_POSITIONS.put(identifier, new ScreenPosition(defaultPos.x(), defaultPos.y()));
    }

    public @Nullable IHudWidget getWidgetByPosition(float x, float y) {
        for (IHudWidget widget : widgets) {

            ScreenPosition pos = Settings.HUD_WIDGET_POSITIONS.get(widget.identifier());
            if (pos == null) continue;

            if (x >= pos.x() && x <= pos.x() + widget.dummyWidth() && y >= pos.y() && y <= pos.y() + widget.dummyHeight()) {
                return widget;
            }
        }

        return null;
    }

    public void openEditScreen() {
        client.setScreen(new HudEditScreen());
    }

    public boolean shouldRender() {
        return !(client.currentScreen instanceof HudEditScreen);
    }
}
