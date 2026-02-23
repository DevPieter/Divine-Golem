package nl.devpieter.divine;

public abstract class Initializable {

    private boolean initialized = false;

    public final void initialize() {
        if (initialized) return;
        init();
        initialized = true;
    }

    protected void init() {

    }
}
