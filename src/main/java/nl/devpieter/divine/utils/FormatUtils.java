package nl.devpieter.divine.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.utilize.utils.minecraft.TextUtils;

import java.util.regex.Pattern;

public class FormatUtils {

    private FormatUtils() {
    }

    public static MutableText formatTranslatable(String key, Style normalStyle, Style highlightStyle, Object... args) {
        String raw = Text.translatable(key, args).getString();

        Pattern pattern = Pattern.compile("<highlight>(.*?)</highlight>");
        MutableText result = Text.empty();
        int lastIndex = 0;

        var matcher = pattern.matcher(raw);
        while (matcher.find()) {
            if (matcher.start() < lastIndex) continue;

            // Append text before the tag
            if (matcher.start() > lastIndex) {
                String before = raw.substring(lastIndex, matcher.start());
                result.append(TextUtils.withStyle(before, normalStyle));
            }

            // Append highlighted text
            String highlighted = matcher.group(1);
            result.append(TextUtils.withStyle(highlighted, highlightStyle));
            lastIndex = matcher.end();
        }

        // Append remaining text after the last tag
        if (lastIndex < raw.length()) {
            String remaining = raw.substring(lastIndex);
            result.append(TextUtils.withStyle(remaining, normalStyle));
        }

        return result;
    }
}
