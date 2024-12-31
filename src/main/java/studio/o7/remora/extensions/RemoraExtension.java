package studio.o7.remora.extensions;

import lombok.Data;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

@Data
public class RemoraExtension {
    /**
     * Project group id. Set as gradle group and publishing groupId.
     */
    private String groupId;
    /**
     * Project artifact id. Set as publishing artifactId.
     */
    private String artifactId;
    /**
     * Project description. Set as publishing description.
     */
    private String description;

    /**
     * Java language version.
     */
    private JavaLanguageVersion javaVersion = JavaLanguageVersion.of(23);

    /**
     * Configuration of included frameworks.
     */
    private FrameworkExtension framework = new FrameworkExtension();

    /**
     * Configuration of publishing strategies.
     */
    private PublishingExtension publishing = new PublishingExtension();
}
