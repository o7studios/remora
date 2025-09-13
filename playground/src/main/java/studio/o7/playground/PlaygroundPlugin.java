package studio.o7.playground;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public class PlaygroundPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        if (true)
            getLogger().info("hey");

        try {
            getLogger().info("err");
        } catch (Exception _) {
            getSLF4JLogger().error("Error");
        }

        String[] x = {
                "test",
                "test"
        };

        switch ("test") {
            case "hey" -> {
                getSLF4JLogger().error("Hey");
            }
            case "sdfgfg" -> getSLF4JLogger().error("test");
            default -> {
                getSLF4JLogger().error("ho");
            }
        }
    }
}
