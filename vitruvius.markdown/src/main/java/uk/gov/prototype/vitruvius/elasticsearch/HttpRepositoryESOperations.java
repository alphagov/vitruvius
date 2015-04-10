package uk.gov.prototype.vitruvius.elasticsearch;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.MetaType;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.MetaType.ALL;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.type_s;
import static uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation.markdown_s;

public class HttpRepositoryESOperations implements RepositoryESOperations {

    private static final Logger logger = LoggerFactory.getLogger(HttpRepositoryESOperations.class);

    private HttpESClient client;

    public HttpRepositoryESOperations(HttpESClient client) {
        this.client = client;
    }

    @Override
    public void initialise() {
        client.createIndex();
        client.createMappingForService();
    }


    public String register(RepositoryInformation stub) {
        try {
            return client.index(stub);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<RepositoryInformation> getListOfRepositoryInformation(RepositoryInfoContext context) throws Exception {
        String typeParam = context.getTypeParam();
        String sort = context.getSortParam();
        MetaType type = MetaType.findTypeByString(typeParam);
        if (type == null) {
            throw new IllegalArgumentException("Type param '" + typeParam + "' is not valid");
        }
        TermQueryBuilder query = null;
        if (type != ALL) {
            query = termQuery(type_s, type.getStringRepresentation());
        }
        return getRepositoryInformation(query, sort);

    }


    @Override
    public String search(String query) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new FieldSortBuilder("serviceName.raw"));
        searchSourceBuilder.query(new QueryStringQueryBuilder(query));
        return client.search(searchSourceBuilder.toString());

    }

    @Override
    public String aggregations(String baseQuery) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fields();
        if (StringUtils.isNotEmpty(baseQuery)) {
            searchSourceBuilder.query(new BoolQueryBuilder().must(queryString(baseQuery)));
        }
        searchSourceBuilder
                .aggregation(terms("statuses").field("status.raw").size(0))
                .aggregation(terms("tags").field("tags.raw").size(0))
                .aggregation(terms("departments").field("department.raw").size(0));
        return client.search(searchSourceBuilder.toString());

    }

    @Override
    public void shutdown() {
        client.shutdown();
    }

    @Override
    public RepositoryInformation getById(String id) {
        WebResource resource = client.getClient().resource(client.getBaseUrl() + "services" + '/' + "service/" + id);
        ClientResponse response = resource.accept("application/json").type("application/json").get(ClientResponse.class);
        String rawResponse = client.generateResponse(response);
        JsonObject source = new JsonObject(rawResponse).getField("_source");
        return (source != null) ? (RepositoryInformation) Json.decodeValue(source.encode(), RepositoryInformation.class) : null;
    }

    @Override
    public void optimise() {
        WebResource resource = client.getClient().resource(client.getBaseUrl() + "services/_optimize");
        ClientResponse response = resource.accept("application/json").type("application/json").get(ClientResponse.class);
        String rawResponse = client.generateResponse(response);
        logger.info("response from optimize '{}'", rawResponse);
    }

    private List<RepositoryInformation> getRepositoryInformation(QueryBuilder query, String sort) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (query != null) {
            searchSourceBuilder.query(query);
        }

        FieldSortBuilder sortBuilder = new FieldSortBuilder(sort + ".raw");
        if (sort.equals("lastUpdated")) {
            sortBuilder = new FieldSortBuilder("lastUpdated");
            sortBuilder.order(SortOrder.DESC);
        }

        searchSourceBuilder.fetchSource("*", markdown_s).sort(sortBuilder);
        String searchResponse = client.search(searchSourceBuilder.toString());
        List<RepositoryInformation> listOfRepositoryInformation = new ArrayList<>();
        JsonObject initialHits = new JsonObject(searchResponse).getField("hits");
        if (initialHits != null) {
            JsonArray hits = initialHits.getArray("hits");
            for (Object hit : hits) {
                JsonObject next = (JsonObject) hit;
                listOfRepositoryInformation.add(Json.<RepositoryInformation>decodeValue(next.getField("_source").toString(), RepositoryInformation.class));
            }
        }
        return listOfRepositoryInformation;
    }

}
