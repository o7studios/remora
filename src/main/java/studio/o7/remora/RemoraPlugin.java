package studio.o7.remora;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import lombok.NonNull;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.plugins.signing.SigningPlugin;
import studio.o7.remora.extensions.FrameworkExtension;
import studio.o7.remora.extensions.RemoraExtension;

public class RemoraPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Logger logger = project.getLogger();

        applyNecessaryPlugins(logger, project);
        applyNecessaryRepositories(logger, project);

        RemoraExtension extension = project.getExtensions().create("remora", RemoraExtension.class);

        applyDependencies(logger, project, extension);


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

        FrameworkExtension.Lombok lombok = framework.getLombok();
        if (lombok.isEnabled()) {
            logger.info("Adding dependency `lombok`");

            String id = "org.projectlombok:lombok:" + lombok.getVersion();

            dependencies.add("compileOnly", id);
            dependencies.add("annotationProcessor", id);
        }

        FrameworkExtension.FastUtils fastUtils = framework.getFastUtils();
        if (fastUtils.isEnabled()) {
            logger.info("Adding dependency `fastUtils`");

            String id = "it.unimi.dsi:fastutil:" + lombok.getVersion();

            dependencies.add(fastUtils.getScope().getConfigurationName(), id);
        }


    }
}
