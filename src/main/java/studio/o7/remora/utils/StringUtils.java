package studio.o7.remora.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public String toPascalCase(String input) {
        var parts = input.split("[-_]");
        var sb = new StringBuilder();
        for (var part : parts) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1));
            }
        }
        return sb.toString();
    }
}
