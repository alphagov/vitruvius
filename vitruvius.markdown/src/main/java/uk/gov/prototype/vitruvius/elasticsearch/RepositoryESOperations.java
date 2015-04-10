package uk.gov.prototype.vitruvius.elasticsearch;

import org.apache.commons.lang.StringUtils;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;

import java.io.IOException;
import java.util.List;

public interface RepositoryESOperations {

    public String register(RepositoryInformation repositoryInformation);

    public List<RepositoryInformation> getListOfRepositoryInformation(RepositoryInfoContext context) throws Exception;

    public String search(String query);

    public String aggregations(String baseQuery);

    public void shutdown();

    public RepositoryInformation getById(String id);

    public void optimise();

    public void initialise() throws IOException;

    public static class RepositoryInfoContext {

        private String typeParam;
        private String sortParam;

        public String getTypeParam() {
            return typeParam;
        }

        public void setTypeParam(String typeParam) {
            this.typeParam = typeParam;
        }

        public String getSortParam() {
            if (StringUtils.isBlank(sortParam)) {
                return "serviceName";
            }
            return sortParam;
        }

        public void setSortParam(String sortParam) {
            this.sortParam = sortParam;
        }
    }
}
