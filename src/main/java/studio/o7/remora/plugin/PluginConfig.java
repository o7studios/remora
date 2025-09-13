package studio.o7.remora.plugin;

import lombok.Builder;
import lombok.Builder.Default;
import org.gradle.api.GradleException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Builder
public final class PluginConfig {
    private final String name;
    private final String main;
    @Default private final String version = "0.0.0";
    @Default private final String bootstrapper = null;
    @Default private final String loader = null;
    @Default private final String description = "Build with o7studios Remora";
    @Default private final String website = null;
    @Default private final String apiVersion = ApiVersion.PAPER_1_21_8.getApiVersion();
    @Default private final String load = "POST_WORLD";
    @Default private final Collection<String> libraries = Set.of();
    @Default private final Collection<String> depend = Set.of();
    @Default private final Collection<String> softDepend = Set.of();
    @Default private final Collection<String> loadBefore = Set.of();
    @Default private final Collection<String> provides = Set.of();


    @SuppressWarnings("ConstantValue")
    public void writeYaml(OutputStream out) throws IOException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        data.put("version", version);
        data.put("main", main);
        if (bootstrapper != null) data.put("bootstrapper", bootstrapper);
        if (loader != null) data.put("loader", loader);
        if (description != null) data.put("description", description);
        if (website != null) data.put("website", website);
        if (apiVersion != null) data.put("api-version", apiVersion);
        if (load != null) data.put("load", load);
        if (!libraries.isEmpty()) data.put("libraries", libraries);
        if (!depend.isEmpty()) data.put("depend", depend);
        if (!softDepend.isEmpty()) data.put("softdepend", softDepend);
        if (!loadBefore.isEmpty()) data.put("loadbefore", loadBefore);
        if (!provides.isEmpty()) data.put("provides", provides);

        var options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        var yaml = new Yaml(options);

        try (var writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            yaml.dump(data, writer);
        }
    }

    public String toHash() {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var input = toString();
            var bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception exception) {
            throw new GradleException("Failed to hash PluginConfig", exception);
        }
    }
}
