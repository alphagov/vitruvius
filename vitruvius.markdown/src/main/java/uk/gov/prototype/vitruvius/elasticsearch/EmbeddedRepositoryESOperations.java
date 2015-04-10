package uk.gov.prototype.vitruvius.elasticsearch;

import org.elasticsearch.action.admin.indices.optimize.OptimizeRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.parser.domain.Meta;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.MetaType;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.MetaType.ALL;
import static uk.gov.prototype.vitruvius.parser.domain.Meta.MetaType.findTypeByString;
import static uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation.markdown_s;

public class EmbeddedRepositoryESOperations implements RepositoryESOperations {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedRepositoryESOperations.class);

    public static String SERVICES_INDEX = "services";

    private EmbeddedElasticSearch embeddedElasticSearch;


    public EmbeddedRepositoryESOperations(EmbeddedElasticSearch embeddedElasticSearch) {
        this.embeddedElasticSearch = embeddedElasticSearch;
    }


    @Override
    public void initialise() throws IOException {
        embeddedElasticSearch.createIndex();
        embeddedElasticSearch.createMappingForService();
    }


    private Client client() {
        return embeddedElasticSearch.getClient();
    }

    public String register(RepositoryInformation stub) {
        String id = UUID.nameUUIDFromBytes(stub.getRepoUri().getBytes()).toString();
        IndexResponse indexResponse = client().index(new IndexRequest(SERVICES_INDEX, "service").id(id).source(Json.encode(stub))).actionGet();
        logger.info("Index response '{}'", indexResponse.isCreated());
        return indexResponse.toString();
    }

    public List<RepositoryInformation> getListOfRepositoryInformation(RepositoryInfoContext context) throws Exception {
        String typeParam = context.getTypeParam();
        String sort = context.getSortParam();
        MetaType type = findTypeByString(typeParam);
        if (type == null) {
            throw new IllegalArgumentException("Type param '" + typeParam + "' is not valid");
        }
        SearchRequestBuilder searchRequestBuilder = client().prepareSearch(SERVICES_INDEX);
        if (type != ALL) {
            searchRequestBuilder.setQuery(termQuery(Meta.type_s, type.getStringRepresentation()));
        }

        FieldSortBuilder sortBuilder = new FieldSortBuilder(sort + ".raw");
        if (sort.equals("lastUpdated")) {
            sortBuilder = new FieldSortBuilder("lastUpdated");
            sortBuilder.order(SortOrder.DESC);
        }

        SearchResponse searchResponse = searchRequestBuilder
                .addSort(sortBuilder)
                .setFetchSource("*", markdown_s).execute().get();
        SearchHit[] aggregations = searchResponse.getHits().getHits();
        List<RepositoryInformation> listOfRepositoryInformation = new ArrayList<>();
        for (SearchHit hit : aggregations) {
            listOfRepositoryInformation.add(Json.<RepositoryInformation>decodeValue(hit.sourceAsString(), RepositoryInformation.class));
        }
        return listOfRepositoryInformation;
    }

    @Override
    public String search(String query) {

        SearchResponse searchResponse = embeddedElasticSearch.getClient()
                .prepareSearch(EmbeddedRepositoryESOperations.SERVICES_INDEX).setTypes("service")
                .setQuery(new QueryStringQueryBuilder(query))
                .addSort(new FieldSortBuilder("serviceName.raw"))
                .setSize(10000).get();

        return searchResponse.toString();
    }

    @Override
    public String aggregations(String baseQuery) {

        SearchRequestBuilder requestBuilder = embeddedElasticSearch.getClient().prepareSearch(EmbeddedRepositoryESOperations.SERVICES_INDEX).setTypes("service");
        if (baseQuery != null && !baseQuery.equals("")) {
            requestBuilder.setQuery(boolQuery().must(queryString(baseQuery)));
        }
        SearchResponse searchResponse = requestBuilder
                .setNoFields()
                .addAggregation(terms("statuses").field("status.raw").size(0))
                .addAggregation(terms("tags").field("tags.raw").size(0))
                .addAggregation(terms("departments").field("department.raw").size(0))
                .get();

        return searchResponse.toString();

    }

    @Override
    public void shutdown() {
        embeddedElasticSearch.shutdown(false);
    }

    @Override
    public RepositoryInformation getById(String id) {
        try {
            String sourceAsString = client().get(new GetRequest("services", "service", id)).actionGet().getSourceAsString();
            if (sourceAsString != null) {
                return Json.decodeValue(sourceAsString, RepositoryInformation.class);
            }
        } catch (IndexMissingException e) {
            logger.info("services index does not exist yet");
        }
        return null;
    }

    @Override
    public void optimise() {
        client().admin().indices().optimize(new OptimizeRequest(SERVICES_INDEX)).actionGet();
    }
}
