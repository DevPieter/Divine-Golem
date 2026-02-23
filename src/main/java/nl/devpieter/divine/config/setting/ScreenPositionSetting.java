package nl.devpieter.divine.config.setting;

import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.utilize.setting.base.SettingBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ScreenPositionSetting extends SettingBase<HashMap<String, ScreenPosition>> implements IScreenPositionSetting {

    public ScreenPositionSetting(@NotNull String identifier, HashMap<String, ScreenPosition> defaultValue) {
        super(identifier, defaultValue);
    }

    @Override
    public void put(String key, ScreenPosition value) {
        if (value == null) throw new IllegalArgumentException("WorldConfig value cannot be null");
        getValue().put(key, value);
    }

    @Override
    public boolean containsKey(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return getValue().containsKey(key);
    }

    @Override
    public ScreenPosition get(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return getValue().get(key);
    }
}
