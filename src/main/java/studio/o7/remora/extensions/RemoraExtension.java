package studio.o7.remora.extensions;

import lombok.Data;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

@Data
public class RemoraExtension {
    private String groupId;
    private String artifactId;

    private JavaLanguageVersion version = JavaLanguageVersion.of(23);

    private final FrameworkExtension framework = new FrameworkExtension();

    private final PublishingExtension publishing = new PublishingExtension();

}
