package nl.devpieter.divine.config.setting;

import com.google.common.reflect.TypeToken;
import nl.devpieter.divine.rendering.hud.models.WidgetOptions;
import nl.devpieter.utilize.setting.interfaces.ISetting;

import java.lang.reflect.Type;
import java.util.HashMap;

public interface IWidgetOptionsSetting extends ISetting<HashMap<String, WidgetOptions>> {

    void put(String key, WidgetOptions value);

    boolean containsKey(String key);

    WidgetOptions get(String key);

    WidgetOptions putIfAbsent(String key, WidgetOptions value);

    @Override
    default Type getType() {
        return new TypeToken<HashMap<String, WidgetOptions>>() {
        }.getType();
    }
}
