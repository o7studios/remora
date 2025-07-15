package studio.o7.remora.extensions;

import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.*;
import org.gradle.api.tasks.Optional;

public interface InformationExtension {

    Property<String> getArtifactId();

    @Optional
    Property<String> getUrl();

    @Optional
    Property<String> getDescription();

    void licenses(Action<? super MavenPomLicenseSpec> action);

    void developers(Action<? super MavenPomDeveloperSpec> action);

    void scm(Action<? super MavenPomScm> action);

    @Optional
    void organization(Action<? super MavenPomOrganization> action);

    void ciManagement(Action<? super MavenPomCiManagement> action);
}
