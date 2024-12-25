package studio.o7.remora.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Scope {
    COMPILE("implementation"),
    PROVIDED("compileOnly"),
    API("api");

    private final String configurationName;
}
