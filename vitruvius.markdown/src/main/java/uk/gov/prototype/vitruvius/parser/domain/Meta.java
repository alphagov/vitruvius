package uk.gov.prototype.vitruvius.parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Meta {

    public static final String status_s = "status";
    public static final String description_s = "description";
    public static final String author_s = "author";
    public static final String lastUpdated_s = "lastUpdated";
    public static final String version_s = "version";
    public static final String department_s = "department";
    public static final String tags_s = "tags";
    public static final String type_s = "type";

    @JsonProperty
    private String status;
    @JsonProperty
    private String description;
    @JsonProperty
    private String author;
    @JsonProperty
    private String lastUpdated;
    @JsonProperty
    private String version;
    @JsonProperty
    private String department;
    @JsonProperty
    private List<String> tags;
    @JsonProperty
    private String type;

    public Meta() {
    }


    public Meta(@JsonProperty(status_s) String status,
                @JsonProperty(description_s) String description,
                @JsonProperty(author_s) String author,
                @JsonProperty(lastUpdated_s) String lastUpdated,
                @JsonProperty(version_s) String version,
                @JsonProperty(department_s) String department,
                @JsonProperty(tags_s) List<String> tags,
                @JsonProperty(type_s) String type) {
        this.status = status;
        this.description = description;
        this.author = author;
        this.lastUpdated = lastUpdated;
        this.version = version;
        this.department = department;
        this.tags = tags;
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDepartment() {
        return department;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public enum MetaType {
        PLATFORM("platform"), SERVICE("service"), COMPONENT("component"), REGISTER("register"), ALL("all");
        private String stringRepresentation;

        private MetaType(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        public String getStringRepresentation() {
            return stringRepresentation;
        }

        public static boolean isValidTypeByString(String typeString) {
            MetaType type = findTypeByString(typeString);
            if (type != null && type != ALL) {
                return true;
            }
            return false;
        }

        public static MetaType findTypeByString(String typeString) {
            for (MetaType metaType : MetaType.values()) {
                if (metaType.getStringRepresentation().equals(typeString)) {
                    return metaType;
                }
            }
            return null;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String description;
        private String lastUpdated;
        private String status;
        private String author;
        private String version;
        private String department;
        private List<String> tags;
        private String type;

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Meta build() {
            return new Meta(status, description, author, lastUpdated, version, department, tags, type);
        }

        public Builder clone(Meta meta){
            this.status = meta.getStatus();
            this.description = meta.getDescription();
            this.author = meta.getAuthor();
            this.lastUpdated = meta.getLastUpdated();
            this.version = meta.getVersion();
            this.department = meta.getDepartment();
            this.tags = meta.getTags();
            this.type = meta.getType();
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
    }
}