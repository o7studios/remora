package studio.o7.remora.extensions;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import studio.o7.remora.RemoraPlugin;
import studio.o7.remora.extensions.framework.FrameworkExtension;
import studio.o7.remora.extensions.framework.LombokExtension;

import static org.junit.jupiter.api.Assertions.*;

class RemoraExtensionTest {

    @Test
    public void testExtension() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(RemoraPlugin.class);

        RemoraExtension extension = project.getExtensions().findByType(RemoraExtension.class);
        assertNotNull(extension);

        FrameworkExtension framework = extension.getFramework();
        assertNotNull(framework);

        LombokExtension lombok = framework.getLombok();
        assertNotNull(lombok);

        String version = lombok.getVersion();
        assertNotNull(version);
        assertEquals("1.18.36", version);
    }
}