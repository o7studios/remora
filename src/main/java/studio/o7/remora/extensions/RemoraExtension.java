package studio.o7.remora.extensions;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import studio.o7.remora.extensions.framework.FrameworkExtension;
import studio.o7.remora.extensions.publish.SonatypeMavenCentralExtension;

import javax.inject.Inject;

@Getter
@Setter
public class RemoraExtension {

    private String groupId;

    private String artifactId;

    private String description;

    private String projectUrl;

    private JavaLanguageVersion javaVersion = JavaLanguageVersion.of(23);

    private FrameworkExtension framework;

    private SonatypeMavenCentralExtension mavenCentral;

    @Inject
    public RemoraExtension(ObjectFactory factory) {
        this.framework = factory.newInstance(FrameworkExtension.class);
        this.mavenCentral = factory.newInstance(SonatypeMavenCentralExtension.class);
    }

    public void framework(Action<? super FrameworkExtension> action) {
        action.execute(this.framework);
    }

    public void mavenCentral(Action<? super SonatypeMavenCentralExtension> action) {
        action.execute(this.mavenCentral);
    }
}
