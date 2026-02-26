package nl.devpieter.divine.formatter;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;

public interface ITextFormatter {

    String getTag();

    MutableText format(String content, Style baseStyle);
}
