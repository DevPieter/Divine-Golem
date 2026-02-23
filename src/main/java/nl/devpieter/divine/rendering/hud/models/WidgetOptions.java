package nl.devpieter.divine.rendering.hud.models;

public class WidgetOptions {

    private boolean enabled;
    private ScreenPosition position;

    public WidgetOptions(float x, float y) {
        this(true, x, y);
    }

    public WidgetOptions(boolean enabled, float x, float y) {
        this(enabled, new ScreenPosition(x, y));
    }

    public WidgetOptions(boolean enabled, ScreenPosition position) {
        this.enabled = enabled;
        this.position = position;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ScreenPosition position() {
        return position;
    }

    public void setPosition(ScreenPosition position) {
        this.position = position;
    }
}
