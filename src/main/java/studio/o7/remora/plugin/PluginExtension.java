package studio.o7.remora.plugin;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PluginExtension {

    Property<@NotNull Boolean> getEnabled();

    Property<@NotNull String> getMain();

    Property<@NotNull ApiVersion> getApiVersion();

    Property<@Nullable Load> getLoad();

    ListProperty<@NotNull String> getLibraries();

    ListProperty<@NotNull String> getDepend();

    ListProperty<@NotNull String> getSoftDepend();

    ListProperty<@NotNull String> getLoadBefore();

    ListProperty<@NotNull String> getProvides();
}
