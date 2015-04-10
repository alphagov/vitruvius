package uk.gov.prototype.vitruvius.parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryUri {

    private static final String name_s = "name";
    private static final String uri_s = "uri";

    @JsonProperty
    private final String name;
    @JsonProperty
    private final String uri;

    public RepositoryUri(@JsonProperty(name_s) String name, @JsonProperty(uri_s) String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryUri that = (RepositoryUri) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RepositoryUri{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }


    public boolean isGitHub() {
        return uri.startsWith("https://") || uri.contains("github.com");
    }
}
