package nl.devpieter.divine.formatter;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.formatter.formats.HexColorFormatter;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO - This should be moved to Utilize
public class TextFormatUtils {

    private static final TextFormatRegistry REGISTRY = TextFormatRegistry.getInstance();

    private TextFormatUtils() {
    }

    public static MutableText format(String key, Style style, Object... args) {
        String raw = Text.translatable(key, args).getString();
//        System.out.println("Formatting text: " + raw);

        String tagAlternatives = REGISTRY.formatters().keySet().stream().map(Pattern::quote).collect(Collectors.joining("|"));
        Pattern pattern = Pattern.compile("<(#[0-9a-fA-F]{6}|" + tagAlternatives + ")>(.*?)</\\1>", Pattern.DOTALL);

        MutableText result = Text.empty();

        int lastIndex = 0;
        var matcher = pattern.matcher(raw);

        while (matcher.find()) {
            if (matcher.start() < lastIndex) continue;

            if (matcher.start() > lastIndex) {
                result.append(Text.literal(raw.substring(lastIndex, matcher.start())).setStyle(style));
            }

            String tag = matcher.group(1);
            String content = matcher.group(2);

            ITextFormatter formatter = REGISTRY.formatters().get(tag);

            if (formatter == null && tag.startsWith("#")) formatter = new HexColorFormatter(tag);
            if (formatter != null) result.append(formatter.format(content, style));
            else result.append(Text.literal(matcher.group(0)).setStyle(style));

            lastIndex = matcher.end();
        }

        if (lastIndex < raw.length()) result.append(Text.literal(raw.substring(lastIndex)).setStyle(style));
        return result;
    }
}
