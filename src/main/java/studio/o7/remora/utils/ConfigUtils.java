package studio.o7.remora.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.gradle.api.Project;
import studio.o7.remora.RemoraPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class ConfigUtils {

    public static File getConfig(@NonNull Project project, @NonNull String resourcesPath) {
        var resourceStream = RemoraPlugin.class.getClassLoader().getResourceAsStream(resourcesPath);
        if (resourceStream == null)
            throw new IllegalStateException(resourcesPath + " config not found in plugin resources!");

        var path = "remora/" + resourcesPath;

        var dest = new File(project.getLayout().getBuildDirectory().get().getAsFile(), path);
        var parent = dest.getParentFile();
        if (parent == null)
            throw new IllegalStateException("Parent file of " + path + " null");
        var _ = parent.mkdirs();
        try {
            Files.copy(resourceStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return dest;
    }
}
