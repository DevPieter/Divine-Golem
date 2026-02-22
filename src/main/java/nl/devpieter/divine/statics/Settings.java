package nl.devpieter.divine.statics;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.utilize.setting.SettingManager;
import nl.devpieter.utilize.setting.interfaces.ISetting;
import nl.devpieter.utilize.setting.settings.MapSetting;
import nl.devpieter.utilize.utils.common.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Settings {

    private static final AppDirs APP_DIRS = AppDirsFactory.getInstance();

    private static final File CONFIG_FOLDER = new File(APP_DIRS.getUserConfigDir("nl.devpieter.divine", null, null, true));
    private static final File SETTINGS_FILE = new File(CONFIG_FOLDER, "Settings.json");

    public static final MapSetting<String, ScreenPosition> HUD_WIDGET_POSITIONS = new MapSetting<>(
            "hud_widget_positions",
            new HashMap<>()
    );

    public static void load() {
        if (!FileUtils.doesFileExist(SETTINGS_FILE)) return;

        SettingManager settingManager = SettingManager.getInstance();
        settingManager.loadSettings(SETTINGS_FILE, List.of(
                HUD_WIDGET_POSITIONS
        ));
    }

    public static void save(ISetting<?> setting) {
        if (!FileUtils.tryCreateFile(SETTINGS_FILE)) return;

        SettingManager settingManager = SettingManager.getInstance();
        settingManager.queueSave(SETTINGS_FILE, setting);
    }
}
