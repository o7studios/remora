package studio.o7.remora;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import lombok.NonNull;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.plugins.signing.SigningPlugin;
import studio.o7.remora.extensions.RemoraExtension;

public class RemoraPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Logger logger = project.getLogger();

        applyNecessaryPlugins(logger, project);
        applyNecessaryRepositories(logger, project);

        RemoraExtension extension = setupExtension(logger, project);

        applyNecessaryProjectConfiguration(logger, project, extension);
        applyJavaPluginConfiguration(logger, project, extension);

        //applyDependencies(logger, project, extension);

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

        /*
        FrameworkExtension framework = null;

        logger.info("Adding dependency `lombok`");

        String lombokId = "org.projectlombok:lombok:1.18.36";

        dependencies.add("compileOnly", lombokId);
        dependencies.add("annotationProcessor", lombokId);

        FrameworkExtension.FastUtils fastUtils = framework.getFastUtils();
        if (fastUtils.isEnabled()) {
            logger.info("Adding dependency `fastUtils`");

            String id = "it.unimi.dsi:fastutil:" + fastUtils.getVersion();

            dependencies.add(fastUtils.getScope().getConfigurationName(), id);
        }
         */
    }

    public static void applyPlaceholderConfiguration(@NonNull Logger logger, @NonNull Project project, @NonNull RemoraExtension extension) {
        logger.info("Applying placeholder replacements");

    }
}
