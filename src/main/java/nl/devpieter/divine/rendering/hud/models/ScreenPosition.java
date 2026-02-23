package nl.devpieter.divine.rendering.hud.models;

public class ScreenPosition {

    private float x;
    private float y;

    public ScreenPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return x;
    }

    public ScreenPosition setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public ScreenPosition setY(float y) {
        this.y = y;
        return this;
    }
}
