package nl.devpieter.divine.config.setting;

import nl.devpieter.divine.rendering.hud.models.WidgetOptions;
import nl.devpieter.utilize.setting.base.SettingBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class WidgetOptionsSetting extends SettingBase<HashMap<String, WidgetOptions>> implements IWidgetOptionsSetting {

    public WidgetOptionsSetting(@NotNull String identifier, HashMap<String, WidgetOptions> defaultValue) {
        super(identifier, defaultValue);
    }

    @Override
    public void put(String key, WidgetOptions value) {
        if (value == null) throw new IllegalArgumentException("WorldConfig value cannot be null");
        getValue().put(key, value);
    }

    @Override
    public boolean containsKey(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return getValue().containsKey(key);
    }

    @Override
    public WidgetOptions get(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return getValue().get(key);
    }

    @Override
    public WidgetOptions putIfAbsent(String key, WidgetOptions value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (value == null) throw new IllegalArgumentException("Value cannot be null");
        return getValue().putIfAbsent(key, value);
    }
}
