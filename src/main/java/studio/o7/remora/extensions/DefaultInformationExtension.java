package studio.o7.remora.extensions;

import org.gradle.api.Action;
import org.gradle.api.publish.maven.*;
import org.gradle.api.reflect.HasPublicType;
import org.gradle.api.reflect.TypeOf;

import static org.gradle.api.reflect.TypeOf.typeOf;

public abstract class DefaultInformationExtension implements InformationExtension, HasPublicType {
    private Action<? super MavenPomLicenseSpec> licenseConfigurator;
    private Action<? super MavenPomDeveloperSpec> developerConfigurator;
    private Action<? super MavenPomScm> scmConfigurator;
    private Action<? super MavenPomOrganization> organizationConfigurator;
    private Action<? super MavenPomCiManagement> ciConfigurator;

    public void configurePom(MavenPom pom) {
        if (licenseConfigurator != null)
            pom.licenses(licenseConfigurator);
        if (developerConfigurator != null)
            pom.developers(developerConfigurator);
        if (scmConfigurator != null)
            pom.scm(scmConfigurator);
        if (organizationConfigurator != null)
            pom.organization(organizationConfigurator);
        if (ciConfigurator != null)
            pom.ciManagement(ciConfigurator);
    }

    public void licenses(Action<? super MavenPomLicenseSpec> action) {
        licenseConfigurator = action;
    }

    public void developers(Action<? super MavenPomDeveloperSpec> action) {
        developerConfigurator = action;
    }

    public void scm(Action<? super MavenPomScm> action) {
        scmConfigurator = action;
    }

    public void organization(Action<? super MavenPomOrganization> action) {
        organizationConfigurator = action;
    }

    public void ciManagement(Action<? super MavenPomCiManagement> action) {
        ciConfigurator = action;
    }

    public TypeOf<?> getPublicType() {
        return typeOf(InformationExtension.class);
    }
}
