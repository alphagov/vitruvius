package uk.gov.prototype.vitruvius.elasticsearch;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class IndexMapping {

    public static String getServiceMapping() {

        try {
            InputStream resourceAsStream = IndexMapping.class.getClassLoader().getResourceAsStream("mapping.json");
            return IOUtils.toString(resourceAsStream);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
