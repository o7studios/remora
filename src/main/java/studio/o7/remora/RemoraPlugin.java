package studio.o7.remora;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import lombok.NonNull;
import net.thebugmc.gradle.sonatypepublisher.CentralPortalExtension;
import net.thebugmc.gradle.sonatypepublisher.PublishToCentralPortal;
import net.thebugmc.gradle.sonatypepublisher.PublishingType;
import net.thebugmc.gradle.sonatypepublisher.SonatypeCentralPortalPublisherPlugin;
import org.cthing.gradle.plugins.buildconstants.BuildConstantsPlugin;
import org.cthing.gradle.plugins.buildconstants.BuildConstantsTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.plugins.signing.SigningExtension;
import studio.o7.remora.extensions.DefaultInformationExtension;
import studio.o7.remora.extensions.InformationExtension;

import java.net.URI;

public class RemoraPlugin implements Plugin<Project> {

    public static InformationExtension setupExtension(@NonNull Logger logger, @NonNull ExtensionContainer extensions) throws IllegalArgumentException {
        logger.info("Setting up the `information` extension");
        return extensions.create("information", DefaultInformationExtension.class);
    }

    public static void applyNecessaryPlugins(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Applying necessary plugins `java-library`, `shadow`, `build-constants`, and `sonatype-central-publisher`");

        var pluginManager = project.getPluginManager();

        pluginManager.apply(JavaLibraryPlugin.class);
        pluginManager.apply(ShadowPlugin.class);
        pluginManager.apply(BuildConstantsPlugin.class);
        pluginManager.apply(SonatypeCentralPortalPublisherPlugin.class);
    }

    public static void applyNecessaryRepositories(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Applying necessary repositories `maven-central`");

        RepositoryHandler repositories = project.getRepositories();

        repositories.mavenCentral();
        repositories.maven(repo -> {
            repo.setName("papermc");
            repo.setUrl(URI.create("https://repo.papermc.io/repository/maven-public/"));
        });
        repositories.maven(repo -> {
            repo.setName("opencollab-snapshot");
            repo.setUrl(URI.create("https://repo.opencollab.dev/main/"));
        });
    }

    public static void applyDependencies(@NonNull Logger logger, @NonNull Project project) {
        DependencyHandler dependencies = project.getDependencies();

        logger.info("Adding dependency `lombok`");
        String lombokId = "org.projectlombok:lombok:1.18.36";
        dependencies.add("compileOnly", lombokId);
        dependencies.add("annotationProcessor", lombokId);
    }

    public static void applyMavenPublishPluginConfiguration(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Configuring `centralPortal` extension");
        project.getExtensions().configure(CentralPortalExtension.class, centralPortal -> {
            centralPortal.getUsername().set(System.getenv("SONATYPE_USERNAME"));
            centralPortal.getPassword().set(System.getenv("SONATYPE_PASSWORD"));
            centralPortal.getPublishingType().set(PublishingType.USER_MANAGED);
        });

        var information = (DefaultInformationExtension) project.getExtensions().getByType(InformationExtension.class);
        project.getExtensions().configure(CentralPortalExtension.class, centralPortal -> {
            centralPortal.getName().set(information.getArtifactId());
            centralPortal.pom(pom -> {
                if (information.getUrl().isPresent())
                    pom.getUrl().set(information.getUrl());
                if (information.getDescription().isPresent())
                    pom.getDescription().set(information.getDescription());
                if (information.getArtifactId().isPresent())
                    pom.getName().set(information.getArtifactId());
                information.configurePom(pom);
            });
        });

        logger.info("Configuring `signing` extension");
        project.getExtensions().configure(SigningExtension.class, signing -> {
            signing.useInMemoryPgpKeys(
                    System.getenv("GPG_KEY"),
                    System.getenv("GPG_PASSPHRASE")
            );
            signing.sign(project.getExtensions().getByType(PublishingExtension.class).getPublications());
        });
    }

    public static void applyTaskConfiguration(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Configuring task `generateBuildConstants`");
        project.getTasks().withType(BuildConstantsTask.class).configureEach(task -> {
            task.getClassname().set(project.getGroup() + ".BuildConstants");

            task.getOutputDirectory().set(project.getLayout().getBuildDirectory()
                    + "/generated/sources/buildConstants/java/main");

            var information = project.getExtensions().getByType(InformationExtension.class);
            var additionalConstants = task.getAdditionalConstants();

            if (information.getUrl().isPresent())
                additionalConstants.put("PROJECT_URL", information.getUrl());
        });

        project.getTasks().named("sourcesJar", Jar.class, jar -> {
            jar.dependsOn("generateBuildConstants");
        });
    }

    @Override
    public void apply(Project project) {
        var logger = project.getLogger();

        applyNecessaryRepositories(logger, project);
        applyNecessaryPlugins(logger, project);

        var extensions = project.getExtensions();
        var extension = setupExtension(logger, extensions);
        if (!extension.getUrl().isPresent())
            extension.getUrl().set("https://github.com/XXX/YYY");

        applyDependencies(logger, project);
        applyMavenPublishPluginConfiguration(logger, project);

        applyTaskConfiguration(logger, project);
    }
}
