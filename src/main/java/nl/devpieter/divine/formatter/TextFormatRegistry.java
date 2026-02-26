package nl.devpieter.divine.formatter;

import java.util.HashMap;
import java.util.Map;

public class TextFormatRegistry {

    private static final TextFormatRegistry INSTANCE = new TextFormatRegistry();

    private final HashMap<String, ITextFormatter> formatters = new HashMap<>();

    private TextFormatRegistry() {
    }

    public static TextFormatRegistry getInstance() {
        return INSTANCE;
    }

    public void register(ITextFormatter formatter) {
        formatters.put(formatter.getTag(), formatter);
    }

    public ITextFormatter formatter(String tag) {
        return formatters.get(tag);
    }

    public Map<String, ITextFormatter> formatters() {
        return formatters;
    }
}
