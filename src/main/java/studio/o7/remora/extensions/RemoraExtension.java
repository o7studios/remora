package studio.o7.remora.extensions;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Optional;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

public interface RemoraExtension {

    Property<String> getGroupId();

    Property<String> getArtifactId();

    @Optional
    Property<String> getDescription();

    @Optional
    Property<JavaLanguageVersion> getJavaVersion();

}
