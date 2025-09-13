package studio.o7.remora.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApiVersion {
    PAPER_1_21_8("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT", "1.21.8");

    public static final ApiVersion DEFAULT = PAPER_1_21_8;

    private final String apiDependency;
    private final String apiVersion;
}
