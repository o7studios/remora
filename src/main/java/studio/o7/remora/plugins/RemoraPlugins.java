package studio.o7.remora.plugins;

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.cthing.gradle.plugins.buildconstants.BuildConstantsTask;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.bundling.Jar;
import studio.o7.remora.extensions.InformationExtension;

@UtilityClass
public class RemoraPlugins {


    public void applyBuildConstants(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Configuring task `generateBuildConstants`");

        project.getTasks().withType(BuildConstantsTask.class).configureEach(task -> {
            var group = String.valueOf(project.getGroup());
            if (!group.isEmpty()) {
                logger.info("Setting constants {}", group);
                task.getClassname().set(group + ".BuildConstants");
            } else {
                task.getClassname().set(project.getName() + ".BuildConstants");
            }

            var information = project.getExtensions().getByType(InformationExtension.class);
            var additionalConstants = task.getAdditionalConstants();

            if (information.getUrl().isPresent())
                additionalConstants.put("PROJECT_URL", information.getUrl());
        });

        project.getTasks().named("sourcesJar", Jar.class, jar -> jar.dependsOn("generateBuildConstants"));
    }

    public void applyShadowJar(@NonNull Logger logger, @NonNull Project project) {
        logger.info("Configuring task `shadowJar`");

        var task = project.getTasks().withType(ShadowJar.class);

        task.configureEach(shadow -> {
            shadow.dependsOn(project.getTasks().getByName("check"));
            shadow.mergeServiceFiles();
        });
    }
}
