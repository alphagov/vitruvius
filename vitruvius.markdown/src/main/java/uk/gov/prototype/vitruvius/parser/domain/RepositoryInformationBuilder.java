package uk.gov.prototype.vitruvius.parser.domain;

import org.vertx.java.core.json.impl.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositoryInformationBuilder {

    private String link;
    private String name;
    private String repoUri;
    private Meta meta;
    private List<CollaboratingProject> services = new ArrayList<>();
    private List<CollaboratingProject> components = new ArrayList<>();
    private List<CollaboratingProject> datas = new ArrayList<>();
    private String markdown;


    public static RepositoryInformationBuilder newInstance() {
        return new RepositoryInformationBuilder();
    }

    public RepositoryInformationBuilder parseAndAddMeta(String metaString) {
        meta = Json.decodeValue(metaString, Meta.class);
        return this;
    }

    public RepositoryInformationBuilder parseAndAddService(String serviceString) {
        services.add((CollaboratingProject) Json.decodeValue(serviceString, CollaboratingProject.class));
        return this;
    }
    public RepositoryInformation build() {
        return new RepositoryInformation(services, components, datas, meta, repoUri, link, name, markdown);
    }

    public RepositoryInformationBuilder repoUri(String repoUri) {
        this.repoUri = repoUri;
        return this;
    }

    public RepositoryInformationBuilder serviceName(String name) {
        this.name = name;
        return this;
    }

    public RepositoryInformationBuilder link(String link) {
        this.link = link;
        return this;
    }

    public RepositoryInformationBuilder parseAndAddComponent(String componentString) {
        components.add((CollaboratingProject) Json.decodeValue(componentString, CollaboratingProject.class));
        return this;
    }

    public RepositoryInformationBuilder parseAndAddData(String dataString) {
        datas.add((CollaboratingProject) Json.decodeValue(dataString, CollaboratingProject.class));
        return this;
    }

    public RepositoryInformationBuilder setDescription(String description) {
        Meta.Builder builder = Meta.newBuilder().clone(meta);
        builder.setDescription(description);
        meta = builder.build();
        return this;
    }

    public RepositoryInformationBuilder setLastUpdated(String updatedAt) {
        Meta.Builder builder = Meta.newBuilder().clone(meta);
        builder.setLastUpdated(updatedAt);
        meta = builder.build();
        return this;
    }

    public RepositoryInformationBuilder markdown(String markdown) {
        this.markdown = markdown;
        return this;
    }
}
