package nl.devpieter.divine.config.setting;

import com.google.common.reflect.TypeToken;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.utilize.setting.interfaces.ISetting;

import java.lang.reflect.Type;
import java.util.HashMap;

public interface IScreenPositionSetting extends ISetting<HashMap<String, ScreenPosition>> {

    void put(String key, ScreenPosition value);

    boolean containsKey(String key);

    ScreenPosition get(String key);

    @Override
    default Type getType() {
        return new TypeToken<HashMap<String, ScreenPosition>>() {
        }.getType();
    }
}
