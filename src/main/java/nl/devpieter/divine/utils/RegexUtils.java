package nl.devpieter.divine.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    private RegexUtils() {
    }

    public static boolean matches(@NotNull Pattern pattern, String input) {
        return pattern.matcher(input).find();
    }

    public static @Nullable String findFirstGroup(@NotNull Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static @Nullable Integer findFirstGroupAsInt(@NotNull Pattern pattern, String input) {
        String group = findFirstGroup(pattern, input);
        if (group == null) return null;

        try {
            return Integer.parseInt(group);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
