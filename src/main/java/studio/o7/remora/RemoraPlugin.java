package studio.o7.remora;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import com.github.spotbugs.snom.Confidence;
import com.github.spotbugs.snom.Effort;
import com.github.spotbugs.snom.SpotBugsExtension;
import com.github.spotbugs.snom.SpotBugsPlugin;
import com.github.spotbugs.snom.SpotBugsTask;
import lombok.NonNull;
import net.thebugmc.gradle.sonatypepublisher.CentralPortalExtension;
import net.thebugmc.gradle.sonatypepublisher.PublishingType;
import net.thebugmc.gradle.sonatypepublisher.SonatypeCentralPortalPublisherPlugin;
import org.cthing.gradle.plugins.buildconstants.BuildConstantsPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.quality.*;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.plugins.signing.SigningExtension;
import org.jetbrains.annotations.NotNull;
import studio.o7.remora.extensions.DefaultInformationExtension;
import studio.o7.remora.extensions.InformationExtension;
import studio.o7.remora.plugin.PaperPlugins;
import studio.o7.remora.plugins.RemoraPlugins;
import studio.o7.remora.utils.ConfigUtils;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

public class RemoraPlugin implements Plugin<@NotNull Project> {

    public static InformationExtension setupExtension(@NonNull Logger logger, @NonNull ExtensionContainer extensions) throws IllegalArgumentException {
        logger.info("Setting up the `information` extension");
        return extensions.create("information", DefaultInformationExtension.class);
    }

    public static void applyNecessaryPlugins(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Applying necessary plugins `java-library`, `shadow`, `build-constants`, `checkstyle`, `spotbugs`, `pmd`, and `sonatype-central-publisher`");

        var pluginManager = project.getPluginManager();

        pluginManager.apply(JavaLibraryPlugin.class);
        pluginManager.apply(ShadowPlugin.class);
        pluginManager.apply(BuildConstantsPlugin.class);
        pluginManager.apply(CheckstylePlugin.class);
        pluginManager.apply(SpotBugsPlugin.class);
        pluginManager.apply(PmdPlugin.class);
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
        String lombokId = "org.projectlombok:lombok:1.18.38";
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

    public static void applyCheckstyle(@NonNull Logger logger, @NonNull Project project) {
        project.getExtensions().configure(CheckstyleExtension.class, checkstyle -> {
            logger.info("Configuring extension `checkstyle`");
            checkstyle.setToolVersion("10.20.0");
            checkstyle.setIgnoreFailures(false);

            checkstyle.setConfigFile(ConfigUtils.getConfig(project, "checkstyle.xml"));
        });
    }

    public static void applyPmd(@NonNull Logger logger, @NonNull Project project) {
        project.getExtensions().configure(PmdExtension.class, pmd -> {
            logger.info("Configuring extension `pmd`");
            pmd.setToolVersion("7.9.0");
            pmd.setIgnoreFailures(true);

            pmd.setConsoleOutput(true);

            pmd.ruleSetFiles(ConfigUtils.getConfig(project, "pmd-ruleset.xml"));
        });
    }

    public static void applySpotBugs(@NonNull Logger logger, @NonNull Project project) {
        project.getExtensions().configure(SpotBugsExtension.class, spotBugs -> {
            logger.info("Configuring extension `spotBugs`");
            spotBugs.getToolVersion().set("4.9.0");
            spotBugs.getEffort().set(Effort.MAX);
            spotBugs.getReportLevel().set(Confidence.MEDIUM);
        });

        project.getTasks().withType(SpotBugsTask.class).configureEach(task -> {
            logger.info("Configuring task `spotBugs`");
            task.setIgnoreFailures(true);
            task.getExcludeFilter().set(ConfigUtils.getConfig(project, "findbugs-exclude.xml"));
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

        PaperPlugins.setupExtension(logger, extensions);
        project.afterEvaluate(p -> PaperPlugins.configureProject(logger, p));

        applyDependencies(logger, project);
        applyMavenPublishPluginConfiguration(logger, project);
        RemoraPlugins.applyShadowJar(logger, project);

        RemoraPlugins.applyBuildConstants(logger, project);

        if (!isDisabled("checkstyle")) {
            applyCheckstyle(logger, project);
        } else {
            logger.lifecycle("Skipping Checkstyle (disabled via env/property)");
            project.getTasks().withType(Checkstyle.class)
                    .configureEach(task -> task.setEnabled(false));
        }

        if (!isDisabled("pmd")) {
            applyPmd(logger, project);
        } else {
            logger.lifecycle("Skipping PMD (disabled via env/property)");
            project.getTasks().withType(Pmd.class)
                    .configureEach(task -> task.setEnabled(false));
        }

        if (!isDisabled("spotbugs")) {
            applySpotBugs(logger, project);
        } else {
            logger.lifecycle("Skipping SpotBugs (disabled via env/property)");
            project.getTasks().withType(SpotBugsTask.class)
                    .configureEach(task -> task.setEnabled(false));
        }

        applyManifest(project);
    }

    public static void applyManifest(@NonNull Project project) {
        project.getTasks().withType(Jar.class).configureEach(jar -> jar.manifest(manifest -> manifest.attributes(Map.of(
                "Implementation-Title", project.getName(),
                "Implementation-Version", project.getVersion(),
                "Build-Date", Instant.now().toString(),
                "Remora", "Build with o7studios Remora"
        ))));
    }

    private static boolean isDisabled(String name) {
        if ("true".equalsIgnoreCase(System.getProperty("remora.skip-" + name))) {
            return true;
        }

        return "true".equalsIgnoreCase(System.getenv("REMORA_SKIP_" + name.toUpperCase()));
    }
}
