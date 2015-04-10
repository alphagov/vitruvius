package uk.gov.prototype.vitruvius.parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vertx.java.core.shareddata.Shareable;
import uk.gov.prototype.vitruvius.parser.github.GitHubUriParser;

import java.util.List;

public class RepositoryInformation implements Shareable{

    public static final String services_s = "services";
    public static final String component_s = "components";
    public static final String meta_s = "meta";
    public static final String repoUri_s = "repoUri";
    public static final String link_s = "link";
    public static final String serviceName_s = "serviceName";
    public static final String markdown_s = "markdown";
    public static final String data_s = "data";


    @JsonProperty
    private List<CollaboratingProject> services;
    @JsonProperty
    private List<CollaboratingProject> components;
    @JsonProperty
    private Meta meta;
    @JsonProperty
    private String repoUri;
    @JsonProperty
    private String link;
    @JsonProperty
    private String serviceName;
    @JsonProperty
    private List<CollaboratingProject> data;
    @JsonProperty
    private String markdown;

    public RepositoryInformation(@JsonProperty(services_s) List<CollaboratingProject> services,
                                 @JsonProperty(component_s) List<CollaboratingProject> components,
                                 @JsonProperty(data_s) List<CollaboratingProject> data,
                                 @JsonProperty(meta_s) Meta meta,
                                 @JsonProperty(repoUri_s) String repoUri,
                                 @JsonProperty(link_s) String link,
                                 @JsonProperty(serviceName_s) String serviceName,
                                 @JsonProperty(markdown_s)String markdown) {
        this.services = services;
        this.components = components;
        this.data = data;
        this.meta = meta;
        this.markdown = markdown;
        if(repoUri != null)
            this.repoUri = makeRepoUri(new GitHubUriParser(repoUri));
        this.link = link;
        this.serviceName = serviceName;
    }

    private static String makeRepoUri(GitHubUriParser gitHubUriParser) {
        return gitHubUriParser.repoUri();
    }

    public String getLink() {
        return link;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getRepoUri() {
        return repoUri;
    }

    public Meta getMeta() {
        return meta;
    }

    public List<CollaboratingProject> getServices() {
        return services;
    }

    public List<CollaboratingProject> getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return "RepositoryInformation{" +
                ", meta=" + meta +
                ", repoUri='" + repoUri + '\'' +
                ", link='" + link + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", number of services='" + services.size() +"'"+
                ", number of components='" + components.size() +"'"+
                ", number of data='" + data.size() +"'"+
                '}';
    }

    public List<CollaboratingProject> getData() {
        return data;
    }

    public String getMarkdown() {
        return markdown;
    }
}
