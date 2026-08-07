package nl.devpieter.divine.formatter;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public interface ITextFormatter {

    String getTag();

    MutableComponent format(String content, Style baseStyle);
}
