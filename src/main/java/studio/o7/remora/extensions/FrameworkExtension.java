package studio.o7.remora.extensions;

import lombok.Data;
import org.gradle.api.tasks.Optional;
import studio.o7.remora.enums.Scope;

@Data
public class FrameworkExtension {
    @Optional
    private final Lombok lombok = new Lombok();
    @Optional
    private final FastUtils fastUtils = new FastUtils();

    @Data
    public static class Lombok {
        private boolean enabled = true;
        private String version = "1.18.36";
    }

    @Data
    public static class FastUtils {
        private boolean enabled = true;
        private String version = "8.5.15";
        private Scope scope = Scope.COMPILE;
    }
}
