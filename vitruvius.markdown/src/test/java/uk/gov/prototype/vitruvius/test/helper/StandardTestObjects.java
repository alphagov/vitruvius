package uk.gov.prototype.vitruvius.test.helper;

import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;

import java.util.Date;
import java.util.UUID;

public class StandardTestObjects {

    public static final String TEST_MARKDOWN = "some text";
    private static final String TEST_META_STATUS = "Draft";
    private static final String TEST_META_DESCRIPTION = "Very brief description of this service.";
    private static final String TEST_META_AUTHOR = "Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>";
    private static final String TEST_META_LAST_UPDATED = ISODateTimeFormat.dateHourMinuteSecond().print(new Date().getTime());
    private static final String TEST_META_VERSION = "0.1";
    public static final String TEST_META_SERVICE = "service";
    private static final String TEST_META_COMPONENT = "component";

    public static final String metaDataGenerator(String type) {
        return "{\"status\":\"" + TEST_META_STATUS + "\"," +
                "\"description\":\"" + TEST_META_DESCRIPTION + "\"," +
                "\"author\":\"" + TEST_META_AUTHOR + "\"," +
                "\"lastUpdated\":\"" + TEST_META_LAST_UPDATED + "\"," +
                "\"version\":\"" + TEST_META_VERSION + "\"," +
                "\"department\":\"" + "department" + "\"," +
                "\"tags\":[\"tag one\", \"tag one\"]," +
                "\"type\":\"" + type + "\"" +
                "}";
    }
    public static final String metaDataGeneratorWithTags(String type, String tags, String department) {
        return "{\"status\":\"" + TEST_META_STATUS + "\"," +
                "\"description\":\"" + TEST_META_DESCRIPTION + "\"," +
                "\"author\":\"" + TEST_META_AUTHOR + "\"," +
                "\"lastUpdated\":\"" + TEST_META_LAST_UPDATED + "\"," +
                "\"version\":\"" + TEST_META_VERSION + "\"," +
                "\"department\":\"" + department + "\"," +
                "\"tags\":[" + tags + "]," +
                "\"type\":\"" + type + "\"" +
                "}";
    }

    public static RepositoryInformation getStandardService(){
        return new RepositoryInformationBuilder().serviceName("Service").repoUri("https://api.github.com/repos/owner/"+ UUID.randomUUID()).parseAndAddMeta(metaDataGenerator(TEST_META_SERVICE)).markdown(TEST_MARKDOWN).build();
    }

    public static RepositoryInformation getStandardService(String name){
        return new RepositoryInformationBuilder().serviceName("Service").repoUri("https://api.github.com/repos/"  + name).parseAndAddMeta(metaDataGenerator(TEST_META_SERVICE)).markdown(TEST_MARKDOWN).build();
    }

    public static RepositoryInformation getStandardComponent() {
        return new RepositoryInformationBuilder().serviceName("Component").repoUri("https://api.github.com/repos/owner/" + UUID.randomUUID()).parseAndAddMeta(metaDataGenerator(TEST_META_COMPONENT)).markdown(TEST_MARKDOWN).build();
    }

    public static RepositoryInformation getStandardServiceWithName(String serviceName) {
        return new RepositoryInformationBuilder().serviceName(serviceName).repoUri("https://api.github.com/repos/owner/" + UUID.randomUUID()).parseAndAddMeta(metaDataGenerator(TEST_META_SERVICE)).markdown(TEST_MARKDOWN).build();
    }

    public static RepositoryInformation getStandardServiceWithTagsAndDepartment(String tags, String department) {
        return new RepositoryInformationBuilder().serviceName("Service").repoUri("https://api.github.com/repos/owner/"+ UUID.randomUUID()).parseAndAddMeta(metaDataGeneratorWithTags(TEST_META_SERVICE, tags, department)).markdown(TEST_MARKDOWN).build();
    }
}
