package studio.o7.remora.extensions;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import studio.o7.remora.extensions.framework.FrameworkExtension;

import javax.inject.Inject;

@Getter
@Setter
public class RemoraExtension {

    private String groupId;

    private String artifactId;

    private String description;

    private JavaLanguageVersion javaVersion = JavaLanguageVersion.of(23);

    private FrameworkExtension framework;

    @Inject
    public RemoraExtension(ObjectFactory factory) {
        this.framework = factory.newInstance(FrameworkExtension.class);
    }

    public void framework(Action<? super FrameworkExtension> action) {
        action.execute(this.framework);
    }
}
