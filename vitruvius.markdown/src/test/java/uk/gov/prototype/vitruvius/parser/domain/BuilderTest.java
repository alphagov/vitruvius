package uk.gov.prototype.vitruvius.parser.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.Builder;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.newBuilder;

public class BuilderTest {

    private static final String DESCRIPTION = "test meta description";
    private static final String LAST_UPDATED = "whenever";
    private static final String STATUS = "example";
    private static final String CONTACT = "Someone <Someone@SomeOrg.com>";
    private static final String VERSION = "1.0";
    private static final String TYPE = Meta.MetaType.COMPONENT.getStringRepresentation();
    private static final List<String> TAGS = new ArrayList<>();
    private static final String TAG_ONE = "tag one";
    private static final String TAG_TWO = "tag two";

    @Before
    public void setUp(){
        TAGS.add(TAG_ONE);
        TAGS.add(TAG_TWO);
    }

    @Test
    public void testBuild() throws Exception {
        Meta meta = new Builder().setDescription(DESCRIPTION).setLastUpdated(LAST_UPDATED).status(STATUS).author(CONTACT).version(VERSION)
                .type(TYPE).tags(TAGS).build();
        assertThat(meta).isNotNull();
        assertThat(meta.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(meta.getLastUpdated()).isEqualTo(LAST_UPDATED);
        assertThat(meta.getStatus()).isEqualTo(STATUS);
        assertThat(meta.getAuthor()).isEqualTo(CONTACT);
        assertThat(meta.getVersion()).isEqualTo(VERSION);
        assertThat(meta.getType()).isEqualTo(TYPE);
        assertThat(meta.getTags()).isEqualTo(TAGS);
    }

    @Test
    public void testClone() throws Exception {
        Meta meta = new Builder().setDescription(DESCRIPTION).setLastUpdated(LAST_UPDATED).status(STATUS).author(CONTACT).version(VERSION)
                .type(TYPE).tags(TAGS).build();
        Meta clonedMeta = newBuilder().clone(meta).build();
        assertThat(clonedMeta).isEqualsToByComparingFields(meta);
    }
}