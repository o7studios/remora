package studio.o7.remora.extensions;

import lombok.Data;
import org.gradle.api.tasks.Optional;
import studio.o7.remora.enums.Scope;

@Data
public class FrameworkExtension {
    @Optional
    private FastUtils fastUtils = new FastUtils();

    @Data
    public static class FastUtils {
        private boolean enabled = true;
        private String version = "8.5.15";
        private Scope scope = Scope.COMPILE;
    }
}
