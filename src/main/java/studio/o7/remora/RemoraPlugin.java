package studio.o7.remora;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import lombok.NonNull;
import net.thebugmc.gradle.sonatypepublisher.CentralPortalExtension;
import net.thebugmc.gradle.sonatypepublisher.PublishingType;
import net.thebugmc.gradle.sonatypepublisher.SonatypeCentralPortalPublisherPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;
import studio.o7.remora.extensions.RemoraExtension;
import studio.o7.remora.extensions.framework.FastUtilsExtension;
import studio.o7.remora.extensions.framework.FrameworkExtension;
import studio.o7.remora.extensions.framework.LombokExtension;
import studio.o7.remora.extensions.publish.SonatypeMavenCentralExtension;

public class RemoraPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Logger logger = project.getLogger();

        applyNecessaryPlugins(logger, project);
        applyNecessaryRepositories(logger, project);

        RemoraExtension extension = setupExtension(logger, project);

        applyNecessaryProjectConfiguration(logger, project, extension);
        applyJavaPluginConfiguration(logger, project, extension);

        applyDependencies(logger, project, extension);

        applyMavenPublishPluginConfiguration(logger, project, extension);

        // todo
        // applyPlaceholderConfiguration(logger, project, extension);
    }

    public static RemoraExtension setupExtension(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Setting up the `remora` extension");
        return project.getExtensions().create("remora", RemoraExtension.class, project.getObjects());
    }

    public static void applyNecessaryProjectConfiguration(@NonNull Logger logger, @NonNull Project project, @NonNull RemoraExtension extension) {
        logger.info("Applying project `group` and `description` configuration");
        project.setGroup(extension.getGroupId());
        project.setDescription(extension.getDescription());
    }

    public static void applyJavaPluginConfiguration(@NonNull Logger logger, @NonNull Project project, @NonNull RemoraExtension extension) {
        logger.info("Applying java configuration");
        JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
        javaExtension.getToolchain().getLanguageVersion().set(extension.getJavaVersion());
    }

    public static void applyNecessaryPlugins(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Applying necessary plugins `java-library`, `shadow`, `maven-publish`, and `signing`");

        PluginManager pluginManager = project.getPluginManager();

        pluginManager.apply(JavaLibraryPlugin.class);
        pluginManager.apply(ShadowPlugin.class);
        pluginManager.apply(MavenPublishPlugin.class);
        pluginManager.apply(SigningPlugin.class);
    }

    public static void applyNecessaryRepositories(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Applying necessary repositories `maven-central`");

        RepositoryHandler repositories = project.getRepositories();

        repositories.mavenCentral();
    }

    public static void applyDependencies(@NonNull Logger logger, @NonNull Project project, @NonNull RemoraExtension extension) {
        DependencyHandler dependencies = project.getDependencies();

        FrameworkExtension framework = extension.getFramework();

        LombokExtension lombok = framework.getLombok();
        if (lombok.isEnabled()) {
            logger.info("Adding dependency `lombok`");

            String id = "org.projectlombok:lombok:" + lombok.getVersion();

            dependencies.add("compileOnly", id);
            dependencies.add("annotationProcessor", id);
        }

        FastUtilsExtension fastUtils = framework.getFastUtils();
        if (fastUtils.isEnabled()) {
            logger.info("Adding dependency `fastUtils`");

            String id = "it.unimi.dsi:fastutil:" + fastUtils.getVersion();

            dependencies.add(fastUtils.getScope().getConfigurationName(), id);
        }
    }

    public static void applyMavenPublishPluginConfiguration(@NonNull Logger logger, @NonNull Project project, RemoraExtension extension) {
        SonatypeMavenCentralExtension mavenCentral = extension.getMavenCentral();
        if (!mavenCentral.isEnabled()) return;

        logger.info("Applying necessary singing configuration");

        logger.info("Applying necessary plugin `sonatype-central-publisher`");
        project.getPlugins().apply(SonatypeCentralPortalPublisherPlugin.class);

        logger.info("Configuring `centralPortal` extension");
        project.getExtensions().configure(CentralPortalExtension.class, centralPortal -> {
            centralPortal.getUsername().set(System.getenv("SONATYPE_USERNAME"));
            centralPortal.getPassword().set(System.getenv("SONATYPE_PASSWORD"));
            centralPortal.getName().set(extension.getArtifactId());
            centralPortal.getPublishingType().set(PublishingType.USER_MANAGED);
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

    public static void applyPlaceholderConfiguration(@NonNull Logger logger, @NonNull Project project, @NonNull RemoraExtension extension) {
        logger.info("Applying placeholder replacements");

    }
}
