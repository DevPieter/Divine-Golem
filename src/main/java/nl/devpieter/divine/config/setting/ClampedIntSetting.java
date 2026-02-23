package nl.devpieter.divine.config.setting;

import nl.devpieter.utilize.setting.settings.IntSetting;
import org.jetbrains.annotations.NotNull;

public class ClampedIntSetting extends IntSetting {

    private final int min;
    private final int max;

    public ClampedIntSetting(@NotNull String identifier, Integer defaultValue, int min, int max) {
        super(identifier, defaultValue);

        if (min > max) throw new IllegalArgumentException("Min cannot be greater than max");
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(Integer value) {
        if (value == null) return;

        if (value < min) value = min;
        else if (value > max) value = max;

        super.setValue(value);
    }
}
