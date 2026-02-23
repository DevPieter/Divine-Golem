package nl.devpieter.divine.rendering.hud;

import net.minecraft.client.MinecraftClient;
import nl.devpieter.divine.config.Settings;
import nl.devpieter.divine.config.setting.WidgetOptionsSetting;
import nl.devpieter.divine.rendering.hud.models.ScreenPosition;
import nl.devpieter.divine.rendering.hud.models.WidgetOptions;
import nl.devpieter.divine.rendering.hud.widget.IHudWidget;
import nl.devpieter.divine.rendering.screens.HudEditScreen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HudManager {

    private static final HudManager INSTANCE = new HudManager();

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Settings settings = Settings.getInstance();

    private final List<IHudWidget> widgets = new ArrayList<>();
    private final HashMap<String, WidgetOptions> defaultWidgetOptions = new HashMap<>();

    private final WidgetOptionsSetting widgetOptions = new WidgetOptionsSetting(
            "hud_widget_options",
            new HashMap<>()
    );

    private HudManager() {
        settings.load(widgetOptions);
    }

    public static HudManager getInstance() {
        return INSTANCE;
    }

    public void registerWidget(IHudWidget widget, WidgetOptions options) {
        defaultWidgetOptions.put(widget.identifier(), options);

        if (!widgetOptions.containsKey(widget.identifier())) {
            widgetOptions.put(widget.identifier(), options);
        }

        widgets.add(widget);
    }

    public List<IHudWidget> widgets() {
        return widgets;
    }

    public WidgetOptions getWidgetOptions(String identifier) {
        return widgetOptions.putIfAbsent(identifier, defaultWidgetOptions.get(identifier));
    }

    public boolean isWidgetEnabled(String identifier) {
        return getWidgetOptions(identifier).enabled();
    }

    public void setWidgetEnabled(String identifier, boolean enabled) {
        getWidgetOptions(identifier).setEnabled(enabled);
    }

    public void toggleWidgetEnabled(String identifier) {
        WidgetOptions options = getWidgetOptions(identifier);
        options.setEnabled(!options.enabled());
    }

    public ScreenPosition getWidgetPosition(String identifier) {
        return getWidgetOptions(identifier).position();
    }

    public void setWidgetPosition(String identifier, float x, float y) {
        getWidgetPosition(identifier).setX(x).setY(y);
    }

    public void resetWidgetPosition(String identifier) {
        if (!defaultWidgetOptions.containsKey(identifier)) return;

        WidgetOptions defaultOptions = defaultWidgetOptions.get(identifier);
        setWidgetPosition(identifier, defaultOptions.position().x(), defaultOptions.position().y());
    }

    public @Nullable IHudWidget getWidgetByPosition(float x, float y) {
        for (IHudWidget widget : widgets) {
            ScreenPosition pos = getWidgetPosition(widget.identifier());

            if (x >= pos.x() && x <= pos.x() + widget.dummyWidth() && y >= pos.y() && y <= pos.y() + widget.dummyHeight()) {
                return widget;
            }
        }

        return null;
    }

    public void openEditScreen() {
        client.setScreen(new HudEditScreen());
    }

    public void save() {
        settings.save(widgetOptions);
    }

    public boolean shouldRender() {
        return !(client.currentScreen instanceof HudEditScreen);
    }
}
