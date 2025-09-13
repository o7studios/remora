package studio.o7.playground;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public class PlaygroundPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        var set = new ObjectArraySet<>();

        set.add("test");

        getLogger().info(set.toString());
    }
}
