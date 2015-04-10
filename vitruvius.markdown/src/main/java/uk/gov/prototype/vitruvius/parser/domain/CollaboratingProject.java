package uk.gov.prototype.vitruvius.parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollaboratingProject {

    private final String description_s = "description";
    private final String name_s = "name";
    private final String link_s = "link";

    @JsonProperty
    private String description;
    @JsonProperty
    private String name;
    @JsonProperty
    private String link;

    public CollaboratingProject(@JsonProperty(description_s) String description,
                                @JsonProperty(name_s) String name,
                                @JsonProperty(link_s) String link) {
        this.description = description;
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
