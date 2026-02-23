package nl.devpieter.divine.config;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import nl.devpieter.utilize.setting.SettingManager;
import nl.devpieter.utilize.setting.interfaces.ISetting;
import nl.devpieter.utilize.utils.common.FileUtils;

import java.io.File;

public class Settings {

    private static final Settings INSTANCE = new Settings();

    private final SettingManager settingManager = SettingManager.getInstance();

    private final AppDirs appDirs = AppDirsFactory.getInstance();
    private final File configFolder = new File(appDirs.getUserConfigDir("nl.devpieter.divine", null, null, true));
    private final File settingsFile = new File(configFolder, "settings.json");

    private Settings() {
    }

    public static Settings getInstance() {
        return INSTANCE;
    }

    public void load(ISetting<?> setting) {
        if (!FileUtils.doesFileExist(settingsFile)) return;
        settingManager.loadSetting(settingsFile, setting);
    }

    public void save(ISetting<?> setting) {
        if (!FileUtils.tryCreateFile(settingsFile)) return;
        settingManager.queueSave(settingsFile, setting);
    }
}
