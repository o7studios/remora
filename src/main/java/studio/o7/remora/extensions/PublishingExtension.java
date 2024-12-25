package studio.o7.remora.extensions;

import lombok.Data;
import org.gradle.api.NamedDomainObjectContainer;
import studio.o7.remora.enums.PublisherType;

@Data
public class PublishingExtension {
    private NamedDomainObjectContainer<Publisher> publishers;


    @Data
    public static class Publisher {
        private final PublisherType type;

        private String projectName;
        private String projectUrl;
        private String projectDescription;
        private NamedDomainObjectContainer<License> licenses;
        private NamedDomainObjectContainer<Developer> developers;
        private NamedDomainObjectContainer<GitRepository> repositories;
    }

    @Data
    public static class License {
        private final String name;
        private String url;
    }

    @Data
    public static class Developer {
        private final String id;
        private String email;
        private String name;
        private String organization;
        private String organizationUrl;
    }

    @Data
    public static class GitRepository {
        private final String url;
        private String tag = "HEAD";
        private String connection;
        private String developerConnection;
    }
}
