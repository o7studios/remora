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

    public String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) return input;

        String regex = "([a-z0-9])([A-Z])";
        String snake = input
                .replaceAll(regex, "$1_$2")
                .replaceAll("[-\\s]", "_");

        return snake.toLowerCase();
    }
}
