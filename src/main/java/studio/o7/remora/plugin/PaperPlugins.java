package studio.o7.remora.plugin;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.language.jvm.tasks.ProcessResources;
import studio.o7.remora.extensions.InformationExtension;
import studio.o7.remora.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@UtilityClass
public class PaperPlugins {

    public static void setupExtension(@NonNull Logger logger, @NonNull ExtensionContainer extensions) throws IllegalArgumentException {
        logger.info("Setting up the `plugin` extension");
        extensions.create("plugin", PluginExtension.class);
    }

    public void configureProject(@NonNull Logger logger, @NonNull Project project) {
        var extension = project.getExtensions().findByType(PluginExtension.class);
        if (extension == null) return;
        if (!extension.getEnabled().orElse(false).get()) return;
        logger.info("Configuring paper plugin with `plugin` extension");
        var infoExtension = project.getExtensions().getByType(InformationExtension.class);

        var builder = PluginConfig.builder();

        var dependencies = project.getDependencies();

        // Set paper-api version stuff
        var apiVersion = extension.getApiVersion().orElse(ApiVersion.DEFAULT).get();

        var apiDependency = apiVersion.getApiDependency();
        logger.info("Adding paper-api dependency: {}", apiDependency);
        dependencies.add("compileOnlyApi", apiDependency);

        var pluginApiVersion = apiVersion.getApiVersion();
        logger.info("Setting api-version: {}", pluginApiVersion);
        builder.apiVersion(pluginApiVersion);


        // Set libraries
        var libraries = extension.getLibraries().orElse(List.of()).get();
        builder.libraries(libraries);

        for (var library : libraries) {
            logger.info("Adding library as dependency: {}", library);
            dependencies.add("compileOnlyApi", library);
        }


        // Set information
        var artifactId = infoExtension.getArtifactId().orElse("").get();
        if (artifactId.isBlank())
            throw new GradleException("Paper plugin requires `artifactId` in Remora extension `information` to be set");
        builder.name(StringUtils.toPascalCase(artifactId));

        var version = project.getVersion().toString();
        if (!version.equals("unspecified")) builder.version(version);

        var pluginDesc = infoExtension.getDescription().orElse("").get();
        if (!pluginDesc.isBlank()) builder.description(pluginDesc);

        var pluginWebsite = infoExtension.getUrl().orElse("").get();
        if (!pluginWebsite.isBlank()) builder.website(pluginWebsite);


        // Set main
        var pluginMain = extension.getMain().orElse("").get();
        if (pluginMain.isBlank())
            throw new GradleException("Remora extension `plugin` requires `main` to be set");

        builder.main(pluginMain);

        // Set load
        var load = extension.getLoad().orElse(Load.POST_WORLD).get();
        builder.load(load.name().replace("_", ""));


        // Set depends
        var depends = extension.getDepend().orElse(List.of()).get();
        builder.depend(depends);


        // Set softDepends
        var softDepends = extension.getSoftDepend().orElse(List.of()).get();
        builder.softDepend(softDepends);


        // Set provides
        var provides = extension.getProvides().orElse(List.of()).get();
        builder.provides(provides);


        // Set loadBefore
        var loadBefore = extension.getLoadBefore().orElse(List.of()).get();
        builder.loadBefore(loadBefore);


        var pluginConfig = builder.build();

        var outputDir = project.getLayout().getBuildDirectory().dir("generated/resources/paper");

        var hash = pluginConfig.toHash();

        var generateYaml = project.getTasks().register("generatePaperPluginYaml", task -> {
            task.getInputs().property("pluginConfig", hash);

            var outputFile = outputDir.map(dir -> new File(dir.getAsFile(), "plugin.yml"));
            task.getOutputs().file(outputFile);

            task.doLast(_ -> {
                try {
                    var file = outputFile.get();
                    var _ = file.getParentFile().mkdirs();

                    try (var fos = new FileOutputStream(file)) {
                        pluginConfig.writeYaml(fos);
                    }

                    logger.info("Generated `paper-plugin.yml`");
                } catch (Exception exception) {
                    throw new GradleException("Failed to generate paper-plugin.yml", exception);
                }
            });
        });


        project.getTasks().withType(ProcessResources.class).configureEach(task -> {
            task.dependsOn(generateYaml);
            task.from(project.getLayout().getBuildDirectory().dir("generated/resources/paper"));
        });
    }
}
