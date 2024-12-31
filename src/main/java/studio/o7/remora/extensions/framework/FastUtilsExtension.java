package studio.o7.remora.extensions.framework;

import lombok.Getter;
import lombok.Setter;
import studio.o7.remora.enums.Scope;

@Getter
@Setter
public class FastUtilsExtension {
    private boolean enabled = false;
    private String version = "8.5.15";
    private Scope scope = Scope.COMPILE;
}
